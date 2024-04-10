package it.unimib.aiprint.repository.implementations.post;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.post.Post;

public interface IPostRepositoryWithLiveData {
    MutableLiveData<Result> getPostById(String id);
    MutableLiveData<Result> savePost(Post post);
    MutableLiveData<Result> getLatestRandomPost(String email);
    MutableLiveData<Result> savePostInLocal(Post post);
    MutableLiveData<Result> removePostInLocal(Post post);
    MutableLiveData<Result> isSavedPostInLocal(String id);
    MutableLiveData<Result> removeAllPostInLocal();
    MutableLiveData<Result> getAllPostInLocal();
    MutableLiveData<Result> addOneLike(String idPost);
    MutableLiveData<Result> removeOneLike(String idPost);
    MutableLiveData<Result> getAllPostByIds(ArrayList<String> list);
}
