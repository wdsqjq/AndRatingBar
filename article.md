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
**1.1  宽度计算**

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



**1.2  高度计算**

`RatingBar`没有自己计算高度直接调用`getMeasuredHeight()` ，因此高度的计算是在父类中完成。

父类`AdbSeekBar`的`onMeasure()`实现如下：

```java
@Override
protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    // 注意此处并没有调用super.onMeasure();
	Drawable d = getCurrentDrawable();

	int thumbHeight = mThumb == null ? 0 : mThumb.getIntrinsicHeight();
	int dw = 0;
	int dh = 0;
	if (d != null) {
		dw = Math.max(mMinWidth, Math.min(mMaxWidth, d.getIntrinsicWidth()));
        // getIntrinsicHeight()获取到的是drawable的原始高度
		dh = Math.max(mMinHeight, Math.min(mMaxHeight, d.getIntrinsicHeight()));
		dh = Math.max(thumbHeight, dh);
	}
	dw += mPaddingLeft + mPaddingRight;
	dh += mPaddingTop + mPaddingBottom;

	setMeasuredDimension(resolveSizeAndState(dw, widthMeasureSpec, 0),
			resolveSizeAndState(dh, heightMeasureSpec, 0));
}
```

因此父类`AdbSeekBar`是根据drawable的原始高度进行计算的。

**1.3  总结**

`RatingBar`的宽高度只跟图片的大小相关，不能根据设置的宽高自适应。那么这些图片的宽高是多少呢？

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









二，AndRatingBar原理