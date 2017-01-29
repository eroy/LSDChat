package com.example.lsdchat.ui.registration;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.design.widget.TextInputEditText;

import com.example.lsdchat.api.registration.RegistrationResponse;
import com.example.lsdchat.api.login.request.SessionRequestNoAuth;
import com.example.lsdchat.api.login.response.SessionResponse;


import rx.Observable;

public interface RegistrationContract {
    interface Presenter {
        //screen navigation
        void navigateToMainScreen();

        void onDestroy();

        void showDialogImageSourceChooser();

        void initFacebookSdk();

        void loginWithFacebook();

        void getFacebookToken();

        void setTextChangedListenerWithInputMask(TextInputEditText phone);

        boolean validateRegForm(String email, String pass, String confPass);

        boolean validateEmail(String email);

        boolean validatePassword(String pass);

        boolean validateConfPassword(String pass, String confPass);

        void requestSessionAndRegistration(boolean validateValue, RegistrationForm form);

        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

    interface View {
        //void onError();
        void setInvalideEmailError();

        void setWeakPasswordError();

        void setLengthPasswordError();

        void setEquelsPasswordError();

        void resetErrorMessages();

        void showProgressBar();

        void hideProgressBar();

        void getUserpicUri(Uri uri);

        Context getContext();
    }

    interface Model {
        Observable<SessionResponse> getSessionNoAuth();
        Observable<RegistrationResponse> getRegistration(String token, RegistrationForm form);
    }
}
