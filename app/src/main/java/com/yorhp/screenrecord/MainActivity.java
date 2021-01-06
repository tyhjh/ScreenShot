package com.yorhp.screenrecord;

import android.Manifest;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.yorhp.recordlibrary.OnScreenShotListener;
import com.yorhp.recordlibrary.ScreenShotUtil;
import com.yorhp.recordlibrary.ScreenUtil;

import permison.PermissonUtil;

public class MainActivity extends AppCompatActivity {

    ImageView iv_pre;

    private int screenRecordBitrate = 32 * 1024 * 1024;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        iv_pre = findViewById(R.id.iv_pre);

        PermissonUtil.checkPermission(this, null, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        ScreenUtil.getScreenSize(this);



        /*ScreenRecordUtil.init(ScreenUtil.SCREEN_WIDTH, ScreenUtil.SCREEN_HEIGHT, screenRecordBitrate);
        String savePath = MyApplication.baseDir + System.currentTimeMillis() + ".mp4";
        ScreenRecordUtil.getInstance().start(MainActivity.this, savePath);


        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(10 * 1000);
                    ScreenRecordUtil.getInstance().destroy();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/




        //申请悬浮窗权限
        //FloatWindowManager.getInstance().applyOrShowFloatWindow(MainActivity.this);


        iv_pre.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* startService(new Intent(MainActivity.this, MyService.class));
                moveTaskToBack(true);*/
                /**
                 * 初始化成功
                 */
                ScreenShotUtil.getInstance().screenShot(MainActivity.this, new OnScreenShotListener() {
                    @Override
                    public void screenShot(boolean success) {
                        if(success){
                            Bitmap bitmap=ScreenShotUtil.getInstance().getScreenShot();
                            iv_pre.setImageBitmap(bitmap);
                        }else {
                            Toast.makeText(MainActivity.this,"初始化失败",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }
        });


    }
}
