package it.unimib.aiprint.source.implementations.post;

import static it.unimib.aiprint.util.Constants.FIREBASE_REALTIME_DATABASE;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import it.unimib.aiprint.model.Like.Like;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.model.post.PostResponse;

public class PostDataRemoteDataSource extends BasePostDataRemoteDataSource {
    private final DatabaseReference databaseReference;
    private List<Post> favouriteList = new ArrayList<>();
    int i = 1;

    public PostDataRemoteDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
    }

    @Override
    public void savePostData(Post post) {
        databaseReference.child("Post").child(post.getId()).setValue(post)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        postResponseCallback.onSuccessFromRemoteDatabaseSinglePost(new PostResponse(post));
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        postResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                    }
                });
        }

    @Override
    public void getPostById(String postId) {
        databaseReference.child("Post").child(postId)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        postResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
                    }
                    else {
                        Post p = task.getResult().getValue(Post.class);
                        postResponseCallback.onSuccessFromRemoteDatabaseSinglePost(new PostResponse(p));
                    }
                });
    }

    @Override
    public void getAllPostByIds(ArrayList<String> list) {
        i=1;
        favouriteList.clear();
        for(String element : list) {
            databaseReference.child("Post").child(element)
                    .get().addOnCompleteListener(task -> {
                        if (!task.isSuccessful()) {
                            postResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
                        }
                        else {
                            Post p = task.getResult().getValue(Post.class);
                            pushFavDatas(list.size(), i++, p);
                        }
                    });
        }
    }

    private void pushFavDatas(int indexTotal, int currentIndex, Post post){
        favouriteList.add(post);
        if(indexTotal == currentIndex){
            postResponseCallback.onSuccessFromRemoteDatabaseFavLists(favouriteList);
        }
    }


    @Override
    public void addOneLike(String idPost) {
        databaseReference.child("Post").child(idPost).child("like_count").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                postResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
            }
            else {
                int likes = Integer.parseInt(task.getResult().getValue().toString());
                databaseReference.child("Post").child(idPost).child("like_count").setValue(likes + 1);
            }
        });
    }

    @Override
    public void removeOneLike(String idPost) {
        databaseReference.child("Post").child(idPost).child("like_count").get().addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                postResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
            }
            else {
                int likes = Integer.parseInt(task.getResult().getValue().toString());
                databaseReference.child("Post").child(idPost).child("like_count").setValue(likes - 1);
            }
        });
    }

    @Override
    public void deleteAll() {

    }

    @Override
    public void getLatestRandomPost(String email) {
        // Get random 20 post
        databaseReference.child("Post").orderByChild("date").limitToFirst(20)
                .get().addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        postResponseCallback.onFailureFromRemoteDatabase(task.getException().getLocalizedMessage());
                    }
                    else {
                        List<Like> likeList = new ArrayList<>();
                        databaseReference.child("Like").orderByChild("user_id").equalTo(email).get()
                                .addOnCompleteListener(likeTask -> {
                                        List<Like> postLikeByUser = new ArrayList<>();
                                        List<Post> postList = new ArrayList<>();

                                        for(DataSnapshot ds : task.getResult().getChildren()) {
                                            Post post = ds.getValue(Post.class);
                                            postList.add(post);
                                        }
                                        for(DataSnapshot ds :  likeTask.getResult().getChildren()) {
                                            Like like = ds.getValue(Like.class);
                                            postLikeByUser.add(like);
                                        }

                                        for (Post post: postList) {
                                            for (Like like :
                                                    postLikeByUser) {
                                                if(post.getId().equals(like.getPost_id()))
                                                    post.setIsLiked(true);
                                            }
                                        }

                                        postResponseCallback.onSuccessFromRemoteDatabase(postList);

                                });
                    }
                });
    }



}
