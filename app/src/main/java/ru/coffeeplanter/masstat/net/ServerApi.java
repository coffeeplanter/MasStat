package ru.coffeeplanter.masstat.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import ru.coffeeplanter.masstat.entities.Keyword;
import ru.coffeeplanter.masstat.entities.Person;
import ru.coffeeplanter.masstat.entities.Site;

/**
 * Interface for HTTP requests using Retrofit.
 */

public interface ServerApi {


    // Get site list
    @GET("api/sites/")
    Call<JsonArray> getSites();

    // Post new site in application/x-www-form-urlencoded format
//    @FormUrlEncoded
//    @POST("api/sites/")
//    Call<SiteTest> postSite(@Field("name") String name, @Field("base_url") String baseUrl, @Field("open_tag") String openTag, @Field("close_tag") String closeTag);

    @POST("api/sites/")
    Call<ResponseBody> postSite(@Body Site site);

    // Put changed site
    @PUT("api/sites/")
    Call<ResponseBody> putSite(@Body Site site);

    // Delete site
    @DELETE("api/sites/{siteId}")
    Call<ResponseBody> deleteSite(@Path("siteId") int siteId);

    // Get person list
    @GET("api/persons/")
    Call<JsonArray> getPersons();

    // Post new person
    @POST("api/persons/")
    Call<ResponseBody> postPerson(@Body Person person);

    // Put changed person
    @PUT("api/persons/")
    Call<ResponseBody> putPerson(@Body Person person);

    // Delete person
    @DELETE("api/persons/{personId}")
    Call<ResponseBody> deletePerson(@Path("personId") int personId);

    // Get keyword list
    @GET("api/keywords/")
    Call<JsonArray> getKeywords();

    // Post new keyword
    @POST("api/keywords/")
    Call<ResponseBody> postKeyword(@Body Keyword keyword);

    // Delete keyword
    @DELETE("api/keywords/{keywordId}")
    Call<ResponseBody> deleteKeyword(@Path("keywordId") int keywordId);

}
