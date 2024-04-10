package it.unimib.aiprint.service;

import it.unimib.aiprint.model.shop.ShopApiResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ShopApiService {
    @FormUrlEncoded
    @POST("https://teemill.com/omnis/v3/product/create/")
    @Headers("Authorization: Bearer P3sbXrqgozFxB1SwZaFbCYwiKIL7Jy6g8rDcHRUj")
    Call<ShopApiResponse> getShopImage(@Field("image_url") String image_url);
}
