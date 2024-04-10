package it.unimib.aiprint.repository.implementations.Like;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.aiprint.model.Like.Like;
import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.repository.interfaces.like.ILikeRepositoryWithLiveData;
import it.unimib.aiprint.repository.interfaces.like.LikeResponseCallback;
import it.unimib.aiprint.source.implementations.Like.BaseLikeDataRemoteDataSource;

public class LikeRepositoryWithLiveData implements ILikeRepositoryWithLiveData, LikeResponseCallback {
    private final MutableLiveData<Result> likeMutableLiveData;
    private final MutableLiveData<Result> likeListLiveData;
    private final BaseLikeDataRemoteDataSource likeRemoteDataSource;


    public LikeRepositoryWithLiveData(BaseLikeDataRemoteDataSource likeRemoteDataSource) {
        this.likeRemoteDataSource = likeRemoteDataSource;
        this.likeMutableLiveData = new MutableLiveData<>();
        this.likeListLiveData = new MutableLiveData<>();
        this.likeRemoteDataSource.setLikeResponseCallback(this);
    }

    @Override
    public MutableLiveData<Result> getAllLikesByUserId(String id) {
        likeRemoteDataSource.getAllLikesByUserId(id);
        return likeListLiveData;
    }

    @Override
    public MutableLiveData<Result> getLikeByPost(String id) {
        likeRemoteDataSource.getPostLike(id);
        return likeMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> saveLike(Like like) {
        likeRemoteDataSource.saveLikeData(like);
        return likeMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> deleteLike(String postId) {
        likeRemoteDataSource.deleteLikeData(postId);
        return likeMutableLiveData;
    }

    @Override
    public void onSuccessFromRemoteDatabase(Like like) {
        likeMutableLiveData.postValue(new Result.Success<Like>(like));
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Like> likeList) {
        likeMutableLiveData.postValue(new Result.Success<List<Like>>(likeList));
    }

    @Override
    public void onSuccessRetrieveLikeList(List<Like> likeList) {
        likeListLiveData.postValue(new Result.Success<List<Like>>(likeList));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        // TODO
    }
}
