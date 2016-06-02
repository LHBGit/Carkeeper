package com.wteam.carkeeper.settings;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SwitchCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import com.andexert.library.RippleView;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.network.CarkeeperApplication;

/**
 * Created by lhb on 2016/4/26.
 */
public class SettingsFragment extends Fragment implements CompoundButton.OnCheckedChangeListener,RippleView.OnRippleCompleteListener,TopBar.Top_bar_tv_1_ClickListener{

    private TopBar settings_top_bar;
    private SwitchCompat auto_play_music_switch;
    private RippleView rv_start_interface;
    private RippleView rv_version_update;
    private RippleView rv_about_me;
    private RippleView logout;
    private DrawerLayout drawerLayout;
    /**
     * 0:出行服务界面
     * 1:个人中心界面
     * 2:音乐休闲界面
     * 3:社区娱乐界面
     */
    private int startInterface;
    /**
     * 是否自动播放音乐
     */
    private boolean isAutoPlayMusic;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_settings,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        settings_top_bar = (TopBar) view.findViewById(R.id.settings_top_bar);
        auto_play_music_switch = (SwitchCompat) view.findViewById(R.id.auto_play_music_switch);
        rv_start_interface = (RippleView) view.findViewById(R.id.rv_start_interface);
        rv_version_update = (RippleView) view.findViewById(R.id.rv_version_update);
        rv_about_me = (RippleView) view.findViewById(R.id.rv_about_me);
        logout = (RippleView) view.findViewById(R.id.logout);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);

        settings_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        auto_play_music_switch.setOnCheckedChangeListener(this);
        rv_start_interface.setOnRippleCompleteListener(this);
        rv_version_update.setOnRippleCompleteListener(this);
        rv_about_me.setOnRippleCompleteListener(this);
        logout.setOnRippleCompleteListener(this);

        sharedPreferences = CarkeeperApplication.getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);
        startInterface = sharedPreferences.getInt("StartInterface",0);
        isAutoPlayMusic = sharedPreferences.getBoolean("isAutoPlayMusic",false);
        auto_play_music_switch.setChecked(isAutoPlayMusic);
        editor  = sharedPreferences.edit();
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        if(isAutoPlayMusic != isChecked) {
            editor.putBoolean("isAutoPlayMusic",isChecked);
            editor.commit();
        }
    }

    @Override
    public void onComplete(RippleView rippleView) {
        rippleView.setClickable(false);
        switch (rippleView.getId()) {
            case R.id.rv_start_interface:
                doStartInterface(rippleView);
                break;
            case R.id.rv_version_update:
                doVersionUpdate(rippleView);
                break;
            case R.id.rv_about_me:
                doAboutMe(rippleView);
                break;
            case R.id.logout:
                doLogout(rippleView);
                break;
            default:break;
        }
        rippleView.setClickable(true);
    }

    /*version_update事件处理*/
    private void doVersionUpdate(RippleView rippleView) {
        Toast.makeText(getActivity(),"当前版本已经最新版本，版本号VL 1.0.0！",Toast.LENGTH_LONG).show();
    }

    /*start_interface事件处理*/
    private  void doStartInterface(RippleView rippleView) {
        AlertDialog startInterfaceDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.start_interface_select)
                .setSingleChoiceItems(getResources().getStringArray(R.array.string_array), startInterface, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startInterface = which ;
                    }
                })
                .setPositiveButton(R.string.confirm,new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),"启动界面修改成功！",Toast.LENGTH_SHORT).show();
                        editor.putInt("StartInterface",startInterface);
                        editor.commit();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();


        if(!startInterfaceDialog.isShowing()) {
            startInterfaceDialog.show();
        }
    }

    /*about_me事件处理*/
    private void doAboutMe(RippleView rippleView) {
        AlertDialog aboutMeDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.about_me)
                .setMessage(R.string.about_me_content)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(getActivity(),""+which,Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                })
                .create();
        if(!aboutMeDialog.isShowing()) {
            aboutMeDialog.show();
        }
    }

    /*logout事件处理*/
    private void doLogout(RippleView rippleView) {
        rippleView.setClickable(false);
        AlertDialog logoutDialog = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.logoutConfirm)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getActivity().finish();
                        dialog.dismiss();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .create();
        rippleView.setClickable(true);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        view.setClickable(true);
    }
}
