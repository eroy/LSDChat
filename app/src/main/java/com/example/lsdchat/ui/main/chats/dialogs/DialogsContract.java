package com.example.lsdchat.ui.main.chats.dialogs;


import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.RelativeLayout;

import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.model.DialogModel;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;

// TODO: 3/9/17 [Code Review] pls check code review TODOs in ChatsContract.class and apply appropriate changes here
public interface DialogsContract {

    interface Model {
        List<DialogModel> getDialogsByType(int type);
        Observable<DialogsResponse> getAllDialogs(String token);

        void saveDialog(List<DialogModel> dialogList);

    }

    interface View {
        void initAdapter(List<DialogModel> list);
        void updateAdapter();
        int getType();

        void navigateToChat(Fragment fragment);
    }

    interface Presenter {
        List<DialogModel> showDialogs(int type);
        void getAllDialogAndSave();

        void setImageDialog(CircleImageView imageView, DialogModel dialogModel);

        void setOnRefreshListener(SwipeRefreshLayout swipeRefreshLayout);

        void setOnClickListener(RelativeLayout relativeLayout, DialogModel dialogModel);
    }

}
