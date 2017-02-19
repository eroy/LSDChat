package com.example.lsdchat.ui.chat.pager;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.api.dialog.model.ItemDialog;

import java.util.ArrayList;
import java.util.List;

public class DialogFragment extends Fragment implements DialogContract.View {
    public static final int PUBLIC_GROUP = 1;
    public static final int GROUP = 2;
    public static final int PRIVATE = 3;
    private static String TYPE = "type";
    private static String USER_IDS = "userId";
    //    private LinearLayout rootLayout;
    private DialogContract.Presenter mPresenter;
    private int mType;
    private ArrayList<Integer> userIds;
    private int userId;

    public static DialogFragment newInstance(int type, ArrayList<Integer> userIds) {
        Bundle args = new Bundle();
        args.putInt(TYPE, type);
        args.putIntegerArrayList(USER_IDS, userIds);
        DialogFragment fragment = new DialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.viewpager_pagefragment_rootlayout, container, false);
        initView(view);
        mType = getArguments().getInt(TYPE);
        userIds = getArguments().getIntegerArrayList(USER_IDS);
        mPresenter = new DialogPresenter(this, userIds, App.getSharedPreferencesManager());
        mPresenter.getAllDialogs();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.onDestroy();
    }

    public void initView(View view) {
//        rootLayout = (LinearLayout) view.findViewById(R.id.viewpager_rootlayout);
    }

    @Override
    public void receiveDialogs(List<ItemDialog> dialogs) {

    }
}