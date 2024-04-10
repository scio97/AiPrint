package it.unimib.aiprint.source.implementations.post;

import static it.unimib.aiprint.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.unimib.aiprint.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;

import java.util.List;

import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.repository.implementations.post.PostDao;
import it.unimib.aiprint.repository.implementations.post.PostRoomDatabase;
import it.unimib.aiprint.util.DataEncryptionUtil;

public class PostLocalDataSource extends BasePostLocalDataSource {
    private final PostDao postDao;
    private final DataEncryptionUtil dataEncryptionUtil;


    public PostLocalDataSource(PostRoomDatabase postRoomDatabase, DataEncryptionUtil dataEncryptionUtil) {
        this.postDao = postRoomDatabase.postDao();
        this.dataEncryptionUtil = dataEncryptionUtil;
    }

    @Override
    public void getAllPost() {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            List<Post> postList = postDao.getAllPost();
            if(postList != null && postList.size() > 0){
                postResponseCallback.onSuccessFromLocalDatabase(postList);
            }else{
                postResponseCallback.onFailureFromRemoteDatabase("No post found");
            }
        });
    }

    @Override
    public void savePost(Post post) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.insertAll(post);
        });
    }

    @Override
    public void deletePost(Post post) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            postDao.delete(post);
        });
    }

    @Override
    public void deleteAll() {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            int newsCounter = postDao.getAllPost().size();
            int newsDeletedNews = postDao.deleteAll();

            // It means that everything has been deleted
            if (newsCounter == newsDeletedNews) {
                dataEncryptionUtil.deleteAll(ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ENCRYPTED_DATA_FILE_NAME);
                postResponseCallback.onSuccessDeletion();
            }
        });
    }

    @Override
    public void isSavedPost(String id) {
        PostRoomDatabase.databaseWriteExecutor.execute(() -> {
            Post post = postDao.getPost(id);
            if(post != null){
                postResponseCallback.onSuccessFromLocalDatabase((List<Post>) post);
            }else{
                postResponseCallback.onFailureFromRemoteDatabase("No post found");
            }
        });
    }

}
