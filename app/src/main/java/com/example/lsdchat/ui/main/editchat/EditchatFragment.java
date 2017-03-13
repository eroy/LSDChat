package com.example.lsdchat.ui.main.editchat;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lsdchat.App;
import com.example.lsdchat.R;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.facebook.drawee.view.SimpleDraweeView;

public class EditchatFragment extends BaseFragment implements EditchatContract.View {
    private static final String DIALOG_ID = "dialog_id";
    private static final int LOADER_ID = 101;
    private static final String TAG = "AAA_LOADER";

    private EditchatPresenter mPresenter;

    private Toolbar mToolbar;
    private EditText mEditChatName;
    private SimpleDraweeView mDialogImage;
    private Button mSaveButton;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_editchat, container, false);

        initView(view);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Log.i(TAG, "onActivityCreated");
        getLoaderManager().initLoader(LOADER_ID, null, new LoaderManager.LoaderCallbacks<EditchatPresenter>() {
            @Override
            public Loader<EditchatPresenter> onCreateLoader(int id, Bundle args) {
                Log.i(TAG, "onCreateLoader");
                return new PresenterLoader<EditchatPresenter>(getContext(), getFactory(), TAG);
            }

            @Override
            public void onLoadFinished(Loader<EditchatPresenter> loader, EditchatPresenter data) {
                Log.i(TAG, "onLoadFinished");
                EditchatFragment.this.mPresenter = data;
            }

            @Override
            public void onLoaderReset(Loader<EditchatPresenter> loader) {
                Log.i(TAG, "onLoaderReset");
                EditchatFragment.this.mPresenter = null;
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume " + mPresenter.toString());
        mPresenter.loadDialogCredentials(getArguments().getString(DIALOG_ID));

        Toast.makeText(getContext(), getArguments().getString(DIALOG_ID), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPause() {
        mPresenter.onDetach();
        super.onPause();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().onBackPressed();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void fillDialogInformation(String dialogName) {
        mEditChatName.setText(dialogName);
    }

    private void initView(View view) {
        mEditChatName = (EditText) view.findViewById(R.id.editchat_et_chatname);
        mSaveButton = (Button) view.findViewById(R.id.editchat_button);
        mDialogImage = (SimpleDraweeView) view.findViewById(R.id.editchat_groupeimage);
        mToolbar = (Toolbar) view.findViewById(R.id.chats_toolbar);

        initToolbar(mToolbar, getString(R.string.edit_chat_title));
    }

    private EditchatPresenterFactory getFactory() {
        EditchatContract.Model model = new EditchatModel();
        return new EditchatPresenterFactory(model, this, App.getSharedPreferencesManager(getContext()));
    }

    public static EditchatFragment newInstance(String dialogID) {
        EditchatFragment fragment = new EditchatFragment();
        Bundle bundle = new Bundle();
        bundle.putString(DIALOG_ID, dialogID);
        fragment.setArguments(bundle);
        return fragment;
    }


}

