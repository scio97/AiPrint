package it.unimib.aiprint.repository.implementations.shop;

import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.shop.ShopApiResponse;
import it.unimib.aiprint.repository.interfaces.shop.IShopRepositoryWithLiveData;
import it.unimib.aiprint.source.implementations.shop.BaseShopRemoteDataSource;
import it.unimib.aiprint.source.interfaces.IShopCallback;

public class ShopRepositoryWithLiveData implements IShopRepositoryWithLiveData, IShopCallback {
    private final BaseShopRemoteDataSource imageRemoteDataSource;
    private MutableLiveData<Result> aiImageLiveData;

    public ShopRepositoryWithLiveData(BaseShopRemoteDataSource imageRemoteDataSource) {
        this.imageRemoteDataSource = imageRemoteDataSource;
        this.imageRemoteDataSource.setImageCallback(this);
        this.aiImageLiveData = new MutableLiveData<>();
    }

    @Override
    public void onSuccessFromRemote(ShopApiResponse shopApiResponse) {
        //imageRemoteDataSource.getAiImageUrl(imageApiResponse.getOutput_url());
        Result.Success result = new Result.Success(shopApiResponse);
        aiImageLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }

    public MutableLiveData<Result> getShopImage(String imageDescription) {
        imageRemoteDataSource.getShopImage(imageDescription);
        return aiImageLiveData;
    }
}
