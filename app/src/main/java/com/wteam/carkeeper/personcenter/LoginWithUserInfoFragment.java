package com.wteam.carkeeper.personcenter;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.wteam.carkeeper.MainActivity;
import com.wteam.carkeeper.R;

/**
 * Created by lhb on 2016/4/29.
 */
public class LoginWithUserInfoFragment extends Fragment implements View.OnClickListener{

    private Button btn_login_with_person_info;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_login_with_userinfo,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        btn_login_with_person_info = (Button)view.findViewById(R.id.btn_login_with_person_info);
        btn_login_with_person_info.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {

        Intent intent = new Intent(getActivity(),MainActivity.class);
        startActivity(intent);
    }
}
