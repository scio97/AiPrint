package it.unimib.aiprint.repository.interfaces.shop;

import androidx.lifecycle.MutableLiveData;

import it.unimib.aiprint.model.Result;

public interface IShopRepositoryWithLiveData {
    MutableLiveData<Result> getShopImage(String imageDescription);
}
