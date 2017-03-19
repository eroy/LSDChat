package com.example.lsdchat.ui.main.editchat;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.model.OccupantsPull;
import com.example.lsdchat.api.dialog.model.OccupantsPush;
import com.example.lsdchat.api.dialog.request.UpdateDialogRequest;
import com.example.lsdchat.constant.ApiConstant;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.model.IdsListInteger;
import com.example.lsdchat.model.RealmDialogModel;
import com.example.lsdchat.util.CreateMapRequestBody;
import com.example.lsdchat.util.StorageHelper;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;

public class EditchatPresenter implements EditchatContract.Presenter {
    private static final int REQUEST_IMAGE_CAMERA = 11;
    private static final int REQUEST_IMAGE_GALLERY = 22;

    private static final String DATE_FORMAT = "yyyyMMdd_HHmmss";
    private static final String AVATAR_FILE_NAME = "_dialog_icon.jpg";

    private Uri mFullSizeAvatarUri = null;
    private File mUploadFile = null;
    private List<Integer> mCheckedOccupantsList;

    private EditchatContract.View mView;
    private EditchatContract.Model mModel;

    private Context mContext;
    private SharedPreferencesManager mPreferencesManager;
    private int mDialogType;
    private String mDialogID;
    private String mNewDialogName;

    public EditchatPresenter(EditchatContract.View view, EditchatContract.Model model, SharedPreferencesManager manager) {
        Log.e("AAA", "constructor");
        mView = view;
        mContext = view.getContext();
        mModel = model;
        mPreferencesManager = manager;
        mCheckedOccupantsList = new ArrayList<>();
        mDialogID = null;
        mNewDialogName = null;
    }

    @Override
    public void onDetach() {
        mView = null;
    }

    @Override
    public void onDestroy() {
        //nothing to clean at this point
    }

    @Override
    public void loadDialogCredentials(String dialogID) {
        mModel.getDialogByID(mPreferencesManager.getToken(), dialogID)
                .subscribe(dialogsResponse -> {
                    mDialogID = dialogID;
                    ItemDialog item = dialogsResponse.getItemDialogList().get(0);
                    mView.fillDialogNameField(item.getName(), item.getOwnerId());

                    prepareAndShowDialogInformation(item);
                });


//        mModel.getDialogFromDatabase(dialogID)
//                .subscribe(dialogModel -> {
//                    mDialogID = dialogID;
//                    mView.fillDialogNameField(dialogModel.getName(), dialogModel.getOwnerId());
//
//                    prepareAndShowDialogInformation(dialogModel);
//                }, throwable -> {
//
//                });
    }

    private void prepareAndShowDialogInformation(ItemDialog dialogModel) {
        mModel.getDialogAvatarFromDatabase(dialogModel.getId())
                .map(contentModel -> {
                    if (contentModel != null) {
                        if (!contentModel.getImagePath().equals("0"))
                            return contentModel.getImagePath();
                        return "";
                    }
                    return "";
                }).subscribe(s -> {
                    Log.e("AAA - AVATAR_STRING", "avatar string <" + s + ">");

                    mView.showDialogAvatar(Uri.fromFile(new File(s)));
                },
                throwable -> {
                    Log.e("AAA - avatarError", throwable.getMessage().toString());
                });

        int dialogType = dialogModel.getType();
        mDialogType = dialogType;
        List<Integer> dialogOccupantsIDs = new ArrayList<>();
        for (Integer id : dialogModel.getOccupantsIdsList()) {
            dialogOccupantsIDs.add(id);
        }

        mModel.getAppUsersFromDatabase()
                .subscribe(loginUsers -> {
                            Log.e("AAA - APP_USERS", String.valueOf(loginUsers.size()));
                            mCheckedOccupantsList.clear();
                            mCheckedOccupantsList.addAll(dialogOccupantsIDs);

                            mView.fillDialogAdapter(dialogOccupantsIDs, loginUsers, dialogType);
                        },
                        throwable -> {
                            Log.e("AAA - usersError", throwable.getMessage());
                        });
    }

    @Override
    public void getAvatarsFromDatabase() {
        mModel.getAllAvatarsFromDatabase()
                .subscribe(contentModelList -> {
                    mView.fillAdapterContentModelsList(contentModelList);
                }, throwable -> {
                    Log.e("AAA", throwable.getMessage());
                });
    }

