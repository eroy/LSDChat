package com.example.lsdchat.ui.chat;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsdchat.R;
import com.example.lsdchat.behavior.NonSwipeableViewPager;
import com.example.lsdchat.ui.chat.pager.DialogFragment;
import com.example.lsdchat.ui.chat.pager.ViewPagerAdapter;

import java.lang.reflect.Field;
import java.util.ArrayList;


public class ChatActivity extends AppCompatActivity implements ChatContract.View {
    private static final int OFFSCREEN_PAGE_LIMIT = 1;
    private ChatContract.Presenter mPresenter;
    private Toolbar mToolbar;
    private FloatingActionButton mFloatingActionButton;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;
    private TabLayout mTabLayout;
    private NonSwipeableViewPager mViewPager;
    private TextView mSpannableText;
    private ImageView mNoChatsImage;
    private LinearLayout mNoChatsMessage;
    private ArrayList<DialogFragment> fragmentList = new ArrayList<>();
    private ArrayList<String> titleList = new ArrayList<>();
    private int mUserId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPresenter = new ChatPresenter(this);
        mUserId = mPresenter.getUserId();
        ArrayList<Integer> userIdis = new ArrayList<>();
        userIdis.add(mUserId);
        fillFragmentList(userIdis);
        fillTitleList();
        initView();
        //search
        handleIntent(getIntent());


        //toggle visibility of viewpager tabs...temporary method.
        toggleTabsVisibility(false);

        mFloatingActionButton.setOnClickListener(view -> {
            //TODO: fab onClicked hadling
            Toast.makeText(this, "FAB clicked", Toast.LENGTH_SHORT).show();
        });

        mNavigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.item_create_new_chat:

                    break;
                case R.id.item_users:

                    break;
                case R.id.item_invite_users:

                    break;
                case R.id.app_options_group:

                    break;
                case R.id.item_settings:

                    break;
                case R.id.item_log_out:

                    break;
            }
            Toast.makeText(ChatActivity.this, "item clicked", Toast.LENGTH_SHORT).show();
            mDrawerLayout.closeDrawers();
            return false;
        });

        //set spannable text (underlined, clickable and colored)
        SpannableString spannableString = new SpannableString(getString(R.string.message_nochats_spannable_text_invite_friends));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View view) {
                //TODO: onClick handling
                Toast.makeText(ChatActivity.this, "clicked", Toast.LENGTH_SHORT).show();
            }
        };
        spannableString.setSpan(clickableSpan, 26, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new ForegroundColorSpan(ContextCompat.getColor(ChatActivity.this, R.color.colorNavyBlue)), 26, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        spannableString.setSpan(new UnderlineSpan(), 26, 40, 0);
        mSpannableText.setMovementMethod(LinkMovementMethod.getInstance());
        mSpannableText.setText(spannableString);
    }

    private void initToolbar() {
        mToolbar = (Toolbar) findViewById(R.id.chats_toolbar);
        mToolbar.setNavigationIcon(R.drawable.ic_menu_white_24dp);
        setSupportActionBar(mToolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeButtonEnabled(true);
            actionBar.setTitle(R.string.chats);
        }

    }

    private void initViewPager() {
        mViewPager = (NonSwipeableViewPager) findViewById(R.id.chats_viewpager);
        mViewPager.setOffscreenPageLimit(OFFSCREEN_PAGE_LIMIT);
        mViewPager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), fragmentList, titleList));
        mTabLayout.setupWithViewPager(mViewPager);
    }

    private void initView() {
        initToolbar();
        mTabLayout = (TabLayout) findViewById(R.id.chats_tabs);
        initViewPager();

        mFloatingActionButton = (FloatingActionButton) findViewById(R.id.chats_fab);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.chats_drawer);
        mNavigationView = (NavigationView) findViewById(R.id.chats_nav);

        mSpannableText = (TextView) findViewById(R.id.message_nochats_text_view);
        mNoChatsMessage = (LinearLayout) findViewById(R.id.message_nochats_root_layout);
        mNoChatsImage = (ImageView) findViewById(R.id.message_nochats_image_view);

        setGrayScale(mNoChatsImage);
    }

    private void toggleTabsVisibility(boolean value) {
        if (value == true) {
            mTabLayout.setVisibility(View.VISIBLE);
            mViewPager.setVisibility(View.VISIBLE);
            mNoChatsMessage.setVisibility(View.GONE);
        } else {
            mTabLayout.setVisibility(View.GONE);
            mViewPager.setVisibility(View.GONE);
            mNoChatsMessage.setVisibility(View.VISIBLE);
        }
    }

    private void setGrayScale(ImageView v) {
        ColorMatrix matrix = new ColorMatrix();
        matrix.setSaturation(0);
        ColorMatrixColorFilter cf = new ColorMatrixColorFilter(matrix);
        v.setColorFilter(cf);
    }

    private void fillFragmentList(ArrayList<Integer> userIds) {
        fragmentList.add(DialogFragment.newInstance(DialogFragment.PUBLIC_GROUP, userIds));
        fragmentList.add(DialogFragment.newInstance(DialogFragment.PRIVATE, userIds));
    }

    private void fillTitleList() {
        titleList.add(getString(R.string.public_chat));
        titleList.add(getString(R.string.private_chat));
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            //TODO: use the query to search data somehow
            Toast.makeText(this, query, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //creating search icon at the right side of toolbar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.toolbar_options_menu, menu);

        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.toolbar_search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));
        searchView.setQueryHint("Search");
        //change caret color to white
        AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        try {
            Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
            mCursorDrawableRes.setAccessible(true);
            //Sets the cursor resource id
            mCursorDrawableRes.set(searchTextView, R.drawable.caretcolor_searchable_shape);
        } catch (Exception e) {
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(Gravity.LEFT);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.LEFT) == true) {
            mDrawerLayout.closeDrawers();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        //in case singleTop flag
        handleIntent(intent);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mPresenter.onDestroy();
    }


}