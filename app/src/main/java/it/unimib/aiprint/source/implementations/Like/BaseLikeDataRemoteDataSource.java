package it.unimib.aiprint.source.implementations.Like;

import it.unimib.aiprint.model.Like.Like;
import it.unimib.aiprint.repository.interfaces.like.LikeResponseCallback;

public abstract class BaseLikeDataRemoteDataSource {
    protected LikeResponseCallback likeResponseCallback;

    public void setLikeResponseCallback(LikeResponseCallback likeResponseCallback) {
        this.likeResponseCallback = likeResponseCallback;
    }

    public abstract void saveLikeData(Like like);

    public abstract void getPostLike(String id);

    public abstract void deleteLikeData(String postId);
    public abstract void getAllLikesByUserId(String userId);
  //  public abstract void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken);
}
