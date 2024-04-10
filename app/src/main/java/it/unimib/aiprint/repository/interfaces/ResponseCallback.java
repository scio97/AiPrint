package it.unimib.aiprint.repository.interfaces;

import it.unimib.aiprint.model.image.Image;

public interface ResponseCallback {
    void onSuccess(String imageUrl);
    void onFailure(String errorMessage);
    void onNewsFavoriteStatusChanged(Image news);
}
