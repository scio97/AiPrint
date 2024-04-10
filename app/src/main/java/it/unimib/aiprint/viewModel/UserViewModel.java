package it.unimib.aiprint.viewModel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.User;
import it.unimib.aiprint.repository.interfaces.user.IUserRepository;

public class UserViewModel extends ViewModel {
    private final IUserRepository userRepository;
    private MutableLiveData<Result> userMutableLiveData;
    private MutableLiveData<Result> userFavoriteNewsMutableLiveData;
    private boolean authenticationError;

    public UserViewModel(IUserRepository userRepository) {
        this.userRepository = userRepository;
        authenticationError = false;
    }

    public MutableLiveData<Result> getUserMutableLiveData(
            String email, String password, boolean isUserRegistered) {
        if (userMutableLiveData == null) {
            getUserData(email, password, isUserRegistered);
        }
        return userMutableLiveData;
    }

    private void getUserData(String email, String password, boolean isUserRegistered) {
        userMutableLiveData = userRepository.getUser(email, password, isUserRegistered);
    }

    private void getUserData(String token) {
        userMutableLiveData = userRepository.getGoogleUser(token);
    }

    public boolean isAuthenticationError() {
        return authenticationError;
    }

    public void setAuthenticationError(boolean authenticationError) {
        this.authenticationError = authenticationError;
    }

    public void getUser(String email, String password, boolean isUserRegistered) {
        userRepository.getUser(email, password, isUserRegistered);
    }
    public MutableLiveData<Result> getUserFavoriteNewsMutableLiveData(String idToken) {
        if (userFavoriteNewsMutableLiveData == null) {
            getUserFavoriteNews(idToken);
        }
        return userFavoriteNewsMutableLiveData;
    }
    private void getUserFavoriteNews(String idToken) {
        userFavoriteNewsMutableLiveData = userRepository.getUserFavoritePost(idToken);
    }


    public User getLoggedUser() {
        return userRepository.getLoggedUser();
    }
}
