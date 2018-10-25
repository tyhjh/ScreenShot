package com.yorhp.recordlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import com.yorhp.recordlibrary.util.ScreenRecorder;

import static android.app.Activity.RESULT_OK;

@RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
public class ScreenRecordUtil {

    static ScreenRecorder mRecorder;

    static int mScreenDensity;
    static int mRecordWidth;
    static int mRecordheight;
    static int mScreenRecordBitrate = 30;
    static String savePath;

    static MediaProjectionManager mMediaProjectionManager;

    private ScreenRecordUtil() {
    }


    public static void init(int width,int height,int screenRecordBitrate){
        mRecordWidth=width;
        mRecordheight=height;
        mScreenRecordBitrate=screenRecordBitrate;
    }


    public static ScreenRecordUtil getInstance() {
        return ScreenRecordHolder.instance;
    }

    public void start(Activity activity, String savePath) {
        ScreenRecordUtil.savePath = savePath;
        WindowManager windowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        ScreenRecordActivity.isScrennShot = false;
        activity.startActivity(new Intent(activity, ScreenRecordActivity.class));
    }


    //返回可以开始录屏的数据
    static void permissionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            MediaProjection mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            mRecorder = new ScreenRecorder(mRecordWidth, mRecordheight, mScreenRecordBitrate, mScreenDensity, mMediaProjection, savePath);
            mRecorder.start();
        }
    }


    static class ScreenRecordHolder {
        private static ScreenRecordUtil instance = new ScreenRecordUtil();
    }

    public void destroy() {
        if (mRecorder != null) {
            mRecorder.quit();
            mRecorder = null;
            mMediaProjectionManager=null;
        }
    }

    public static int getmRecordWidth() {
        return mRecordWidth;
    }

    public static void setmRecordWidth(int mRecordWidth) {
        ScreenRecordUtil.mRecordWidth = mRecordWidth;
    }

    public static int getmRecordheight() {
        return mRecordheight;
    }

    public static void setmRecordheight(int mRecordheight) {
        ScreenRecordUtil.mRecordheight = mRecordheight;
    }

    public static int getmScreenRecordBitrate() {
        return mScreenRecordBitrate;
    }

    public static void setmScreenRecordBitrate(int mScreenRecordBitrate) {
        ScreenRecordUtil.mScreenRecordBitrate = mScreenRecordBitrate;
    }
}
