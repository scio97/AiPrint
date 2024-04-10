package it.unimib.aiprint.repository.implementations.post;

import android.app.Application;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.util.ServiceLocator;

public class PostRepository {
    private final PostResponseCallback postResponseCallback;
    private final MutableLiveData<Result> userFavoritePostMutableLiveData;
    private final PostDao postDao;
    private final Application application;

    public PostRepository(Application application, PostResponseCallback postResponseCallback) {
        this.application = application;
        this.userFavoritePostMutableLiveData = new MutableLiveData<>();
        PostRoomDatabase roomDatabase = ServiceLocator.getInstance().getPostsDao(application);
        this.postDao = roomDatabase.postDao();
        this.postResponseCallback = postResponseCallback;
    }

    public void saveDataPost(Post post){
        return;
    }

    public void getAllPost(){
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> postList = postDao.getAllPost();
            if(postList != null && postList.size() > 0){
                postResponseCallback.onSuccessFromRemoteDatabase(postList);
            }else{
                postResponseCallback.onFailureFromRemoteDatabase("No post found");
            }
        });
    }
}
