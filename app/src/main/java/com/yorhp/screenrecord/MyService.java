package com.yorhp.screenrecord;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;

import com.yorhp.recordlibrary.ScreenRecordUtil;


public class MyService extends Service {

    public static final int FLAG_LAYOUT_INSET_DECOR = 0x00000200;
    WindowManager.LayoutParams params3;
    WindowManager windowManager;
    ImageView btnView3;

    public MyService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        createWindowView();
    }

    private void createWindowView() {
        btnView3 = new ImageView(getApplicationContext());
        btnView3.setImageResource(R.drawable.ic_star);
        btnView3.setScaleType(ImageView.ScaleType.CENTER_CROP);
        windowManager = (WindowManager) getApplicationContext()
                .getSystemService(Context.WINDOW_SERVICE);

        params3 = new WindowManager.LayoutParams();

        // 设置Window Type


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            params3.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            params3.type = WindowManager.LayoutParams.TYPE_SYSTEM_ERROR;
        }


        // 设置悬浮框不可触摸
        params3.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL
                | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | FLAG_LAYOUT_INSET_DECOR;
        // 悬浮窗不可触摸，不接受任何事件,同时不影响后面的事件响应
        params3.format = PixelFormat.RGBA_8888;
        // 设置悬浮框的宽高
        params3.width = 400;
        params3.height = 400;
        params3.gravity = Gravity.TOP;
        params3.x = 300;
        params3.y = 200;
        btnView3.setOnTouchListener(new View.OnTouchListener() {

            //保存悬浮框最后位置的变量
            int lastX, lastY;
            int paramX, paramY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                        lastX = (int) event.getRawX();
                        lastY = (int) event.getRawY();
                        paramX = params3.x;
                        paramY = params3.y;
                        break;
                    case MotionEvent.ACTION_MOVE:
                        int dx = (int) event.getRawX() - lastX;
                        int dy = (int) event.getRawY() - lastY;
                        params3.x = paramX + dx;
                        params3.y = paramY + dy;
                        // 更新悬浮窗位置
                        windowManager.updateViewLayout(btnView3, params3);
                        break;
                }
                return false;
            }
        });
        windowManager.addView(btnView3, params3);
        btnView3.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                Bitmap bitmap = ScreenRecordUtil.getInstance().getScreenShot();
                btnView3.setImageBitmap(bitmap);
            }
        });
    }

}
