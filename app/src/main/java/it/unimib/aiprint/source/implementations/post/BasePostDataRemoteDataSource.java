package it.unimib.aiprint.source.implementations.post;

import java.util.ArrayList;

import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.repository.implementations.post.PostResponseCallback;

public abstract class BasePostDataRemoteDataSource {
    protected PostResponseCallback postResponseCallback;

    public void setPostResponseCallback(PostResponseCallback postResponseCallback) {
        this.postResponseCallback = postResponseCallback;
    }

    public abstract void savePostData(Post user);
    public abstract void getLatestRandomPost(String email);
    public abstract void getPostById(String id);
    public abstract void getAllPostByIds(ArrayList<String> list);
    public abstract void deleteAll();
    public abstract void addOneLike(String idPost);
    public abstract void removeOneLike(String idPost);
}
