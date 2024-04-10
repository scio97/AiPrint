package it.unimib.aiprint.source.implementations.image;

import it.unimib.aiprint.source.interfaces.IImageCallback;

public abstract class BaseImageRemoteDataSource {
    protected IImageCallback imageCallback;

    public void setImageCallback(IImageCallback imageCallback) {
        this.imageCallback = imageCallback;
    }

    public abstract void getAiImageUrl(String imageDescription);
}
