package it.unimib.aiprint.source.implementations.shop;

import it.unimib.aiprint.source.interfaces.IShopCallback;

public abstract class BaseShopRemoteDataSource {
    protected IShopCallback imageCallback;

    public void setImageCallback(IShopCallback imageCallback) {
        this.imageCallback = imageCallback;
    }

    public abstract void getShopImage(String imageDescription);
}
