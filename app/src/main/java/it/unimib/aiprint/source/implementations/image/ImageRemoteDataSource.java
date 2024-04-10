package it.unimib.aiprint.source.implementations.image;

import androidx.annotation.NonNull;

import it.unimib.aiprint.model.image.ImageApiResponse;
import it.unimib.aiprint.service.ImageApiService;
import it.unimib.aiprint.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ImageRemoteDataSource extends BaseImageRemoteDataSource {
    private final ImageApiService imageApiService;
    private final String apiKey;

    public ImageRemoteDataSource(String apiKey) {
        this.imageApiService = ServiceLocator.getInstance().getImageApiService();
        this.apiKey = apiKey;
    }

    @Override
    public void getAiImageUrl(String imageDescription) {
        Call<ImageApiResponse> imageResponseCall = imageApiService.getAiImage(imageDescription, apiKey);
        imageResponseCall.enqueue(new Callback<ImageApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ImageApiResponse> call, @NonNull Response<ImageApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    imageCallback.onSuccessFromRemote(response.body());
                } else {
                    imageCallback.onFailureFromRemote(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ImageApiResponse> call, @NonNull Throwable t) {
                imageCallback.onFailureFromRemote(new Exception());
            }
        });
    }
}
