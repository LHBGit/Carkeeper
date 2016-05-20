package com.wteam.carkeeper.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.wteam.carkeeper.R;

/**
 * 自定义Topbar控件
 */
public class TopBar extends RelativeLayout {

    private Top_bar_tv_1_ClickListener top_bar_tv_1_clickListener;
    private Top_bar_tv_2_ClickListener top_bar_tv_2_clickListener;
    private Top_bar_tv_3_ClickListener top_bar_tv_3_clickListener;
    private Top_bar_tv_4_ClickListener top_bar_tv_4_clickListener;
    private Top_bar_tv_5_ClickListener top_bar_tv_5_clickListener;
    private Top_bar_tv_title_ClickListener top_bar_tv_title_clickListener;

    public interface Top_bar_tv_1_ClickListener {
        void top_bar_tv_1_click(View view);
    }

    public interface Top_bar_tv_2_ClickListener {
        void top_bar_tv_2_click(View view);
    }

    public interface Top_bar_tv_3_ClickListener {
        void top_bar_tv_3_click(View view);
    }

    public interface Top_bar_tv_4_ClickListener {
        void top_bar_tv_4_click(View view);
    }

    public interface Top_bar_tv_5_ClickListener {
        void top_bar_tv_5_click(View view);
    }

    public interface Top_bar_tv_title_ClickListener {
        void top_bar_tv_title_click(View view);
    }

    public void setOnTop_bar_tv_1_ClickListener(Top_bar_tv_1_ClickListener top_bar_tv_1_clickListener) {
        this.top_bar_tv_1_clickListener = top_bar_tv_1_clickListener;
    }

    public void setOnTop_bar_tv_2_ClickListener(Top_bar_tv_2_ClickListener top_bar_tv_2_clickListener) {
        this.top_bar_tv_2_clickListener = top_bar_tv_2_clickListener;
    }

    public void setOnTop_bar_tv_3_ClickListener(Top_bar_tv_3_ClickListener top_bar_tv_3_clickListener) {
        this.top_bar_tv_3_clickListener = top_bar_tv_3_clickListener;
    }

    public void setOnTop_bar_tv_4_ClickListener(Top_bar_tv_4_ClickListener top_bar_tv_4_clickListener) {
        this.top_bar_tv_4_clickListener = top_bar_tv_4_clickListener;
    }

    public void setOnTop_bar_tv_5_ClickListener(Top_bar_tv_5_ClickListener top_bar_tv_5_clickListener) {
        this.top_bar_tv_5_clickListener = top_bar_tv_5_clickListener;
    }

    public void setOnTop_bar_tv_title_ClickListener(Top_bar_tv_title_ClickListener top_bar_tv_title_clickListener) {
        this.top_bar_tv_title_clickListener = top_bar_tv_title_clickListener;
    }

    private TextView top_bar_tv_1;
    private TextView top_bar_tv_2;
    private TextView top_bar_tv_3;
    private TextView top_bar_tv_4;
    private TextView top_bar_tv_5;
    private TextView top_bar_tv_title;

    private String top_bar_tv_1_text;
    private float top_bar_tv_1_textSize;
    private Drawable top_bar_tv_1_bg;
    private int top_bar_tv_1_color;
    private String top_bar_tv_2_text;
    private float top_bar_tv_2_textSize;
    private Drawable top_bar_tv_2_bg;
    private int top_bar_tv_2_color;
    private String top_bar_tv_3_text;
    private float top_bar_tv_3_textSize;
    private Drawable top_bar_tv_3_bg;
    private int top_bar_tv_3_color;
    private String top_bar_tv_4_text;
    private float top_bar_tv_4_textSize;
    private Drawable top_bar_tv_4_bg;
    private int top_bar_tv_4_color;
    private String top_bar_tv_5_text;
    private float top_bar_tv_5_textSize;
    private Drawable top_bar_tv_5_bg;
    private int top_bar_tv_5_color;
    private String top_bar_tv_title_text;
    private float top_bar_tv_title_textSize;
    private Drawable top_bar_tv_title_bg;
    private int top_bar_tv_title_color;
    private static final float TEXT_SIZE = 14.0f;
    private static final int TEXT_COLOR = 0;

