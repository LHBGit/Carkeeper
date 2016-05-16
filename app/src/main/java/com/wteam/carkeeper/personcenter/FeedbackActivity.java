package com.wteam.carkeeper.personcenter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.wteam.carkeeper.R;
import com.wteam.carkeeper.custom.TopBar;

/**
 * Created by lhb on 2016/5/4.
 */
public class FeedbackActivity extends AppCompatActivity implements TopBar.Top_bar_tv_1_ClickListener,View.OnClickListener{

    private TopBar feedback_top_bar;
    private TextView sentFeedback;
    private EditText feedback_content;
    private EditText feedback_contact_way;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);

        feedback_top_bar = (TopBar) findViewById(R.id.feedback_top_bar);
        feedback_contact_way = (EditText) findViewById(R.id.feedback_contact_way);
        feedback_content = (EditText) findViewById(R.id.feedback_content);
        sentFeedback = (TextView) findViewById(R.id.sentFeedback);

        feedback_top_bar.setOnTop_bar_tv_1_ClickListener(this);
        sentFeedback.setOnClickListener(this);
    }

    @Override
    public void top_bar_tv_1_click(View view) {
        view.setClickable(false);
        finish();
        view.setClickable(true);
    }

    @Override
    public void onClick(View v) {
        String feedbackMsg = feedback_content.getText().toString().trim();
        String contactWay = feedback_contact_way.getText().toString().trim();
        if("".equals(feedbackMsg) || "".equals(contactWay)) {
            Toast.makeText(FeedbackActivity.this,"反馈内容及联系方式不能为空",Toast.LENGTH_LONG).show();
            return;
        }

        String content = "您输入的内容是:" + feedbackMsg + "\n"
                + "您输入的联系方式为：" + contactWay;
        Toast.makeText(FeedbackActivity.this,content,Toast.LENGTH_SHORT).show();
    }
}
