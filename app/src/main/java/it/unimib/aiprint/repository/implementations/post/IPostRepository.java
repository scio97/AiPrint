package it.unimib.aiprint.repository.implementations.post;

import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.post.Post;

public interface IPostRepository {
    void getPost(String id);
    // Get home post, random without id
    void getPost();
    void savePost(Post post);

}
