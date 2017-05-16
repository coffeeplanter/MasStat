package ru.coffeeplanter.masstat;

import android.app.Application;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.coffeeplanter.masstat.net.ServerApi;

/**
 * Application class for holding Retrofit object and Retrofit interface object.
 */

public class MasStatApp extends Application {

    private final String TAG = "MasStatApp";

    private final String BASE_URL = "http://188.166.160.168:8085/";

    private static ServerApi sServerApi;
    private Retrofit mRetrofit;


    @Override
    public void onCreate() {
        super.onCreate();
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(logging)
                .build();
        Gson gson = new GsonBuilder()
                .excludeFieldsWithoutExposeAnnotation()
                .create();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(okHttpClient)
                .build();
        Log.d(TAG, "" + mRetrofit.toString());
        sServerApi = mRetrofit.create(ServerApi.class);
        Log.d(TAG, "" + sServerApi.toString());
    }

    public static ServerApi getApi() {
        return sServerApi;
    }

}
