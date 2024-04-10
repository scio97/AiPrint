package it.unimib.aiprint.repository.implementations.post;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.model.post.PostResponse;
import it.unimib.aiprint.source.implementations.post.BasePostDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.post.BasePostLocalDataSource;

public class PostRepositoryWithLiveData implements IPostRepositoryWithLiveData, PostResponseCallback{
    private final MutableLiveData<Result> favoriteNewsMutableLiveData;
    private final BasePostDataRemoteDataSource postRemoteDataSource;
    private final MutableLiveData<Result> postMutableLiveData;
    private final MutableLiveData<Result> test;
    private final BasePostLocalDataSource postLocalDataSource;

    public PostRepositoryWithLiveData(BasePostDataRemoteDataSource postRemoteDataSource, BasePostLocalDataSource postLocalDataSource) {
        this.postRemoteDataSource = postRemoteDataSource;
        this.favoriteNewsMutableLiveData = new MutableLiveData<>();
        this.postMutableLiveData = new MutableLiveData<>();
        this.test = new MutableLiveData<>();
        this.postRemoteDataSource.setPostResponseCallback(this);
        this.postLocalDataSource = postLocalDataSource;

    }

    @Override
    public MutableLiveData<Result> getPostById(String id) {
        postRemoteDataSource.getPostById(id);
        return favoriteNewsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getAllPostByIds(ArrayList<String> list) {
        postRemoteDataSource.getAllPostByIds(list);
        return test;
    }

    @Override
    public MutableLiveData<Result> savePost(Post post) {
        postRemoteDataSource.savePostData(post);
        return postMutableLiveData;
    }

    @Override
    public void onSuccessFromRemoteDatabaseSinglePost(PostResponse post) {
        Result.Success result = new Result.Success<PostResponse>(post);
        favoriteNewsMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Post> postList) {
        favoriteNewsMutableLiveData.postValue(new Result.Success<PostResponse>(new PostResponse(postList)));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
    }

    @Override
    public MutableLiveData<Result> getLatestRandomPost(String email) {
        postRemoteDataSource.getLatestRandomPost(email);
        return favoriteNewsMutableLiveData;
    }

    @Override
    public void onSuccessFromRemoteDatabaseFavLists(List<Post> postList) {
        List<Post> e = new ArrayList<>();
        e.addAll(postList);
        test.postValue(new Result.Success<PostResponse>(new PostResponse(e)));
    }

    @Override
    public void onSuccessFromLocalDatabase(List<Post> postList) {

    }

    @Override
    public void onSuccessDeletion() {

    }

    public MutableLiveData<Result> savePostInLocal(Post post){
        postLocalDataSource.savePost(post);
        return postMutableLiveData;
    }

    public MutableLiveData<Result> removePostInLocal(Post post){
        postLocalDataSource.deletePost(post);
        return postMutableLiveData;
    }

    public MutableLiveData<Result> isSavedPostInLocal(String id){
        postLocalDataSource.isSavedPost(id);
        return postMutableLiveData;
    }

    public MutableLiveData<Result> removeAllPostInLocal(){
        postLocalDataSource.deleteAll();
        return postMutableLiveData;
    }

    public MutableLiveData<Result> getAllPostInLocal(){
        postLocalDataSource.getAllPost();
        return postMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> addOneLike(String idPost) {
        postRemoteDataSource.addOneLike(idPost);
        return postMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> removeOneLike(String idPost) {
        postRemoteDataSource.removeOneLike(idPost);
        return postMutableLiveData;
    }
}
