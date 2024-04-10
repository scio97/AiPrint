package it.unimib.aiprint.repository.implementations.image;


import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.image.ImageApiResponse;
import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.repository.interfaces.image.IImageRepositoryWithLiveData;
import it.unimib.aiprint.source.implementations.image.BaseImageRemoteDataSource;
import it.unimib.aiprint.source.interfaces.IImageCallback;

public class ImageRepositoryWithLiveData implements IImageRepositoryWithLiveData, IImageCallback {
    private final BaseImageRemoteDataSource imageRemoteDataSource;
    private MutableLiveData<Result> aiImageLiveData;

    public ImageRepositoryWithLiveData(BaseImageRemoteDataSource imageRemoteDataSource) {
        this.imageRemoteDataSource = imageRemoteDataSource;
        this.imageRemoteDataSource.setImageCallback(this);
        this.aiImageLiveData = new MutableLiveData<>();
    }

    @Override
    public void onSuccessFromRemote(ImageApiResponse imageApiResponse) {
        //imageRemoteDataSource.getAiImageUrl(imageApiResponse.getOutput_url());
        Result.Success result = new Result.Success(imageApiResponse);
        aiImageLiveData.postValue(result);
    }

    @Override
    public void onFailureFromRemote(Exception exception) {

    }

    public MutableLiveData<Result> getAiImageUrl(String imageDescription) {
        imageRemoteDataSource.getAiImageUrl(imageDescription);
        return aiImageLiveData;
    }
}
