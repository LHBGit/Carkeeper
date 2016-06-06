package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.andexert.library.RippleView;
import com.loopj.android.http.RequestParams;
import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;
import com.wteam.carkeeper.entity.FeedbackInfoVo;
import com.wteam.carkeeper.entity.ResultMessage;
import com.wteam.carkeeper.network.CodeType;
import com.wteam.carkeeper.network.HttpUtil;
import com.wteam.carkeeper.network.NetCallBack;
import com.wteam.carkeeper.network.UrlManagement;

import cz.msebera.android.httpclient.Header;

/**
 * Created by lhb on 2016/5/4.
 */
public class FeedbackActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,RippleView.OnRippleCompleteListener{

    private TopBar feedback_top_bar;
    private RippleView rv_sentFeedback;
    private EditText feedback_content;
    private EditText feedback_contact_way;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_top_bar = (TopBar) findViewById(R.id.feedback_top_bar);
        feedback_contact_way = (EditText) findViewById(R.id.feedback_contact_way);
        feedback_content = (EditText) findViewById(R.id.feedback_content);
        rv_sentFeedback = (RippleView) findViewById(R.id.rv_sentFeedback);

        feedback_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        rv_sentFeedback.setOnRippleCompleteListener(this);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }

    @Override
    public void onComplete(final RippleView rippleView) {
        rippleView.setClickable(false);

        String feedbackMsg = feedback_content.getText().toString().trim();
        String contactWay = feedback_contact_way.getText().toString().trim();
        if("".equals(feedbackMsg) || "".equals(contactWay)) {
            Toast.makeText(FeedbackActivity.this,"反馈内容及联系方式不能为空！",Toast.LENGTH_LONG).show();
            return;
        }

        if(feedbackMsg.length() < 20) {
            Toast.makeText(FeedbackActivity.this,"反馈内容太短！",Toast.LENGTH_LONG).show();
            return;
        }

        RequestParams requestParams = new RequestParams();
        FeedbackInfoVo feedbackInfoVo = new FeedbackInfoVo();
        feedbackInfoVo.setContactInformation(contactWay);
        feedbackInfoVo.setContent(feedbackMsg);
        String json = JSON.toJSON(feedbackInfoVo).toString();
        requestParams.add("feedbackInfoVo",json);


        HttpUtil.post(UrlManagement.SAVE_FEEDBACK_INFO, requestParams, new NetCallBack(UrlManagement.SAVE_FEEDBACK_INFO, requestParams) {
            @Override
            public void success(int statusCode, Header[] headers, String responseString) {
                if(null != responseString) {
                    ResultMessage resultMessage = JSON.parseObject(responseString,ResultMessage.class);
                    if(CodeType.OPERATION_SUCCESS.getCode().equals(resultMessage.getCode())) {
                        Toast.makeText(FeedbackActivity.this, "反馈成功，谢谢您的反馈！" , Toast.LENGTH_LONG).show();
                    }
                }
                finish();
                rippleView.setClickable(true);
            }

            @Override
            public void failure(int statusCode, Header[] headers, String responseString, String errorCode) {
                Toast.makeText(FeedbackActivity.this,"网络连接超时，请检查网络后再试！" , Toast.LENGTH_LONG).show();
                rippleView.setClickable(true);
            }
        });
    }
}
