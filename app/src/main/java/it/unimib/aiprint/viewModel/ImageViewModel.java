package it.unimib.aiprint.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.repository.interfaces.image.IImageRepositoryWithLiveData;

public class ImageViewModel extends ViewModel {
    private MutableLiveData<Result> aiImageGeneratedLiveData;
    private final IImageRepositoryWithLiveData imageRepositoryWithLiveData;

    public ImageViewModel(IImageRepositoryWithLiveData imageRepositoryWithLiveData) {
        this.imageRepositoryWithLiveData = imageRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getAiImage(String imageDescription) {
        if(aiImageGeneratedLiveData == null){
            aiImageGeneratedLiveData = imageRepositoryWithLiveData.getAiImageUrl(imageDescription);
        }
        return aiImageGeneratedLiveData;
    }
}
