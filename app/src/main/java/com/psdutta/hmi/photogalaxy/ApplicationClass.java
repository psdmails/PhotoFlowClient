package com.psdutta.hmi.photogalaxy;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

public class ApplicationClass extends Application {
    private static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        startService();
    }

    void startService() {
        Intent it = new Intent(this, GalaxyClientService.class);
        startService(it);
    }

    public static Context getContext(){
        return mContext;
    }
}
