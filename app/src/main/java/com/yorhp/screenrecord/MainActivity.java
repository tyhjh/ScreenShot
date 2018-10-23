package com.yorhp.screenrecord;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.yorhp.recordlibrary.OnScreenShotListener;
import com.yorhp.recordlibrary.ScreenRecordUtil;

import permison.FloatWindowManager;

public class MainActivity extends AppCompatActivity {

    ImageView iv_pre;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_pre = findViewById(R.id.iv_pre);

        //申请悬浮窗权限
        FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this);

        ScreenRecordUtil.getInstance().screenShot(this, new OnScreenShotListener() {
            @Override
            public void screenShot() {
                iv_pre.setImageBitmap(ScreenRecordUtil.getInstance().getScreenShot());
            }
        });

        iv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startService(new Intent(MainActivity.this, MyService.class));
                moveTaskToBack(true);
            }
        });


    }
}
