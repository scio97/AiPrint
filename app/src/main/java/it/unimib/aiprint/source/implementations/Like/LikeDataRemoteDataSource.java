package it.unimib.aiprint.source.implementations.Like;

import static it.unimib.aiprint.util.Constants.FIREBASE_REALTIME_DATABASE;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import it.unimib.aiprint.model.Like.Like;

public class LikeDataRemoteDataSource extends BaseLikeDataRemoteDataSource {
    private final DatabaseReference databaseReference;

    public LikeDataRemoteDataSource() {
        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance(FIREBASE_REALTIME_DATABASE);
        databaseReference = firebaseDatabase.getReference().getRef();
    }


    @Override
    public void saveLikeData(Like like) {
        like.setIdToken(databaseReference.child("Like").push().getKey());
        databaseReference.child("Like").child(like.getIdToken()).setValue(like)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        likeResponseCallback.onSuccessFromRemoteDatabase(like);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        likeResponseCallback.onFailureFromRemoteDatabase(e.getLocalizedMessage());
                    }
                });
    }

    @Override
    public void getAllLikesByUserId(String userId) {
        databaseReference.child("Like").orderByChild("user_id").equalTo(userId)
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Like> likeList = new ArrayList<>();

                for(DataSnapshot ds : snapshot.getChildren()) {
                    Like like = ds.getValue(Like.class);
                    likeList.add(like);
                }
                likeResponseCallback.onSuccessRetrieveLikeList(likeList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                likeResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
            }
        });
    }

    @Override
    public void getPostLike(String id) {
        databaseReference.child("Like").orderByChild("post_id").equalTo(id)
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        List<Like> likeList = new ArrayList<>();

                        for(DataSnapshot ds : snapshot.getChildren()) {
                            Like like = ds.getValue(Like.class);
                            likeList.add(like);
                        }
                        likeResponseCallback.onSuccessFromRemoteDatabase(likeList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        likeResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
                    }
                });
    }

    @Override
    public void deleteLikeData(String postId) {
        databaseReference.child("Like").orderByChild("post_id").equalTo(postId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot ds : snapshot.getChildren()) {
                            ds.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        likeResponseCallback.onFailureFromRemoteDatabase(error.getMessage());
                    }
                });
    }
}
