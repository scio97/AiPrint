package it.unimib.aiprint.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.repository.implementations.post.IPostRepositoryWithLiveData;

public class PostViewModel extends ViewModel {
    private MutableLiveData<Result> randomPostListLiveData;
    private MutableLiveData<Result> favoritePostListLiveData;
    private MutableLiveData<Result> savePostResult;
    private final IPostRepositoryWithLiveData postRepositoryWithLiveData;
    private MutableLiveData<Result> savedPostInLocal;

    public PostViewModel(IPostRepositoryWithLiveData iNewsRepositoryWithLiveData) {
        this.postRepositoryWithLiveData = iNewsRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getLatestRandomPost(String email) {
        randomPostListLiveData = postRepositoryWithLiveData.getLatestRandomPost(email);
        return randomPostListLiveData;
    }

    public MutableLiveData<Result> getPostById(String id) {
        favoritePostListLiveData = postRepositoryWithLiveData.getPostById(id);
        return favoritePostListLiveData;
    }

    public MutableLiveData<Result> getAllPostByIds(ArrayList<String> list) {
        favoritePostListLiveData = postRepositoryWithLiveData.getAllPostByIds(list);
        return favoritePostListLiveData;
    }

    public MutableLiveData<Result> savePost(Post post) {
        if(savePostResult == null){
            savePostResult = postRepositoryWithLiveData.savePost(post);
        }
        return savePostResult;
    }

    public MutableLiveData<Result> savePostInLocal(Post post) {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.savePostInLocal(post);
        }
        return savedPostInLocal;
    }

    public MutableLiveData<Result> getAllPostInLocal() {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.getAllPostInLocal();
        }
        return savedPostInLocal;
    }

    public MutableLiveData<Result> addLike(String idPost) {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.addOneLike(idPost);
        }else {
            savedPostInLocal = postRepositoryWithLiveData.addOneLike(idPost);
        }
        return savedPostInLocal;
    }

    public MutableLiveData<Result> removeLike(String idPost) {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.removeOneLike(idPost);
        } else {
            savedPostInLocal = postRepositoryWithLiveData.removeOneLike(idPost);
        }
        return savedPostInLocal;
    }

    public MutableLiveData<Result> isSavedPostInLocal(String id) {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.isSavedPostInLocal(id);
        }else {
            savedPostInLocal = postRepositoryWithLiveData.isSavedPostInLocal(id);
        }
        return savedPostInLocal;
    }

    public MutableLiveData<Result> removeAllPostInLocal() {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.removeAllPostInLocal();
        }else {
            savedPostInLocal = postRepositoryWithLiveData.removeAllPostInLocal();
        }
        return savedPostInLocal;
    }

    public MutableLiveData<Result> removePostInLocal(Post post) {
        if(savedPostInLocal == null){
            savedPostInLocal = postRepositoryWithLiveData.removePostInLocal(post);
        }else {
            savedPostInLocal = postRepositoryWithLiveData.removePostInLocal(post);
        }
        return savedPostInLocal;
    }

}
