package it.unimib.aiprint.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.repository.interfaces.shop.IShopRepositoryWithLiveData;

public class ShopViewModel extends ViewModel {

    private MutableLiveData<Result> aiImageGeneratedLiveData;
    private final IShopRepositoryWithLiveData imageRepositoryWithLiveData;

    public ShopViewModel(IShopRepositoryWithLiveData imageRepositoryWithLiveData) {
        this.imageRepositoryWithLiveData = imageRepositoryWithLiveData;
    }

    public MutableLiveData<Result> getShopImage(String imageUrl) {
        if(aiImageGeneratedLiveData == null){
            aiImageGeneratedLiveData = imageRepositoryWithLiveData.getShopImage(imageUrl);
        }
        return aiImageGeneratedLiveData;
    }
}
