package it.unimib.aiprint.source.interfaces;

import it.unimib.aiprint.model.image.ImageApiResponse;

public interface IImageCallback {
    void onSuccessFromRemote(ImageApiResponse imageApiResponse);
    void onFailureFromRemote(Exception exception);
}
