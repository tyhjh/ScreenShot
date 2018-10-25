package com.yorhp.recordlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.hardware.display.DisplayManager;
import android.hardware.display.VirtualDisplay;
import android.media.Image;
import android.media.ImageReader;
import android.media.projection.MediaProjection;
import android.media.projection.MediaProjectionManager;
import android.os.Build;
import android.os.SystemClock;
import android.util.DisplayMetrics;
import android.view.WindowManager;

import java.nio.ByteBuffer;

import static android.app.Activity.RESULT_OK;

/**
 * 作者：Tyhj on 2018/10/22 01:20
 * 邮箱：tyhj5@qq.com
 * github：github.com/tyhjh
 * description：
 */

public class ScreenShotUtil {

    static MediaProjectionManager mMediaProjectionManager;
    static MediaProjection mMediaProjection;
    static ImageReader mImageReader;
    static VirtualDisplay mVirtualDisplay;
    static int mScreenDensity;
    static int mScreenWidth;
    static int mScreenHeight;
    WindowManager mWindowManager;

    public static boolean isInit;

    OnScreenShotListener onScreenShotListener;

    private ScreenShotUtil() {
    }

    public static ScreenShotUtil getInstance() {
        return ScreenRecordUtilHolder.screenRecord;
    }

    /**
     * 获取第一次截图
     *
     * @param activity
     * @param listener
     */
    public void screenShot(Activity activity, OnScreenShotListener listener) {
        onScreenShotListener = listener;
        init(activity);
        ScreenRecordActivity.isScrennShot=true;
        activity.startActivity(new Intent(activity, ScreenRecordActivity.class));
    }

    //获取截屏
    public Bitmap getScreenShot() {
        if (isInit)
            return startCapture();
        else
            return null;
    }

    //停止录屏
    public void destroy() {
        onScreenShotListener = null;
        if (mVirtualDisplay == null) {
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mVirtualDisplay.release();
        }
        mVirtualDisplay = null;

        if (mMediaProjection != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaProjection.stop();
            }
        }
        isInit = false;
    }

    /**
     * 初始化
     *
     * @param activity
     */
    void init(final Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
        }
        ScreenUtil.getScreenSize(activity);
        mScreenWidth = ScreenUtil.SCREEN_WIDTH;
        mScreenHeight = ScreenUtil.SCREEN_HEIGHT;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mImageReader = ImageReader.newInstance(ScreenUtil.SCREEN_WIDTH, ScreenUtil.SCREEN_HEIGHT, PixelFormat.RGBA_8888, 1);
        }
        mWindowManager = (WindowManager) activity.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        mWindowManager.getDefaultDisplay().getMetrics(metrics);
        mScreenDensity = metrics.densityDpi;
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isInit) {
                    SystemClock.sleep(10);
                }
                if (activity != null) {
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (onScreenShotListener != null)
                                onScreenShotListener.screenShot();
                        }
                    });
                }
            }
        }).start();

    }

    //返回可以开始录屏的数据
    static void permissionResult(int resultCode, Intent data) {
        if (resultCode == RESULT_OK && data != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
            }
            virtualDisplay();
        }
    }

    //开始录屏
    static void virtualDisplay() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
                    mScreenWidth, mScreenHeight, mScreenDensity, DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
                    mImageReader.getSurface(), null, null);
        }

    }

    //获取截图
    private Bitmap startCapture() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            Image image = null;
            image = mImageReader.acquireLatestImage();
            while (image == null) {
                SystemClock.sleep(10);
                image = mImageReader.acquireLatestImage();
            }
            int width = image.getWidth();
            int height = image.getHeight();
            final Image.Plane[] planes = image.getPlanes();
            final ByteBuffer buffer = planes[0].getBuffer();
            //每个像素的间距
            int pixelStride = planes[0].getPixelStride();
            //总的间距
            int rowStride = planes[0].getRowStride();
            int rowPadding = rowStride - pixelStride * width;
            Bitmap bitmap = Bitmap.createBitmap(width + rowPadding / pixelStride, height, Bitmap.Config.ARGB_8888);
            bitmap.copyPixelsFromBuffer(buffer);
            bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
            image.close();
            return bitmap;
        }
        return null;
    }

    private static class ScreenRecordUtilHolder {
        private static final ScreenShotUtil screenRecord = new ScreenShotUtil();
    }

}
