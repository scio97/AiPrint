package it.unimib.aiprint.source.interfaces;

import it.unimib.aiprint.model.shop.ShopApiResponse;

public interface IShopCallback {
    void onSuccessFromRemote(ShopApiResponse shopApiResponse);
    void onFailureFromRemote(Exception exception);
}
