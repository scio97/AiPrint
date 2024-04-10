package it.unimib.aiprint.repository.interfaces.like;

import java.util.List;

import it.unimib.aiprint.model.Like.Like;

public interface LikeResponseCallback {
    void onSuccessFromRemoteDatabase(Like like);
    void onSuccessFromRemoteDatabase(List<Like> likeList);
    void onSuccessRetrieveLikeList(List<Like> likeList);
    void onFailureFromRemoteDatabase(String message);
}
