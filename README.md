# Android截屏工具

标签（空格分隔）： Android

---

> 原文链接：https://www.jianshu.com/p/8a428fb45098

有时候会用到颜色拾取器这样的东西来查看屏幕上的颜色值，一直是用Pixolor这个软件来看颜色的；很方便，点哪里显示哪里，也没有延迟，以为是什么黑科技；我注意到一个细节，如果只是切换屏幕，颜色拾取器不会更新，只有移动拾取器才更新选中；可以确定是截屏来实现的了，那就简单了，截屏获取像素点的颜色值就好了


用到截屏，网上看了一下，大概分为保存View为图像和调用录屏服务来截屏，录屏是比较好的办法，可以在APP外截屏，所以简单的封装了一下

###集成方法：

Step 1. Add the JitPack repository to your build file
```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}

```
Step 2. Add the dependency
```
dependencies {
	        implementation 'com.github.tyhjh:ScreenShot:v1.0.0'
	}
```
### 简单使用

主要分为两步，第一步是开启录屏；第二步就可以直接获取截屏，返回Bitmap
截图的过程录屏是开启的，录屏开启就可以进行截屏，操作完需要关闭录屏
截屏过程很快，效果很好

```java
//第一次会自动申请录屏权限
ScreenRecordUtil.getInstance().screenShot(this, new OnScreenShotListener() {
            @Override
            public void screenShot() {
            //可以获取截图，可以多次调用
            iv_pre.setImageBitmap(ScreenRecordUtil.getInstance().getScreenShot());
            //最后关闭录屏服务
            ScreenRecordUtil.getInstance().destroy();
            }
        });
```
如果是APP外截屏则开启悬浮窗服务，可以通过操作悬浮窗进行截屏

#### 参考文章：[Android 截屏方式整理](https://www.jianshu.com/p/63e29dc43a69)

### 截屏实现代码

1.初始化一个`MediaProjectionManager`

```java
MediaProjectionManager mMediaProjectionManager = (MediaProjectionManager) activity.getSystemService(Context.MEDIA_PROJECTION_SERVICE);
```
2.创建并启动`Intent`
```java
startActivityForResult(mMediaProjectionManager.createScreenCaptureIntent(),REQUEST_MEDIA_PROJECTION);
```
3.在`onActivityResult`中拿到MediaProjection
```java
mMediaProjection = mMediaProjectionManager.getMediaProjection(resultCode, data);
```
4.设置VirtualDisplay将图像和展示的View关联起来。一般来说我们会将图像展示到SurfaceView，这里为了为了便于拿到截图，我们使用ImageReader，他内置有SurfaceView。
```java
mVirtualDisplay = mMediaProjection.createVirtualDisplay("screen-mirror",
mScreenWidth, mScreenHeight, mScreenDensity,
DisplayManager.VIRTUAL_DISPLAY_FLAG_AUTO_MIRROR,
mImageReader.getSurface(), null, null);
```

5.通过ImageReader拿到截图
```java
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
```
6.注意截屏之后要及时关闭VirtualDisplay ，因为VirtualDisplay 是十分消耗内存和电量的。
```java
if (mVirtualDisplay == null) {
            return;
}
mVirtualDisplay.release();
mVirtualDisplay = null;

```

#### 项目地址：https://github.com/tyhjh/ScreenShot