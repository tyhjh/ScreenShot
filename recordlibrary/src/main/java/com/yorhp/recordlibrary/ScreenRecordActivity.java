package com.yorhp.recordlibrary;

import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class ScreenRecordActivity extends AppCompatActivity {
    public static final int REQUEST_MEDIA_PROJECTION = 18;

    public static boolean isScrennShot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        MediaProjectionManager projectionManager = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            projectionManager = (MediaProjectionManager) getSystemService(Context.MEDIA_PROJECTION_SERVICE);
            startActivityForResult(projectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }else {
            requestCapturePermission();
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_MEDIA_PROJECTION) {
            if (isScrennShot) {
                ScreenShotUtil.getInstance().permissionResult(resultCode, data);
            } else {
                ScreenRecordUtil.getInstance().permissionResult(resultCode, data);
            }
            finish();
        }
    }

    //请求权限
    private void requestCapturePermission() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //5.0 之后才允许使用屏幕截图
            return;
        }
        if (isScrennShot) {
            startActivityForResult(ScreenShotUtil.mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        } else {
            startActivityForResult(ScreenRecordUtil.mMediaProjectionManager.createScreenCaptureIntent(), REQUEST_MEDIA_PROJECTION);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("ScreenRecordActivity：", "onDestroy");
    }
}
