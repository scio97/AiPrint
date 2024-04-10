package it.unimib.aiprint.model.Like;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.List;

public class LikeResponse {

    private List<Like> likeList;

    public LikeResponse(List<Like> likeList) {
        this.likeList = likeList;
    }

    public List<Like> getLikeList() {
        return likeList;
    }

    public void setLikeList(List<Like> likeList) {
        this.likeList = likeList;
    }
}