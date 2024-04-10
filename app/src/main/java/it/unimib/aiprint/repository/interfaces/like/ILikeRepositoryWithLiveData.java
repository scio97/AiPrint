package it.unimib.aiprint.repository.interfaces.like;

import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.Like.Like;
import it.unimib.aiprint.model.Result;

public interface ILikeRepositoryWithLiveData {
    MutableLiveData<Result> getAllLikesByUserId(String id);
    MutableLiveData<Result> getLikeByPost(String id);
    MutableLiveData<Result> saveLike(Like like);
    MutableLiveData<Result> deleteLike(String postId);

}
