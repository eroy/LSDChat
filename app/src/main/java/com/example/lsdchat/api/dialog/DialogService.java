package com.example.lsdchat.api.dialog;

import com.example.lsdchat.api.dialog.model.ItemDialog;
import com.example.lsdchat.api.dialog.model.ItemMessage;
import com.example.lsdchat.api.dialog.request.CreateDialogRequest;
import com.example.lsdchat.api.dialog.request.CreateMessageRequest;
import com.example.lsdchat.api.dialog.response.DialogsResponse;
import com.example.lsdchat.api.dialog.response.MessagesResponse;
import com.example.lsdchat.constant.ApiConstant;

import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;


public interface DialogService {

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.DIALOGS_REQUEST)
    Observable<DialogsResponse> getDialog(@Header(ApiConstant.QB_TOKEN) String token);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.DIALOGS_REQUEST)
    Observable<ItemDialog> createDialog(@Header(ApiConstant.QB_TOKEN) String token, @Body CreateDialogRequest createDialogRequest);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @GET(ApiConstant.MESSAGES_REQUEST)
    Observable<MessagesResponse> getMessages(@Header(ApiConstant.QB_TOKEN) String token, @Query("chat_dialog_id") String chatDialogId);

    @Headers(ApiConstant.HEADER_CONTENT_TYPE)
    @POST(ApiConstant.MESSAGES_REQUEST)
    Observable<ItemMessage> createMessages(@Header(ApiConstant.QB_TOKEN) String token, @Body CreateMessageRequest createMessageRequest);



}