package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.wteam.carkeeper.R;

/**
 * Created by lhb on 2016/4/29.
 */
public class LoginWithTelephoneNumFragment extends Fragment implements View.OnClickListener{

    private TextView get_code;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (0 == msg.arg1){
                get_code.setBackgroundResource(R.drawable.get_code_light);
                get_code.setText("");
            } else {
                get_code.setBackgroundResource(R.drawable.get_code);
                get_code.setText("剩余"+ msg.arg1 + "s");
            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login_with_telephone_num,container,false);
        initView(view);
        return view;
    }

    private void initView(View view) {
        get_code = (TextView) view.findViewById(R.id.get_code);
        get_code.setOnClickListener(this);
    }

    @Override
    public void onClick(final View v) {
        Toast.makeText(getActivity(),"点击事件",Toast.LENGTH_SHORT).show();
        new Thread() {
            @Override
            public void run() {
                v.setClickable(false);
                for(int i=60;i>=0;i--) {
                    try {
                        Message message = handler.obtainMessage();
                        message.arg1 = i;
                        message.sendToTarget();
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                v.setClickable(true);
            }
        }.start();
    }
}
