package com.test.wllnon.sirenxinyi.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.test.wllnon.sirenxinyi.R;
import com.test.wllnon.sirenxinyi.application.Application;
import com.test.wllnon.sirenxinyi.constant.Constant;
import com.test.wllnon.sirenxinyi.customview.CircleImageView;
import com.test.wllnon.sirenxinyi.fragment.CollectionConcernFragment;
import com.test.wllnon.sirenxinyi.fragment.DeviceFragment;
import com.test.wllnon.sirenxinyi.fragment.ExplorerFragment;
import com.test.wllnon.sirenxinyi.fragment.FriendFragment;
import com.test.wllnon.sirenxinyi.fragment.HomeFragment;
import com.test.wllnon.sirenxinyi.fragment.MessageFragment;
import com.test.wllnon.sirenxinyi.pojo.User;
import com.test.wllnon.sirenxinyi.service.DeviceConnectService;
import com.test.wllnon.sirenxinyi.utils.network.GsonUtils;

import java.util.HashSet;
import java.util.Set;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    public static final String TAG = "MainActivity";

    private Toolbar toolbar;
    private TabLayout tabLayout;

    private CircleImageView userAvatar;
    private TextView userName;
    private TextView userSignature;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tabLayout = (TabLayout) findViewById(R.id.tablayout_main);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        assert drawer != null;
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        assert navigationView != null;
        navigationView.setNavigationItemSelectedListener(this);

        // enable the first icon of the draw-layout
        toolbar.setTitle(getResources().getString(R.string.home));
        setTabLayoutVisibility(View.GONE);
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.content_layout, new HomeFragment())
                .commit();

        View view = navigationView.getHeaderView(0);
        userAvatar = (CircleImageView) view.findViewById(R.id.avatar_header);
        userName = (TextView) view.findViewById(R.id.name_header);
        userSignature = (TextView) view.findViewById(R.id.signature_header);

        final User user = Application.getInstance().getUser();
        Glide.with(this)
                .load(user.getAvatarUrl())
                .error(R.drawable.ic_account_circle_grey_600_48dp)
                .into(userAvatar);
        userName.setText(user.getName());
        userSignature.setText(user.getSignature());
        userAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PersonalInfoActivity.class);
                intent.putExtra("user", GsonUtils.newInstance().getGson().toJson(user));
                startActivity(intent);
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        startService(new Intent(this, DeviceConnectService.class));
    }

    @Override
    protected void onStop() {
        Set<String> usernames = Application.getInstance().getSharedPreferences().getStringSet(Constant.USERS_HISTORY, null);
        if (usernames != null &&usernames.size() != 0) {
            Object[] names = Application.getInstance().getSharedPreferences().getStringSet(Constant.USERS_HISTORY, new HashSet<String>()).toArray();
            String source = "";
            for (Object name : names) {
                source = source + name + " ";
            }
            Log.i(TAG, "onStop: " + source);
        }
        super.onStop();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!Application.getInstance().getSharedPreferences().getBoolean(Constant.DEFAULT_MODEL, false)) {
            new SweetAlertDialog(this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText(getResources().getString(R.string.run_background))
                    .setContentText(getResources().getString(R.string.run_background_description))
                    .setCancelText(getResources().getString(R.string.run_background_confirm))
                    .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .setConfirmText(getResources().getString(R.string.run_background_cancel))
                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            getApplication().stopService(new Intent(getApplication(), DeviceConnectService.class));
                            sweetAlertDialog.dismissWithAnimation();
                            finish();
                        }
                    })
                    .show();
        } else {
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        switch (id) {
            case R.id.nav_home:
                // Handle the camera action
                navigationWork(item, View.GONE, new HomeFragment());
                break;
            case R.id.nav_explorer:
                // The tab-layout is in main activity, so for each fragment, it needs the tab-layout to do some special works
                ExplorerFragment explorerFragment = new ExplorerFragment();
                explorerFragment.setTabLayout(tabLayout);

                navigationWork(item, View.VISIBLE, explorerFragment);
                break;
            case R.id.nav_friend:
                FriendFragment friendFragment = new FriendFragment();
                friendFragment.setTabLayout(tabLayout);

                navigationWork(item, View.VISIBLE, friendFragment);
                break;
            case R.id.nav_collect_concern:
                CollectionConcernFragment collectionConcernFragment = new CollectionConcernFragment();
                collectionConcernFragment.setTabLayout(tabLayout);

                navigationWork(item, View.VISIBLE, collectionConcernFragment);
                break;
            case R.id.nav_message:
                navigationWork(item, View.GONE, new MessageFragment());
                break;
            case R.id.nav_devices:
                navigationWork(item, View.GONE, new DeviceFragment());
                break;
            case R.id.nav_setting:
                startActivity(new Intent(MainActivity.this, SettingActivity.class));
                break;
            case R.id.nav_exit:
                logoutAndExit();
                break;
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        assert drawer != null;
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * @author wllnon
     * @timer 2016/2/21 22:17
     * @func Do basic things for navigation to another fragment.
     */
    public void navigationWork(MenuItem item, int visibility, final Fragment targetFragment) {
        item.setChecked(true);

        setToolbarTitle(item.getTitle());
        setTabLayoutVisibility(visibility);

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(400);
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.content_layout, targetFragment)
                            .commit();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void logoutAndExit() {
        SharedPreferences.Editor editor = Application.getInstance().getSharedPreferences().edit();
        editor.putBoolean(Constant.AUTO_LOGIN, false);
        editor.putString(Constant.AUTO_EMAIL, "");
        editor.putString(Constant.AUTO_PASSWORD, "");
        editor.apply();

        Application.getInstance().setLogined(false);
        finish();
    }

    public void setToolbarTitle(CharSequence title) {
        if (toolbar != null)
            toolbar.setTitle(title);
    }

    public void setTabLayoutVisibility(int visibility) {
        if (tabLayout != null)
            tabLayout.setVisibility(visibility);
    }
}
