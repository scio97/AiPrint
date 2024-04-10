package it.unimib.aiprint.viewModel;


import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.aiprint.model.Like.Like;
import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.repository.interfaces.like.ILikeRepositoryWithLiveData;

public class LikeViewModel extends ViewModel {
    private MutableLiveData<Result> likeListLiveData;
    private MutableLiveData<Result> saveLikeResult;
    private final ILikeRepositoryWithLiveData likeRepositoryWithLiveData;

    public LikeViewModel(ILikeRepositoryWithLiveData iLikeRepositoryWithLiveData) {
        this.likeRepositoryWithLiveData = iLikeRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getAllLikesByUserId(String id) {
        likeListLiveData = likeRepositoryWithLiveData.getAllLikesByUserId(id);
        return likeListLiveData;
    }

    public MutableLiveData<Result> getLikeByPost(String id) {
        if(likeListLiveData == null){
            likeListLiveData = likeRepositoryWithLiveData.getLikeByPost(id);
        }
        return likeListLiveData;
    }


    public MutableLiveData<Result> saveLike(Like like) {
        if(saveLikeResult == null){
            saveLikeResult = likeRepositoryWithLiveData.saveLike(like);
        } else {
            // we have to save like also if we have already a result
            saveLikeResult = likeRepositoryWithLiveData.saveLike(like);
        }
        return saveLikeResult;
    }

    public MutableLiveData<Result> deleteLike(String postId) {
        if(saveLikeResult == null){
            saveLikeResult = likeRepositoryWithLiveData.deleteLike(postId);
        } else {
            // we have to save like also if we have already a result
            saveLikeResult = likeRepositoryWithLiveData.deleteLike(postId);
        }
        return saveLikeResult;
    }

}

