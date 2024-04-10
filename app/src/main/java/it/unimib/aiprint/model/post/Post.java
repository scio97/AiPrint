package it.unimib.aiprint.model.post;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.google.firebase.database.Exclude;

import org.jetbrains.annotations.NotNull;

@Entity
public class Post {
    @PrimaryKey
    @NotNull
    private String id;
    private String description;
    private String aiImageBase64;
    private String shopImageBase64;
    private String shopUrl;
    private double price;
    private String userId;
    private String userName;
    private String userIdImage;
    private String date;
    private int like_count;
    private boolean isLiked;

    public Post(String id, String description, String aiImageBase64, String shopImageBase64,
                String shopUrl, double price, String userId, String userName, String userIdImage, String date, int like) {
        this.id = id;
        this.description = description;
        this.aiImageBase64 = aiImageBase64;
        this.shopImageBase64 = shopImageBase64;
        this.shopUrl = shopUrl;
        this.price = price;
        this.userId = userId;
        this.userName = userName;
        this.userIdImage = userIdImage;
        this.date = date;
        this.like_count = like;
        this.isLiked = false;

    }
    public Post(){}

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAiImageBase64() {
        return aiImageBase64;
    }

    public void setAiImageBase64(String aiImageBase64) {
        this.aiImageBase64 = aiImageBase64;
    }

    public String getShopImageBase64() {
        return shopImageBase64;
    }

    public void setShopImageBase64(String shopImageBase64) {
        this.shopImageBase64 = shopImageBase64;
    }

    public String getShopUrl() {
        return shopUrl;
    }

    public void setShopUrl(String shopUrl) {
        this.shopUrl = shopUrl;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserIdImage() {
        return userIdImage;
    }

    public void setUserIdImage(String userIdImage) {
        this.userIdImage = userIdImage;
    }

    public int getLike_count() {
        return like_count;
    }

    public void setLike_count(int like_count) {
        this.like_count = like_count;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    // This annotation is used to exclude the field from the database (firebase)
    @Exclude
    public boolean getIsLiked() {
        return isLiked;
    }

    public void setIsLiked(boolean isLiked) {
        this.isLiked = isLiked;
    }
}
