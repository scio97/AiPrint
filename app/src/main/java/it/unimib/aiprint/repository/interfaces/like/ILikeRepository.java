package it.unimib.aiprint.repository.interfaces.like;

import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.Result;

public interface ILikeRepository {
    MutableLiveData<Result> getUserFavouritePost(String userId);
}
