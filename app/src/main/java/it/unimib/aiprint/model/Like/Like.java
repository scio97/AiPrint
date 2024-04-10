package it.unimib.aiprint.model.Like;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

public class Like  { //implements Parcelable

    @PrimaryKey(autoGenerate = true)
    private long id;
    private String post_id;
    private String user_id;
    private String idToken;


    /*protected Like(Parcel in) {
        id = in.readLong();
        post_id = in.readString();
        user_id = in.readString();
    }

    public static final Creator<Like> CREATOR = new Creator<Like>() {
        @Override
        public Like createFromParcel(Parcel in) {
            return new Like(in);
        }

        @Override
        public Like[] newArray(int size) {
            return new Like[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeLong(id);
        parcel.writeString(post_id);
        parcel.writeString(user_id);
    } */

    public Like() {
    }

    public Like(long id, String post_id, String user_id) {
        this.user_id = user_id;
        this.post_id = post_id;
    }


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPost_id() {
        return post_id;
    }

    public void setPost_id(String post_id) {
        this.post_id = post_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    // This annotation is used to exclude the field from the database (firebase)
    @Exclude
    public String getIdToken() {
        return idToken;
    }

    public void setIdToken(String idToken) {
        this.idToken = idToken;
    }

}
