package it.unimib.aiprint.source.implementations.post;

import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.repository.implementations.post.PostResponseCallback;

public abstract class BasePostLocalDataSource {
    protected PostResponseCallback postResponseCallback;

    public void setPostResponseCallback(PostResponseCallback postResponseCallback) {
        this.postResponseCallback = postResponseCallback;
    }

    public abstract void getAllPost();
    // Get home post, random without id
    //public abstract void getPost();
    public abstract void savePost(Post post);
    public abstract void deletePost(Post post);
    public abstract void deleteAll();
    public abstract void isSavedPost(String id);
}
