package it.unimib.aiprint.repository.implementations.user;

import androidx.lifecycle.MutableLiveData;

import java.util.List;
import java.util.Set;

import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.User;
import it.unimib.aiprint.model.post.Post;
import it.unimib.aiprint.model.post.PostResponse;
import it.unimib.aiprint.repository.UserResponseCallback;
import it.unimib.aiprint.repository.implementations.post.PostResponseCallback;
import it.unimib.aiprint.repository.interfaces.user.IUserRepository;
import it.unimib.aiprint.source.implementations.post.BasePostDataRemoteDataSource;
import it.unimib.aiprint.source.implementations.user.BaseUserAuthenticationRemoteDataSource;
import it.unimib.aiprint.source.implementations.user.BaseUserDataRemoteDataSource;

public class UserRepository implements IUserRepository, UserResponseCallback, PostResponseCallback {

    private static final String TAG = UserRepository.class.getSimpleName();

    private final BaseUserAuthenticationRemoteDataSource userRemoteDataSource;
    private final BaseUserDataRemoteDataSource userDataRemoteDataSource;
    private final BasePostDataRemoteDataSource postLocalDataSource;
    private final MutableLiveData<Result> userMutableLiveData;
    private final MutableLiveData<Result> userFavoriteNewsMutableLiveData;
    private final MutableLiveData<Result> userPreferencesMutableLiveData;

    public UserRepository(BaseUserAuthenticationRemoteDataSource userRemoteDataSource,
                          BaseUserDataRemoteDataSource userDataRemoteDataSource,
                          BasePostDataRemoteDataSource newsLocalDataSource) {
        this.userRemoteDataSource = userRemoteDataSource;
        this.userDataRemoteDataSource = userDataRemoteDataSource;
        this.postLocalDataSource = newsLocalDataSource;
        this.userMutableLiveData = new MutableLiveData<>();
        this.userPreferencesMutableLiveData = new MutableLiveData<>();
        this.userFavoriteNewsMutableLiveData = new MutableLiveData<>();
        this.userRemoteDataSource.setUserResponseCallback(this);
        this.userDataRemoteDataSource.setUserResponseCallback(this);
        this.postLocalDataSource.setPostResponseCallback(this);
    }

    @Override
    public MutableLiveData<Result> getUser(String email, String password, boolean isUserRegistered) {
        if (isUserRegistered) {
            signIn(email, password);
        } else {
            signUp(email, password);
        }
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getGoogleUser(String idToken) {
        signInWithGoogle(idToken);
        return userMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserFavoritePost(String idToken) {
        userDataRemoteDataSource.getUserFavoritePost(idToken);
        return userFavoriteNewsMutableLiveData;
    }

    @Override
    public MutableLiveData<Result> getUserPreferences(String idToken) {
       // userDataRemoteDataSource.getUserPreferences(idToken);
        return userPreferencesMutableLiveData;
    }

    @Override
    public User getLoggedUser() {
        return userRemoteDataSource.getLoggedUser();
    }

    @Override
    public MutableLiveData<Result> logout() {
        userRemoteDataSource.logout();
        return userMutableLiveData;
    }

    @Override
    public void signUp(String email, String password) {
        userRemoteDataSource.signUp(email, password);
    }

    @Override
    public void signIn(String email, String password) {
        userRemoteDataSource.signIn(email, password);
    }

    @Override
    public void signInWithGoogle(String token) {
        userRemoteDataSource.signInWithGoogle(token);
    }

    @Override
    public void saveUserPreferences(String favoriteCountry, Set<String> favoriteTopics, String idToken) {
        //userDataRemoteDataSource.saveUserPreferences(favoriteCountry, favoriteTopics, idToken);
    }

    @Override
    public void onSuccessFromAuthentication(User user) {
        if (user != null) {
            userDataRemoteDataSource.saveUserData(user);
        }
    }

    @Override
    public void onFailureFromAuthentication(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabase(User user) {
        Result.Success result = new Result.Success<User>(user);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromRemoteDatabaseSinglePost(PostResponse post) {
        String e = "";
    }

    @Override
    public void onSuccessFromRemoteDatabase(List<Post> newsList) {
        //postLocalDataSource.insertNews(newsList);
    }

    @Override
    public void onSuccessFromRemoteDatabaseFavLists(List<Post> newsList) {

    }

    @Override
    public void onSuccessFromGettingUserPreferences() {
        userPreferencesMutableLiveData.postValue(new Result.Success<User>(null));
    }

    @Override
    public void onFailureFromRemoteDatabase(String message) {
        Result.Error result = new Result.Error(message);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessFromLocalDatabase(List<Post> postList) {

    }

    @Override
    public void onSuccessDeletion() {
        Result.Success result = new Result.Success<User>(null);
        userMutableLiveData.postValue(result);
    }

    @Override
    public void onSuccessLogout() {
        postLocalDataSource.deleteAll();
    }
}
