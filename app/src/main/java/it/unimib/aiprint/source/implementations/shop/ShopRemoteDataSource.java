package it.unimib.aiprint.source.implementations.shop;

import androidx.annotation.NonNull;

import it.unimib.aiprint.model.shop.ShopApiResponse;
import it.unimib.aiprint.service.ShopApiService;
import it.unimib.aiprint.util.ServiceLocator;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShopRemoteDataSource extends BaseShopRemoteDataSource {
    private final ShopApiService shopApiService;

    public ShopRemoteDataSource() {
        this.shopApiService = ServiceLocator.getInstance().getShopApiService();
    }

    @Override
    public void getShopImage(String imageUrl) {
        Call<ShopApiResponse> imageResponseCall = shopApiService.getShopImage(imageUrl);
        imageResponseCall.enqueue(new Callback<ShopApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ShopApiResponse> call, @NonNull Response<ShopApiResponse> response) {
                if (response.body() != null && response.isSuccessful()) {
                    imageCallback.onSuccessFromRemote(response.body());
                } else {
                    imageCallback.onFailureFromRemote(new Exception());
                }
            }

            @Override
            public void onFailure(@NonNull Call<ShopApiResponse> call, @NonNull Throwable t) {
                imageCallback.onFailureFromRemote(new Exception());
            }
        });
    }
}