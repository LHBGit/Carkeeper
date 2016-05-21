package com.wteam.carkeeper.network;

import android.app.Application;

/**
 * Created by lhb on 2016/5/21.
 */
public class CarkeeperApplication  extends Application {
    /**
     * 当前类对象
     */
    private static CarkeeperApplication instance;

    /**
     * 对外暴露获取Application对象的接口
     * @return
     */
    public static CarkeeperApplication getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }
}
