package it.unimib.aiprint.ui.welcome;

import static it.unimib.aiprint.util.Constants.EMAIL_ADDRESS;
import static it.unimib.aiprint.util.Constants.ENCRYPTED_DATA_FILE_NAME;
import static it.unimib.aiprint.util.Constants.ENCRYPTED_SHARED_PREFERENCES_FILE_NAME;
import static it.unimib.aiprint.util.Constants.ID_TOKEN;
import static it.unimib.aiprint.util.Constants.INVALID_CREDENTIALS_ERROR;
import static it.unimib.aiprint.util.Constants.INVALID_USER_ERROR;
import static it.unimib.aiprint.util.Constants.PASSWORD;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.google.android.material.textfield.TextInputLayout;

import java.io.IOException;
import java.security.GeneralSecurityException;

import it.unimib.aiprint.R;
import it.unimib.aiprint.model.Result;
import it.unimib.aiprint.model.User;
import it.unimib.aiprint.util.DataEncryptionUtil;
import it.unimib.aiprint.viewModel.UserViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LoginUserFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoginUserFragment extends Fragment {
    private static final String TAG = LoginUserFragment.class.getSimpleName();

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private UserViewModel userViewModel;
    private DataEncryptionUtil dataEncryptionUtil;

    private TextInputLayout textInputLayoutEmail;
    private TextInputLayout textInputLayoutPassword;

    public LoginUserFragment() {}

    public static LoginUserFragment newInstance(String param1, String param2) {
        LoginUserFragment fragment = new LoginUserFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        userViewModel = new ViewModelProvider(requireActivity()).get(UserViewModel.class);
        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login_user, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        Button loginButton = (Button) view.findViewById(R.id.BTN_accedi2);
        EditText emailBox = (EditText) view.findViewById(R.id.editTextTextEmailAddress);
        EditText passwordBox = (EditText) view.findViewById(R.id.editTextTextPassword);

        dataEncryptionUtil = new DataEncryptionUtil(requireActivity().getApplication());

        if (userViewModel.getLoggedUser() != null) {
            Navigation.findNavController(view).navigate(R.id.action_loginUserFragment_to_mainActivity);
        }

        try {
            Log.d(TAG, "Email address from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS));
            Log.d(TAG, "Password from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD));
            Log.d(TAG, "Token from encrypted SharedPref: " + dataEncryptionUtil.
                    readSecretDataWithEncryptedSharedPreferences(
                            ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN));
            Log.d(TAG, "Login data from encrypted file: " + dataEncryptionUtil.
                    readSecretDataOnFile(ENCRYPTED_DATA_FILE_NAME));
        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }

        loginButton.setOnClickListener(v-> {
            String email = emailBox.getText().toString();
            String password = passwordBox.getText().toString();
            if (isEmailOk(email) & isPasswordOk(password)) {
                if (!userViewModel.isAuthenticationError()) {
                    //progressIndicator.setVisibility(View.VISIBLE);
                    userViewModel.getUserMutableLiveData(email, password, true).observe(
                            getViewLifecycleOwner(), result -> {
                                if (result.isSuccess()) {
                                    User user = (User) ((Result.Success) result).getData();
                                    saveLoginData(email, password, user.getIdToken());
                                    userViewModel.setAuthenticationError(false);
                                    retrieveUserInformationAndStartActivity(view, user);
                                } else {
                                    userViewModel.setAuthenticationError(true);
                                    //progressIndicator.setVisibility(View.GONE);
                                    //Snackbar.make(requireActivity().findViewById(android.R.id.content),
                                    //        getErrorMessage(((Result.Error) result).getMessage()),
                                    //        Snackbar.LENGTH_SHORT).show();
                                }
                            });
                } else {
                    userViewModel.getUser(email, password, true);
                }
            } else {
                //Snackbar.make(requireActivity().findViewById(android.R.id.content),
                //        R.string.check_login_data_message, Snackbar.LENGTH_SHORT).show();
            }

            Navigation.findNavController(view).navigate(R.id.action_loginUserFragment_to_mainActivity);
        });


    }

    private void saveLoginData(String email, String password, String idToken) {
        try {
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, EMAIL_ADDRESS, email);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, PASSWORD, password);
            dataEncryptionUtil.writeSecretDataWithEncryptedSharedPreferences(
                    ENCRYPTED_SHARED_PREFERENCES_FILE_NAME, ID_TOKEN, idToken);

            if (password != null) {
                dataEncryptionUtil.writeSecreteDataOnFile(ENCRYPTED_DATA_FILE_NAME,
                        email.concat(":").concat(password));
            }

        } catch (GeneralSecurityException | IOException e) {
            e.printStackTrace();
        }
    }

    private boolean isEmailOk(String email) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
        //if (!EmailValidator.getInstance().isValid((email))) {
        //    textInputLayoutEmail.setError(getString(R.string.error_email));
        //    return false;
        //} else {
        //    textInputLayoutEmail.setError(null);
        //    return true;
        //}
        return true;
    }

    private boolean isPasswordOk(String password) {
        // Check if the email is valid through the use of this library:
        // https://commons.apache.org/proper/commons-validator/
      return true;
    }

    @Override
    public void onResume() {
        super.onResume();
        userViewModel.setAuthenticationError(false);
    }

    private String getErrorMessage(String errorType) {
        switch (errorType) {
            case INVALID_CREDENTIALS_ERROR:
                return requireActivity().getString(R.string.error_login_password_message);
            case INVALID_USER_ERROR:
                return requireActivity().getString(R.string.error_login_user_message);
            default:
                return requireActivity().getString(R.string.unexpected_error);
        }
    }


    private void retrieveUserInformationAndStartActivity(View view, User user) {
        //progressIndicator.setVisibility(View.VISIBLE);

        userViewModel.getUserFavoriteNewsMutableLiveData(user.getIdToken()).observe(
                getViewLifecycleOwner(), userFavoriteNewsRetrievalResult -> {
                   // progressIndicator.setVisibility(View.GONE);
                    Navigation.findNavController(view).navigate(R.id.action_loginUserFragment_to_mainActivity);
                }
        );
        requireActivity().finish();
    }
}