Android原生RatingBar为何这么难用？终极替代方案

### 一，原生RatingBar原理

先看一下`RatingBar`的继承关系

```
ProgressBar
	|_ AbsSeekBar
		|_ RatingBar
```



#### 1，测量：

先来看看`RatingBar`的`onMeasure()`

```java
@Override
protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    if (mSampleWidth > 0) {
        // 计算宽度
        final int width = mSampleWidth * mNumStars;
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
                getMeasuredHeight());
    }
}
```
**1.1  宽度测量**

宽度是通过`mSampleWidth`乘以`star`数量得来，那么`mSampleWidth`又是怎么来的呢？

`mSampleWidth`定义在父类`ProgressBar`中：

```java
// ProgressBar.java
// 定义
int mSampleWidth = 0;
// 构造方法
public ProgressBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
	
	final TypedArray a = context.obtainStyledAttributes(
			attrs, R.styleable.ProgressBar, defStyleAttr, defStyleRes);
	
	final Drawable progressDrawable = a.getDrawable(R.styleable.ProgressBar_progressDrawable);
	if (progressDrawable != null) {
		// Calling setProgressDrawable can set mMaxHeight, so make sure the
		// corresponding XML attribute for mMaxHeight is read after calling
		// this method.
		if (needsTileify(progressDrawable)) {
			setProgressDrawableTiled(progressDrawable);
		} else {
			setProgressDrawable(progressDrawable);
		}
	}
}


public void setProgressDrawableTiled(Drawable d) {
	if (d != null) {
		d = tileify(d, false);
	}

	setProgressDrawable(d);
}


private Drawable tileify(Drawable drawable, boolean clip) {
	...
	if (drawable instanceof BitmapDrawable) {
		final Drawable.ConstantState cs = drawable.getConstantState();
        // 根据Drawable获取BitmapDrawable
		final BitmapDrawable clone = (BitmapDrawable) cs.newDrawable(getResources());
		clone.setTileModeXY(Shader.TileMode.REPEAT, Shader.TileMode.CLAMP);

		if (mSampleWidth <= 0) {
            // 获取bitmap的宽度
			mSampleWidth = clone.getIntrinsicWidth();
		}
		...
	}
	return drawable;
}
```

`BitmapDrawable`中`getIntrinsicWidt()`返回的就是mBitmap根据像素密度缩放后的bitmap的宽度：

```java
// BitmapDrawable.java
@Override
public int getIntrinsicWidth() {
	return mBitmapWidth;
}

private void computeBitmapSize() {
	final Bitmap bitmap = mBitmapState.mBitmap;
	if (bitmap != null) {
		mBitmapWidth = bitmap.getScaledWidth(mTargetDensity);
		mBitmapHeight = bitmap.getScaledHeight(mTargetDensity);
	} else {
		mBitmapWidth = mBitmapHeight = -1;
	}
}
```

因此`RatingBar`的宽度只跟图片的宽度相关。



**1.2  高度测量**

`RatingBar`没有自己计算高度直接调用`getMeasuredHeight()` ，因此高度的计算是在父类中完成。

父类`AbsSeekBar`的`onMeasure()`实现如下：

```java
@Override
protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 注意此处并没有调用super.onMeasure();
	Drawable d = getCurrentDrawable();
	...
	int dh = 0;
	if (d != null) {
        ...
        // getIntrinsicHeight()获取到的是drawable的原始高度
		dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
	}
	...
	dh += mPaddingTop + mPaddingBottom;

	setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
			resolveSizeAndState(dh, heightMeasureSpec, 0));
}
```

因此父类`AdbSeekBar`是根据`drawable`的原始高度及`minHeight`，`maxHeight`进行计算的。

**1.3  小结**

`RatingBar`的宽度只跟图标的大小相关，高度和图标大小以及`minHeight`，`maxHeight`相关，不能根据设置的宽高自适应。那么这些图片的宽高是多少呢？

RatingBar提供了三种样式，可以从整个app的`theme`：`Theme.MaterialComponents.Light.DarkActionBar`开始寻找

