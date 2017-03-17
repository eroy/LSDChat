package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.login.model.LoginUser;
import com.example.lsdchat.model.ContentModel;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.ui.BasePresenter;

import java.util.List;

import rx.Observable;

public interface EditchatContract {
    interface Presenter extends BasePresenter {
        void loadDialogCredentials(String dialogID);

        void getAvatarsFromDatabase();

        void getPhotoFromGallery();

        void getPhotoFromCamera();

        void saveDialogImageUri(Uri uri);

        Uri getDialogImageUri();

        void onActivityResult(int requestCode, int resultCode, Intent data);

    }

    interface View {
        Context getContext();

        void fillDialogNameField(String name);

        void showDialogAvatar(Uri path);

        void fillDialogAdapter(List<Integer> occupantIDs, List<LoginUser> appUsers, int type);

        void fillAdapterContentModelsList(List<ContentModel> contentModels);

    }

    interface Model {
        Observable<DialogsResponse> getDialogByID(String token, String dialogID);

        Observable<ItemDialog> updateDialog(String dialogID, String token, CreateDialogRequest body);

        Observable<RealmDialogModel> getDialogFromDatabase(String dialogID);

        Observable<List<ContentModel>> getAllAvatarsFromDatabase();

        Observable<ContentModel> getDialogAvatarFromDatabase(String dialogID);

        Observable<List<LoginUser>> getAppUsersFromDatabase();
    }
}
