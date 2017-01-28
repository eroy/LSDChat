package com.example.lsdchat.ui.login;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.example.lsdchat.App;
import com.example.lsdchat.api.request.LoginRequest;
import com.example.lsdchat.api.request.SessionRequestAuth;
import com.example.lsdchat.manager.DataManager;
import com.example.lsdchat.model.User;


public class LoginPresented implements LoginContract.Presented {
    private LoginContract.View mView;
    private Context mContext;
    private DataManager mDataManager = new DataManager();


    public LoginPresented(LoginContract.View mView) {
        this.mView = mView;
    }

    @Override
    public void onDestroy() {
        // TODO: 28.01.2017 [Code Review] leave some comment, like 'unused' or 'not required'
        // try not to leave empty bodies in your code
    }


    @Override
    public void validateCredentials(String email, String password) {
        requestSessionAndLogin(email, password);
    }


    @Override
    public void requestSessionAndLogin(String email, String password) {
        // TODO: 28.01.2017 [Code Review] you should inject App.getApiManager() to this class as parameter
        // to have possibility to mock in for tests
        App.getApiManager().getSessionAuth(new SessionRequestAuth(email, password))
                .doOnNext(sessionResponse -> getLoginWithToken(email, password, sessionResponse.getSession().getToken()))
                .subscribe(sessionResponse -> {
                    addUserToDb(email,password,mView.isKeepSignIn());
                            Log.e("AAA", "TOKEN  - " + sessionResponse.getSession().getToken());
//                            save token

                        },
                        // TODO: 28.01.2017 [Code Review] add proper error handling logic
                        throwable -> Log.e("11111", throwable.getMessage())
                );
    }


    private void getLoginWithToken(String email, String password, String token) {
        App.getApiManager().getLogin(new LoginRequest(email, password), token)
                .doOnNext(loginResponse -> mView.navigateToMainScreen())
                .subscribe(loginUser -> {
                            //                    save model users
                            Log.e("AAA", "id  - " + loginUser.getLoginUser().getId() + " phone-" + loginUser.getLoginUser().getPhone());
                        },
                        // TODO: 28.01.2017 [Code Review] add proper error handling logic
                        throwable -> Log.e("22222", throwable.getMessage()));
    }

    //    add current user to db
    private void addUserToDb(String email, String password, boolean isKeepSignIn) {
        User currentUser = new User();
        currentUser.setEmail(email);
        currentUser.setPassword(password);
        currentUser.setSignIn(isKeepSignIn);
//        mDataManager.insertUser(currentUser);
    }


    //    validation email and password
    private boolean validationData(String email, String password) {
        return validEmail(email) && validPassword(password);
    }


    private boolean validPassword(String password) {
        if (!TextUtils.isEmpty(password) && !(password.length() < 8)) {
            mView.hidePasswordError();
            return true;
        } else {
            mView.setPasswordError();
            return false;
        }
    }

    private boolean validEmail(String email) {
        if (!TextUtils.isEmpty(email) && email.contains("@")) {
            mView.hideEmailError();
            return true;
        } else {
            mView.setEmailError();
            return false;
        }
    }


    @Override
    public void attachView(LoginContract.View view) {
        mView = view;
        // TODO: 28.01.2017 [Code Review] try not to use Android SDK's classes in Presenter, especially Context
        mContext = mView.getContext();
    }

    @Override
    public void detachView() {
        mView = null;
        // TODO: 28.01.2017 [Code Review] nullify mContext as well.
    }
}