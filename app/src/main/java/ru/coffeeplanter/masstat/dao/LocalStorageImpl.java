package ru.coffeeplanter.masstat.dao;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;

/**
 * Class implementing managing data in SharedPreferences.
 */

public class LocalStorageImpl implements LocalStorage {

    private final String PREF_SITES_LIST = "sites_list";
    private final String PREF_PERSONS_LIST = "persons_list";

    private Context mContext;

    private Gson mGson;

    public LocalStorageImpl(Context context) {
        mContext = context;
        mGson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
    }

    @Override
    public void saveSites(String jsonString) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(PREF_SITES_LIST, jsonString)
                .apply();
    }

    @Nullable
    @Override
    public List<Site> readSites() {
        String jsonSites = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(PREF_SITES_LIST, null);
        Type listType = new TypeToken<List<Site>>(){}.getType();
        return mGson.fromJson(jsonSites, listType);
    }

    @Override
    public void savePersons(String jsonString) {
        PreferenceManager.getDefaultSharedPreferences(mContext)
                .edit()
                .putString(PREF_PERSONS_LIST, jsonString)
                .apply();
    }

    @Nullable
    @Override
    public List<Person> readPersons() {
        String jsonSites = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(PREF_PERSONS_LIST, null);
        Type listType = new TypeToken<List<Person>>(){}.getType();
        return mGson.fromJson(jsonSites, listType);
    }

}
