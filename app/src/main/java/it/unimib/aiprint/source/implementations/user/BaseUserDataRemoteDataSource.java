package it.unimib.aiprint.source.implementations.user;

import it.unimib.aiprint.model.User;
import it.unimib.aiprint.repository.UserResponseCallback;

public abstract class BaseUserDataRemoteDataSource {
    protected UserResponseCallback userResponseCallback;

    public void setUserResponseCallback(UserResponseCallback userResponseCallback) {
        this.userResponseCallback = userResponseCallback;
    }

    public abstract void saveUserData(User user);
    public abstract void getUserFavoritePost(String idToken);

}
