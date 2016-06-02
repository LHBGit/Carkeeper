package com.wteam.carkeeper;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.wteam.carkeeper.communication.CommunicationMainFragment;
import com.wteam.carkeeper.custom.DoubleClickExitTools;
import com.wteam.carkeeper.map.MapMainFragment;
import com.wteam.carkeeper.music.MusicMainFragment;
import com.wteam.carkeeper.network.CarkeeperApplication;
import com.wteam.carkeeper.personcenter.LoginActivity;
import com.wteam.carkeeper.personcenter.PersonCenterFragment;
import com.wteam.carkeeper.personcenter.SystemMessageActivity;
import com.wteam.carkeeper.settings.SettingsFragment;
import com.wteam.carkeeper.util.blur.Blurry;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {

    private CircleImageView circleImageView;
    private ImageView imageView;
    private ImageView systemMsg;
    private FragmentManager fragmentManager;
    private Fragment current;
    private MapMainFragment mapMainFragment;
    private PersonCenterFragment personCenterFragment;
    private SettingsFragment settingsFragment;
    private CommunicationMainFragment communicationMainFragment;
    private MusicMainFragment musicMainFragment;
    private DrawerLayout drawer;

    private DoubleClickExitTools doubleClick;
    private MenuItem preMenuItem;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        init();
    }

    private void init() {
        doubleClick = new DoubleClickExitTools(this);
    }

    /**
     * 初始化界面
     */
    private void initView() {
        //初始化首页面及导航栏
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        FrameLayout root = (FrameLayout) navigationView.getHeaderView(0);
        imageView = (ImageView) root.getChildAt(0);
        LinearLayout linearLayout = (LinearLayout) root.getChildAt(1);
        RelativeLayout relativeLayout = (RelativeLayout) linearLayout.getChildAt(0);
        circleImageView = (CircleImageView) relativeLayout.getChildAt(0);
        systemMsg = (ImageView) relativeLayout.getChildAt(1);
        fragmentManager = getSupportFragmentManager();
        mapMainFragment = new MapMainFragment();
        settingsFragment = new SettingsFragment();
        personCenterFragment = new PersonCenterFragment();
        communicationMainFragment = new CommunicationMainFragment();
        musicMainFragment = new MusicMainFragment();

        navigationView.setNavigationItemSelectedListener(this);
        circleImageView.setOnClickListener(this);
        systemMsg.setOnClickListener(this);

        applyBlur();

        /**
         * 根据用户设置初始化首个页面
         */
        SharedPreferences sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        int startInterface = sharedPreferences.getInt("StartInterface",0);
        if(startInterface == 0) {
            preMenuItem = navigationView.getMenu().getItem(0);
            preMenuItem.setChecked(true);
            switchContent(current,mapMainFragment);
        }
        if(startInterface == 1) {
            preMenuItem = navigationView.getMenu().getItem(1);
            preMenuItem.setChecked(true);
            switchContent(current,personCenterFragment);
        }
        if(startInterface == 2) {
            preMenuItem = navigationView.getMenu().getItem(3).getSubMenu().getItem(1);
            preMenuItem.setChecked(true);
            switchContent(current,musicMainFragment);
        }
        if(startInterface == 3) {
            preMenuItem = navigationView.getMenu().getItem(3).getSubMenu().getItem(0);
            preMenuItem.setChecked(true);
            switchContent(current,communicationMainFragment);
        }
    }

    private void switchContent(Fragment from, Fragment to) {
        if(null != to && !to.equals(from)) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

            if(!to.isAdded()) {
                if(null == from) {
                    fragmentTransaction.add(R.id.main_container, to).commit();
                } else {
                    fragmentTransaction.hide(from).add(R.id.main_container, to).commit();
                }
            } else {
                if(null == from) {
                    fragmentTransaction.show(to).commit();
                } else {
                    fragmentTransaction.hide(from).show(to).commit();
                }
            }
            current = to;
        }
    }

    private void gotoActivity(Class c) {
        Intent intent = new Intent(MainActivity.this,c);
        startActivity(intent);
    }


    /**
     * 高斯模糊处理
     */
    private void applyBlur() {
        imageView.getViewTreeObserver().addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
            @Override
            public boolean onPreDraw() {
                imageView.getViewTreeObserver().removeOnPreDrawListener(this);
                Blurry.with(MainActivity.this)
                        .radius(2)
                        .sampling(4)
                        .color(Color.argb(0, 100, 100, 100))
                        .async()
                        .capture(imageView)
                        .into(imageView);
                return true;
            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                drawer.closeDrawer(GravityCompat.START);
                return true;
            } else {
                return doubleClick.onKeyDown(keyCode, event);
            }
        }
        return super.onKeyDown(keyCode, event);
    }


    /**
     * 菜单事件监听
     */
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.menu_car_service) {
            if (!item.isChecked()) {
                preMenuItem.setChecked(false);
                switchContent(current,mapMainFragment);
            }

        } else if (id == R.id.menu_person_center) {
            if (!item.isChecked()) {
                preMenuItem.setChecked(false);
                switchContent(current,personCenterFragment);
            }

        } else if (id == R.id.menu_settings) {
            if (!item.isChecked()) {
                preMenuItem.setChecked(false);
                switchContent(current,settingsFragment);
            }
        } else if (id == R.id.menu_ac_community) {
            if (!item.isChecked()) {
                preMenuItem.setChecked(false);
                switchContent(current,communicationMainFragment);
            }

        } else if (id == R.id.menu_music) {
            if (!item.isChecked()) {
                preMenuItem.setChecked(false);
                switchContent(current,musicMainFragment);
            }
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.circleImageView:
                drawer.closeDrawer(GravityCompat.START);
                gotoActivity(LoginActivity.class);
                break;
            case R.id.nav_header_main_system_msg:
                drawer.closeDrawer(GravityCompat.START);
                gotoActivity(SystemMessageActivity.class);
                break;
            default:
                break;
        }
    }
}
