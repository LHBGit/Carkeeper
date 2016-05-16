package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.andexert.library.RippleView;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/4/26.
 */
public class PersonCenterFragment extends Fragment implements TopBar.Top_bar_tv_1_ClickListener,RippleView.OnRippleCompleteListener,View.OnClickListener {

    private TopBar person_center_top_bar;
    private RippleView rv_account_info;
    private RippleView rv_garage;
    private RippleView rv_order_management;
    private RippleView rv_illegal_check;
    private RippleView rv_feedback;
    private DrawerLayout drawerLayout;
    private ImageView person_center_system_msg;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_personcenter_main,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        person_center_top_bar = (TopBar) view.findViewById(R.id.person_center_top_bar);
        rv_account_info = (RippleView) view.findViewById(R.id.rv_account_info);
        rv_garage= (RippleView) view.findViewById(R.id.rv_garage);
        rv_order_management = (RippleView) view.findViewById(R.id.rv_order_management);
        rv_illegal_check = (RippleView) view.findViewById(R.id.rv_illegal_check);
        rv_feedback = (RippleView) view.findViewById(R.id.rv_feedback);
        person_center_system_msg = (ImageView) view.findViewById(R.id.person_center_system_msg);
        drawerLayout = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);


        person_center_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        rv_account_info.setOnRippleCompleteListener(this);
        rv_garage.setOnRippleCompleteListener(this);
        rv_order_management.setOnRippleCompleteListener(this);
        rv_illegal_check.setOnRippleCompleteListener(this);
        rv_feedback.setOnRippleCompleteListener(this);
        person_center_system_msg.setOnClickListener(this);
    }

    @Override
    public void onComplete(RippleView rippleView) {
        rippleView.setClickable(false);
        switch (rippleView.getId()) {
            case R.id.rv_account_info:
                gotoActivity(PersonInfoActivity.class);
                break;
            case R.id.rv_garage:
                gotoActivity(GarageActivity.class);
                break;
            case R.id.rv_order_management:
                gotoActivity(OrderManagementActivity.class);
                break;
            case R.id.rv_illegal_check:
                gotoActivity(IllegalInfoActivity.class);
                break;
            case R.id.rv_feedback:
                gotoActivity(FeedbackActivity.class);
                break;
            default:break;
        }
        rippleView.setClickable(true);
    }

    private void gotoActivity(Class target) {
        Intent intent = new Intent(getActivity(),target);
        startActivity(intent);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.openDrawer(GravityCompat.START);
        }
        view.setClickable(false);
    }

    @Override
    public void onClick(View v) {
        v.setClickable(false);
        switch (v.getId()) {
            case R.id.person_center_system_msg:
                gotoActivity(SystemMessageActivity.class);
                break;
            default:break;
        }
        v.setClickable(true);
    }
}
