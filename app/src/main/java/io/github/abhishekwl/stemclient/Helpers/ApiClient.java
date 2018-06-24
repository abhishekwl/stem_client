package io.github.abhishekwl.stemclient.Helpers;

import android.content.Context;

import io.github.abhishekwl.stemclient.R;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {

    private static Retrofit retrofit;

    public static Retrofit getClient(Context context) {
        if(retrofit==null) retrofit = new Retrofit.Builder().baseUrl(context.getString(R.string.BASE_SERVER)).addConverterFactory(GsonConverterFactory.create()).build();
        return retrofit;
    }

}
