package it.unimib.aiprint.repository.interfaces.user;

import androidx.lifecycle.MutableLiveData;

import java.util.Set;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.User;

public interface IUserRepository {
    MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered);
    MutableLiveData<Result> getGoogleUser(String idToken);
    MutableLiveData<Result> getUserFavoritePost(String idToken);
    MutableLiveData<Result> getUserPreferences(String idToken);
    MutableLiveData<Result> logout();
    User getLoggedUser();
    void signUp(String email, String password);
    void signIn(String email, String password);
    void signInWithGoogle(String token);
    void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken);
}
