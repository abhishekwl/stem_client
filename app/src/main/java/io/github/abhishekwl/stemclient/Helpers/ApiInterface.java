package io.github.abhishekwl.stemclient.Helpers;

import java.util.ArrayList;

import io.github.abhishekwl.stemclient.Models.Test;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiInterface {

    @GET("tests/popular")
    Call<ArrayList<Test>> getPopularTests(@Query("uid") String clientUid, @Query("city") String clientCityName);

    @GET("tests")
    Call<ArrayList<Test>> getMatchingTests(@Query("uid") String clientUid, @Query("city") String clientCityName, @Query("test") String searchKey);

    @GET("tests/{testId}")
    Call<Test> getTest(@Query("uid") String clientUid, @Query("city") String clientCityName, @Path("testId") String testId);

}