    @Override
    public void getPhotoFromGallery() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ((Activity) mContext).startActivityForResult(pickPhoto, REQUEST_IMAGE_GALLERY);
    }

    @Override
    public void getPhotoFromCamera() {
        if (mContext.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
            Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            if (pictureIntent.resolveActivity(mContext.getPackageManager()) != null) {

                File storageDir = mContext.getExternalFilesDir(String.valueOf(Environment.DIRECTORY_PICTURES));
                String timestamp = new SimpleDateFormat(DATE_FORMAT).format(new Date());
                File file = new File(storageDir + "/" + timestamp + AVATAR_FILE_NAME);
                mFullSizeAvatarUri = Uri.fromFile(file);

                pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mFullSizeAvatarUri);
                ((Activity) mContext).startActivityForResult(pictureIntent, REQUEST_IMAGE_CAMERA);
            }
        } else {
            Toast.makeText(mContext, mContext.getString(R.string.there_is_no_device_camera), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public Uri getDialogImageUri() {
        return mFullSizeAvatarUri;
    }

    @Override
    public void saveDialogImageUri(Uri uri) {
        mFullSizeAvatarUri = uri;
        try {
            mUploadFile = StorageHelper.decodeAndSaveUri(mContext, mFullSizeAvatarUri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Toast.makeText(mContext, mContext.getString(R.string.photo_added), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showPermissionErrorMessage() {
        mView.showPermissionErrorMessage();
    }

    @Override
    public void updateDialogCredentials(List<Integer> addedOccupants, List<Integer> deletedOccupants, String dialogName) {
        mNewDialogName = dialogName;

        if (mUploadFile != null) {
            getBlobObjectCreateFile(mPreferencesManager.getToken(), getFileMimeType(mUploadFile), mUploadFile.getName());
        }

        if (!addedOccupants.isEmpty()) {
            UpdateDialogRequest body = new UpdateDialogRequest();
            body.setName(dialogName);
            body.setPushAll(new OccupantsPush(addedOccupants));

            mModel.updateDialog(mPreferencesManager.getToken(), mDialogID, body)
                    .subscribe(itemDialog -> {

                    }, throwable -> {

                    });
        }
        if (!deletedOccupants.isEmpty()) {
            UpdateDialogRequest body = new UpdateDialogRequest();
            body.setName(dialogName);
            body.setPullAll(new OccupantsPull(deletedOccupants));

            mModel.updateDialog(mPreferencesManager.getToken(), mDialogID, body)
                    .subscribe(itemDialog -> {

                    }, throwable -> {

                    });
        } else {
            UpdateDialogRequest body = new UpdateDialogRequest();
            body.setName(dialogName);

            mModel.updateDialog(mPreferencesManager.getToken(), mDialogID, body)
                    .subscribe(itemDialog -> {

                    }, throwable -> {

                    });
        }

        mView.navigateToConversationFragment(mDialogID, mNewDialogName);

    }

    private void getBlobObjectCreateFile(String token, String mime, String fileName) {
        mModel.createFile(token, mime, fileName)
                .subscribe(registrationCreateFileResponse -> {
                    long blobId = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getBlobId();
                    String params = registrationCreateFileResponse.getBlob().getBlobObjestAccess().getParams();
                    Uri uri = Uri.parse(params);

                    RequestBody file = RequestBody.create(MediaType.parse(getFileMimeType(mUploadFile)), mUploadFile);
                    MultipartBody.Part multiPart = MultipartBody.Part.createFormData(ApiConstant.UploadParametres.FILE, mUploadFile.getName(), file);

                    uploadFileRetrofit(token, blobId, CreateMapRequestBody.createMapRequestBody(uri), multiPart);
                }, throwable -> {

//                    decodeThrowableAndShowAlert(throwable);
                    Log.e("TEST", throwable.getMessage());
                });
    }

    private void uploadFileRetrofit(String token, long blobId, HashMap<String, RequestBody> map, MultipartBody.Part file) {
        mModel.uploadFileMap(map, file)
                .subscribe(aVoid -> {
                    long fileSize = mUploadFile.length();
                    if (fileSize != 0 && blobId != 0) {
                        declareFileUploaded(fileSize, token, blobId);
                    }
                }, throwable -> {

//                    decodeThrowableAndShowAlert(throwable);
                });
    }

    private void declareFileUploaded(long size, String token, long blobId) {
        mModel.declareFileUploaded(size, token, blobId)
                .subscribe(aVoid -> {
                    updateDialog(mPreferencesManager.getToken(), mDialogID, blobId);
                }, throwable -> {

//                    decodeThrowableAndShowAlert(throwable);
                });
    }

    private void updateDialog(String token, String dialogID, long blobID) {
        UpdateDialogRequest body = new UpdateDialogRequest();
        body.setPhotoId(blobID);

        mModel.updateDialog(mPreferencesManager.getToken(), mDialogID, body)
                .subscribe(itemDialog -> {

                }, throwable -> {

                });
//                    updateUserBlobId(token, blobId);
    }

    private String getFileMimeType(File file) {
        return URLConnection.guessContentTypeFromName(file.getName());
    }
}
