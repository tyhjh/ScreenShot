package com.yorhp.screenrecord.app;

import android.app.Application;
import android.os.Environment;

import java.io.File;

public class MyApplication extends Application {

    public static String baseDir = Environment.getExternalStorageDirectory() + "/ASceenUtil/";

    @Override
    public void onCreate() {
        super.onCreate();
        initDir();
    }

    void initDir() {
        File file = new File(baseDir);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

}
