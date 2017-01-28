package com.example.lsdchat.api.request;

import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.util.SignatureTest;
import com.google.gson.annotations.SerializedName;

import java.util.Random;


public class SessionRequestAuth {
    @SerializedName("application_id")
    private String applicationId;
    @SerializedName("auth_key")
    private String authKey;
    @SerializedName("nonce")
    private int nonce;
    @SerializedName("timestamp")
    private long timestamp;
    @SerializedName("signature")
    private String signature;
    @SerializedName("user[email]")
    private String email;
    @SerializedName("user[password]")
    private String password;

    public SessionRequestAuth(String email, String password) {
        // TODO: 28.01.2017 [Code Review] pass all the parameters in constructor, SessionRequestAuth
        // should know nothing about classes like System, ApiConstant etc. Make factory method for
        // request instance creation, for example.
        this.timestamp = System.currentTimeMillis() / 1000;
        this.nonce = new Random().nextInt();
        this.applicationId = ApiConstant.APP_ID;
        this.authKey = ApiConstant.AUTH_KEY;
        this.signature = SignatureTest.calculateSignatureAuth(email, password, nonce, timestamp);
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(String applicationId) {
        this.applicationId = applicationId;
    }

    public String getAuthKey() {
        return authKey;
    }

    public void setAuthKey(String authKey) {
        this.authKey = authKey;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }



}