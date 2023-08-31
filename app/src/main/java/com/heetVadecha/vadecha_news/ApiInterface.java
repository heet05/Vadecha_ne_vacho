package com.heetVadecha.vadecha_news;


import com.heetVadecha.vadecha_news.Model.Headlines;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {


    @GET("top-headlines")
    Call<Headlines> getHeadlines(

            @Query("country") String  country,
            @Query("apiKey") String apiKey,
            @Query("pageSize") String pageSize
    );

    @GET("top-headlines")
    Call<Headlines> getSpecificData(
            @Query("q") String query,
            @Query("apiKey") String apiKey,
            @Query("pageSize") String pageSize

    );



}

