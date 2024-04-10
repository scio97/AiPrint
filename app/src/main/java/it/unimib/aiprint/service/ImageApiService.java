package it.unimib.aiprint.service;

import it.unimib.aiprint.model.image.ImageApiResponse;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ImageApiService {
    @FormUrlEncoded
    @POST("stable-diffusion")
    Call<ImageApiResponse> getAiImage(
            @Field("text") String text,
            //@Query(TOP_HEADLINES_PAGE_SIZE_PARAMETER) int pageSize,
            //@Query(TOP_HEADLINES_PAGE_PARAMETER) int page,
            @Header("api-key") String apiKey
    );
}
