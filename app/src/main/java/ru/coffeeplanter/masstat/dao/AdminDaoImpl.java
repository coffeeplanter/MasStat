package ru.coffeeplanter.masstat.dao;

import android.content.Context;
import android.os.Handler;
import android.os.Parcel;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.plumillonforge.android.chipview.Chip;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.coffeeplanter.masstat.MasStatApp;
import ru.coffeeplanter.masstat.entities.Keyword;
import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;
import ru.coffeeplanter.masstat.net.NetCallback;

/**
 * Class implementing AdminDao functionality.
 */

public class AdminDaoImpl implements AdminDao {

    private final String TAG = "AdminDaoImpl";

//    private Context mContext; // This is needed for interacting with SharedPreferences.

    private LocalStorage mLocalStorage;

//    private NetCallback mOnSitesGet;

    private Gson mGson;

    public AdminDaoImpl(Context context) {
//        mOnSitesGet = onSitesGet;
//        mContext = context;
        mLocalStorage = new LocalStorageImpl(context);
        mGson = new Gson();
    }

    @Override
    public void getSites(final NetCallback callback) {
        MasStatApp.getApi().getSites().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                String jsonSiteArray = response.body().toString();
                mLocalStorage.saveSites(jsonSiteArray);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        callback.success();
                    }
                }, 100);
                Log.d(TAG, response.toString());
            }
            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                callback.failure();
                Log.d(TAG, "Server connection failed while getting sites");
            }
        });
    }

    @Override
    public void addSite(Site site, final NetCallback callback) {
        MasStatApp.getApi().postSite(site).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.toString().contains("code=200")) {
                    getSites(callback);
                } else {
                    Log.e(TAG, "Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void saveSite(Site site, final NetCallback callback) {
        MasStatApp.getApi().putSite(site).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.toString().contains("code=200")) {
                    getSites(callback);
                } else {
                    Log.e(TAG, "Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void deleteSite(int siteId, final NetCallback callback) {
        MasStatApp.getApi().deleteSite(siteId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.toString().contains("code=200")) {
                    getSites(callback);
                } else {
                    Log.e(TAG, "Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    @Override
    public void getPersons(final NetCallback callback, final boolean loadKeywords) {

        MasStatApp.getApi().getPersons().enqueue(new Callback<JsonArray>() {
            @Override
            public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {

                String jsonPersonArray = response.body().toString();

                Type listType = new TypeToken<List<Person>>(){}.getType();
                final List<Person> persons = mGson.fromJson(jsonPersonArray, listType);

                if (loadKeywords) {
                    MasStatApp.getApi().getKeywords().enqueue(new Callback<JsonArray>() {
                        @Override
                        public void onResponse(Call<JsonArray> call, Response<JsonArray> response) {
                            String jsonKeywordArray = response.body().toString();
                            Type listType = new TypeToken<List<Keyword>>() {
                            }.getType();
                            List<Keyword> keywords = mGson.fromJson(jsonKeywordArray, listType);

                            for (Person person : persons) {
                                for (int i = 0; i < keywords.size(); i++) {
                                    if (person.getId() == keywords.get(i).getPersonId()) {
                                        person.addKeyword(keywords.get(i));
                                        keywords.remove(i);
                                        i--;
                                    }
                                }
                            }

                            String json = new Gson().toJson(persons);
                            mLocalStorage.savePersons(json);

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    callback.success();
                                }
                            }, 100);
                            Log.d(TAG, response.toString());

                        }

                        @Override
                        public void onFailure(Call<JsonArray> call, Throwable t) {
                            callback.failure();
                            Log.d(TAG, "Server connection failed while getting keywords");
                        }
                    });
                } else {
                    String json = new Gson().toJson(persons);
                    mLocalStorage.savePersons(json);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            callback.success();
                        }
                    }, 100);
                    Log.d(TAG, response.toString());
                }

            }

            @Override
            public void onFailure(Call<JsonArray> call, Throwable t) {
                callback.failure();
                Log.d(TAG, "Server connection failed while getting persons");
            }
        });

    }

    @Override
    public void addPerson(final Person person, final NetCallback callback) {

        MasStatApp.getApi().postPerson(person).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.toString().contains("code=200")) {
                    getPersons(new NetCallback() {
                        @Override
                        public void success() {
                            Log.d(TAG, "NetCallback.success() call");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Log.d(TAG, "Handler.postDelayed() call");
                                    List<Person> personsStored = mLocalStorage.readPersons();
                                    Person maxIdPerson = new Person();
                                    assert personsStored != null;
                                    if (personsStored.size() == 1) {
                                        maxIdPerson = personsStored.get(0);
                                    } else if (personsStored.size() > 1) {
                                        for (int i = 1; i < personsStored.size(); i++) {
                                            if (personsStored.get(i).getId() > personsStored.get(i - 1).getId()) {
                                                maxIdPerson = personsStored.get(i);
                                            }
                                        }
                                    }
                                    if (maxIdPerson.getName().equals(person.getName())) {
                                        Log.d(TAG, person.toString());
                                        Log.d(TAG, person.getKeywords().toString());
                                        if ((person.getKeywords() != null) && !person.getKeywords().isEmpty()) {
                                            for (int i = 0; i < person.getKeywords().size(); i++) {
                                                ((Keyword) person.getKeywords().get(i)).setPersonId(maxIdPerson.getId());
                                            }
                                            postKeywordList(person.getKeywords(), callback);
                                        }
                                    }
                                }
                            }, 100);
                        }

                        @Override
                        public void failure() {

                        }

                        @Override
                        public int describeContents() {
                            return 0;
                        }

                        @Override
                        public void writeToParcel(Parcel dest, int flags) {

                        }
                    }, false);
                } else {
                    Log.e(TAG, "Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void savePerson(final Person person, final NetCallback callback) {
        MasStatApp.getApi().putPerson(person).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                final List<Chip> keywords = person.getKeywords();
                for (int i = 0; i < keywords.size(); i++) {
                    final int finalI = i;
                    MasStatApp.getApi().deleteKeyword(((Keyword) keywords.get(i)).getId()).enqueue(new Callback<ResponseBody>() {
                        @Override
                        public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                            if (!response.toString().contains("code=200")) {
                                Log.e(TAG, "Error: " + response.toString());
                            }
                            if (finalI == (keywords.size() - 1)) {
                                getPersons(callback, true);
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseBody> call, Throwable t) {

                        }
                    });
                }
                if ((person.getAdditionalKeywords() != null) && !person.getAdditionalKeywords().isEmpty()) {
                    postKeywordList(person.getAdditionalKeywords(), callback);
                }
                if (person.getKeywords().isEmpty()) {
                    getPersons(callback, true);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });

    }

    @Override
    public void deletePerson(int personId, final NetCallback callback) {
        MasStatApp.getApi().deletePerson(personId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.toString().contains("code=200")) {
                    getPersons(callback, true);
                } else {
                    Log.e(TAG, "Error: " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }

    private void postKeywordList(final List<Chip> keywords, final NetCallback callback) {
        for (int i = 0; i < keywords.size(); i++) {
            final int finalI = i;
            MasStatApp.getApi().postKeyword((Keyword) keywords.get(i)).enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.toString().contains("code=200")) {
                        if (finalI == (keywords.size() - 1)) {
                            getPersons(callback, true);
                        }
                    } else {
                        Log.e(TAG, "Error: " + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

}
