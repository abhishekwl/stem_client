package io.github.abhishekwl.stemclient.Helpers;

import java.util.ArrayList;

import io.github.abhishekwl.stemclient.Models.Test;
import io.github.abhishekwl.stemclient.Models.User;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    //TESTS ENDPOINT
    @GET("tests/popular")
    Call<ArrayList<Test>> getPopularTests(@Query("uid") String clientUid, @Query("city") String clientCityName);
    @GET("tests")
    Call<ArrayList<Test>> getMatchingTests(@Query("uid") String clientUid, @Query("city") String clientCityName, @Query("test") String searchKey);
    @GET("tests/{testId}")
    Call<Test> getTest(@Query("uid") String clientUid, @Query("city") String clientCityName, @Path("testId") String testId);


    //USERS ENDPOINT
    @POST("users")
    @FormUrlEncoded
    Call<User> createNewUser(@Field("uid") String uid, @Field("name") String name, @Field("age") int age, @Field("blood") String bloodGroup, @Field("gender") boolean gender, @Field("contact") String contactNumber, @Field("email") String emailAddress);


    //ORDERS ENDPOINT
}
