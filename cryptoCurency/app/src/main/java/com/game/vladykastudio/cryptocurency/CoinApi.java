package com.game.vladykastudio.cryptocurency;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CoinApi {
    @GET("v1/cryptocurrency/listings/latest")
    Call<CoinResponse> getCoinListResponse(@Query("CMC_PRO_API_KEY") String api_key,
                                           @Query("start") Integer start,
                                           @Query("limit") Integer limit);
}
