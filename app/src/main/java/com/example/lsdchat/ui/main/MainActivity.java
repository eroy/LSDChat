package com.example.lsdchat.ui.main;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.widget.FrameLayout;

import com.example.lsdchat.R;
import com.example.lsdchat.manager.SharedPreferencesManager;
import com.example.lsdchat.ui.main.chats.ChatsFragment;
import com.example.lsdchat.ui.main.fragment.BaseFragment;
import com.example.lsdchat.util.UsersUtil;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mFrameLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mFrameLayout = (FrameLayout) findViewById(R.id.fragment);

//        get user list and save to db
        UsersUtil.getUserListAndSave(new SharedPreferencesManager(this).getToken());

        replaceFragment(new ChatsFragment());
    }





    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment, fragment).commit();
    }


    @Override
    public void onBackPressed() {
        List fragmentList = getSupportFragmentManager().getFragments();

        boolean handled = false;
        for(Object f : fragmentList) {
            if(f instanceof BaseFragment) {
                handled = ((BaseFragment)f).onBackPressed();

                if(handled) {
                    break;
                }
            }
        }

        if(!handled) {
            super.onBackPressed();
        }
    }
}
