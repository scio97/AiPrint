package it.unimib.aiprint.repository.implementations.post;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import it.unimib.aiprint.model.post.Post;
@Dao
public interface PostDao {
    @Query("SELECT * FROM post ORDER BY date DESC")
    List<Post> getAllPost();

    @Query("SELECT * FROM post WHERE id = :id")
    Post getPost(String id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Post> posts);

    @Insert
    void insertAll(Post... post);

    @Delete
    void delete(Post post);

    @Query("DELETE FROM post")
    int deleteAll();
}