```xml
<!-- 默认样式 -->
<style name="Base.Widget.AppCompat.RatingBar" parent="android:Widget.RatingBar">
	<item name="android:progressDrawable">@drawable/abc_ratingbar_material</item>
	<item name="android:indeterminateDrawable">@drawable/abc_ratingbar_material</item>
</style>

<!-- Indicator样式 -->
<style name="Base.Widget.AppCompat.RatingBar.Indicator" parent="android:Widget.RatingBar">
	<item name="android:progressDrawable">@drawable/abc_ratingbar_indicator_material</item>
	<item name="android:indeterminateDrawable">@drawable/abc_ratingbar_indicator_material</item>
	<item name="android:minHeight">36dp</item>
	<item name="android:maxHeight">36dp</item>
	<item name="android:isIndicator">true</item>
	<item name="android:thumb">@null</item>
</style>

<!-- Small样式 -->
<style name="Base.Widget.AppCompat.RatingBar.Small" parent="android:Widget.RatingBar">
	<item name="android:progressDrawable">@drawable/abc_ratingbar_small_material</item>
	<item name="android:indeterminateDrawable">@drawable/abc_ratingbar_small_material</item>
	<item name="android:minHeight">16dp</item>
	<item name="android:maxHeight">16dp</item>
	<item name="android:isIndicator">true</item>
	<item name="android:thumb">@null</item>
</style>
```

其中`minHeight`和`maxHeight`可以控制控件的高度，而`progressDrawable`设置的图片控制着星星的大小。他们的大小分别是48dp，36dp，16dp。

> 注意：以上测量出来的宽高度只能控制这个view的大小，并不能改变内部星星的显示大小。

#### 2，绘制：







最后，说一个比较复杂的Drawable，是进度条相关的。

**LayerDrawable**：对应`Seekbar android:progressDrawable`

通常，我们用XML定义一个进度条的ProgressDrawable是这样的，

```xml
<!--ProgressDrawable-->
<layer-list xmlns:android="http://schemas.android.com/apk/res/android">
    <item android:id="@android:id/background" android:drawable="@drawable/background"/>
    <item android:id="@android:id/secondaryProgress" android:drawable="@drawable/secondary_progress"/>
    <item android:id="@android:id/progress" android:drawable="@drawable/progress"/>
</layer-list>
```

而对于其中的，`@drawable/progress`和`@drawable/secondary_progress`也不是普通的drawable，

```xml
<!--@drawable/progress 定义-->
<clip xmlns:android="http://schemas.android.com/apk/res/android"
      android:clipOrientation="horizontal"
      android:drawable="@drawable/progress_drawable"
      android:gravity="left" >
</clip>
```

也就是说，通过XML要定义进度条的`ProgressDrawable`，我们需要定义多个XML文件的，还是比较复杂的。那么JavaCode实现呢？

其实，理解了XML实现的方式，下面的JavaCode就很好理解了。

```java
LayerDrawable layerDrawable = (LayerDrawable) getProgressDrawable();

//背景
layerDrawable.setDrawableByLayerId(android.R.id.background, backgroundDrawable);

//进度条
ClipDrawable clipProgressDrawable = new ClipDrawable(progressDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
layerDrawable.setDrawableByLayerId(android.R.id.progress, clipProgressDrawable);

//缓冲进度条
ClipDrawable clipSecondaryProgressDrawable = new ClipDrawable(secondaryProgressDrawable, Gravity.LEFT, ClipDrawable.HORIZONTAL);
layerDrawable.setDrawableByLayerId(android.R.id.secondaryProgress, clipSecondaryProgressDrawable);
```



二，AndRatingBar原理

1，测量

```java
@Override
protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    int height = getMeasuredHeight();
    int width = Math.round(height * mDrawable.getTileRatio() * getNumStars() * scaleFactor) + (int) ((getNumStars() - 1) * starSpacing);
    setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0), height);
}
```



2，构造LayerDrawable

要构造一个包含`progress`,`secondaryProgress`，`background`三个图层的drawable，其中前两个是`ClipDrawable`

















https://mp.weixin.qq.com/s?__biz=MzI1NjEwMTM4OA==&mid=2651232105&idx=1&sn=fcc4fa956f329f839f2a04793e7dd3b9&mpshare=1&scene=21&srcid=0719Nyt7J8hsr4iYwOjVPXQE#wechat_redirect