    public TopBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.top_bar,this,true);

        top_bar_tv_1 = (TextView) view.findViewById(R.id.top_bar_tv_1);
        top_bar_tv_2 = (TextView) view.findViewById(R.id.top_bar_tv_2);
        top_bar_tv_3 = (TextView) view.findViewById(R.id.top_bar_tv_3);
        top_bar_tv_4 = (TextView) view.findViewById(R.id.top_bar_tv_4);
        top_bar_tv_5 = (TextView) view.findViewById(R.id.top_bar_tv_5);
        top_bar_tv_title = (TextView) view.findViewById(R.id.top_bar_tv_title);

        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.Topbar);
        top_bar_tv_1_text = ta.getString(R.styleable.Topbar_top_bar_tv_1_text);
        top_bar_tv_1_textSize = ta.getFloat(R.styleable.Topbar_top_bar_tv_1_textSize,TEXT_SIZE);
        top_bar_tv_1_bg = ta.getDrawable(R.styleable.Topbar_top_bar_tv_1_bg);
        top_bar_tv_1_color = ta.getColor(R.styleable.Topbar_top_bar_tv_1_textColor,TEXT_COLOR);
        top_bar_tv_1.setText(top_bar_tv_1_text);
        top_bar_tv_1.setTextSize(top_bar_tv_1_textSize);
        top_bar_tv_1.setBackgroundDrawable(top_bar_tv_1_bg);
        top_bar_tv_1.setTextColor(top_bar_tv_1_color);

        top_bar_tv_2_text = ta.getString(R.styleable.Topbar_top_bar_tv_2_text);
        top_bar_tv_2_textSize = ta.getFloat(R.styleable.Topbar_top_bar_tv_2_textSize,TEXT_SIZE);
        top_bar_tv_2_bg = ta.getDrawable(R.styleable.Topbar_top_bar_tv_2_bg);
        top_bar_tv_2_color = ta.getColor(R.styleable.Topbar_top_bar_tv_2_textColor,TEXT_COLOR);
        top_bar_tv_2.setText(top_bar_tv_2_text);
        top_bar_tv_2.setTextSize(top_bar_tv_2_textSize);
        top_bar_tv_2.setBackgroundDrawable(top_bar_tv_2_bg);
        top_bar_tv_2.setTextColor(top_bar_tv_2_color);

        top_bar_tv_3_text = ta.getString(R.styleable.Topbar_top_bar_tv_3_text);
        top_bar_tv_3_textSize = ta.getFloat(R.styleable.Topbar_top_bar_tv_3_textSize,TEXT_SIZE);
        top_bar_tv_3_bg = ta.getDrawable(R.styleable.Topbar_top_bar_tv_3_bg);
        top_bar_tv_3_color = ta.getColor(R.styleable.Topbar_top_bar_tv_3_textColor,TEXT_COLOR);
        top_bar_tv_3.setText(top_bar_tv_3_text);
        top_bar_tv_3.setTextSize(top_bar_tv_3_textSize);
        top_bar_tv_3.setBackgroundDrawable(top_bar_tv_3_bg);
        top_bar_tv_3.setTextColor(top_bar_tv_3_color);

        top_bar_tv_4_text = ta.getString(R.styleable.Topbar_top_bar_tv_4_text);
        top_bar_tv_4_textSize = ta.getFloat(R.styleable.Topbar_top_bar_tv_4_textSize,TEXT_SIZE);
        top_bar_tv_4_bg = ta.getDrawable(R.styleable.Topbar_top_bar_tv_4_bg);
        top_bar_tv_4_color = ta.getColor(R.styleable.Topbar_top_bar_tv_4_textColor,TEXT_COLOR);
        top_bar_tv_4.setText(top_bar_tv_4_text);
        top_bar_tv_4.setTextSize(top_bar_tv_4_textSize);
        top_bar_tv_4.setBackgroundDrawable(top_bar_tv_4_bg);
        top_bar_tv_4.setTextColor(top_bar_tv_4_color);

        top_bar_tv_5_text = ta.getString(R.styleable.Topbar_top_bar_tv_5_text);
        top_bar_tv_5_textSize = ta.getFloat(R.styleable.Topbar_top_bar_tv_5_textSize,TEXT_SIZE);
        top_bar_tv_5_bg = ta.getDrawable(R.styleable.Topbar_top_bar_tv_5_bg);
        top_bar_tv_5_color = ta.getColor(R.styleable.Topbar_top_bar_tv_5_textColor,TEXT_COLOR);
        top_bar_tv_5.setText(top_bar_tv_5_text);
        top_bar_tv_5.setTextSize(top_bar_tv_5_textSize);
        top_bar_tv_5.setBackgroundDrawable(top_bar_tv_5_bg);
        top_bar_tv_5.setTextColor(top_bar_tv_5_color);

        top_bar_tv_title_text = ta.getString(R.styleable.Topbar_top_bar_tv_title_text);
        top_bar_tv_title_textSize = ta.getFloat(R.styleable.Topbar_top_bar_tv_title_textSize,TEXT_SIZE);
        top_bar_tv_title_bg = ta.getDrawable(R.styleable.Topbar_top_bar_tv_title_bg);
        top_bar_tv_title_color = ta.getColor(R.styleable.Topbar_top_bar_tv_title_textColor,TEXT_COLOR);
        top_bar_tv_title.setText(top_bar_tv_title_text);
        top_bar_tv_title.setTextSize(top_bar_tv_title_textSize);
        top_bar_tv_title.setBackgroundDrawable(top_bar_tv_title_bg);
        top_bar_tv_title.setTextColor(top_bar_tv_title_color);

        top_bar_tv_1.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != top_bar_tv_1_clickListener) {
                    top_bar_tv_1_clickListener.top_bar_tv_1_click(v);
                }
            }
        });

        top_bar_tv_2.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != top_bar_tv_2_clickListener) {
                    top_bar_tv_2_clickListener.top_bar_tv_2_click(v);
                }
            }
        });

        top_bar_tv_3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != top_bar_tv_3_clickListener) {
                    top_bar_tv_3_clickListener.top_bar_tv_3_click(v);
                }
            }
        });

        top_bar_tv_4.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != top_bar_tv_4_clickListener) {
                    top_bar_tv_4_clickListener.top_bar_tv_4_click(v);
                }
            }
        });

        top_bar_tv_5.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != top_bar_tv_5_clickListener) {
                    top_bar_tv_5_clickListener.top_bar_tv_5_click(v);
                }
            }
        });

        top_bar_tv_title.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(null != top_bar_tv_title_clickListener) {
                    top_bar_tv_title_clickListener.top_bar_tv_title_click(v);
                }
            }
        });
    }
}
