package it.unimib.aiprint.repository.interfaces.image;

import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.Result;

public interface IImageRepositoryWithLiveData {
    MutableLiveData<Result> getAiImageUrl(String imageDescription);
}
