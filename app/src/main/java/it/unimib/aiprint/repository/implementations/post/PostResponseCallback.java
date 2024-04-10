package it.unimib.aiprint.repository.implementations.post;

import java.util.List;

import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.model.post.PostResponse;

public interface PostResponseCallback {
    void onSuccessFromRemoteDatabaseSinglePost(PostResponse post);
    void onSuccessFromRemoteDatabase(List<Post> newsList);
    void onSuccessFromRemoteDatabaseFavLists(List<Post> newsList);
    void onFailureFromRemoteDatabase(String message);

    void onSuccessFromLocalDatabase(List<Post> postList);
    void onSuccessDeletion();
}
