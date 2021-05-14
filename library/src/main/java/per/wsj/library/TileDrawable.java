package per.wsj.library;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class TileDrawable extends BaseDrawable {

    private Drawable mDrawable;
    private int mTileCount = -1;

    public TileDrawable(Drawable drawable) {
        mDrawable = drawable;
    }

    public Drawable getDrawable() {
        return mDrawable;
    }

    public int getTileCount() {
        return mTileCount;
    }

    public void setTileCount(int tileCount) {
        mTileCount = tileCount;
        invalidateSelf();
    }

    @NonNull
    @Override
    public Drawable mutate() {
        mDrawable = mDrawable.mutate();
        return this;
    }

    @Override
    protected void onDraw(Canvas canvas, int width, int height) {

        mDrawable.setAlpha(mAlpha);
        ColorFilter colorFilter = getColorFilterForDrawing();
        if (colorFilter != null) {
            mDrawable.setColorFilter(colorFilter);
        }

        int tileHeight = mDrawable.getIntrinsicHeight();
        // 计算view高度和图标高度比例
        float scale = (float) height / tileHeight;
        // 将画布缩放成图标高度的坐标系统
        canvas.scale(scale, scale);

        float scaledWidth = width / scale;
        if (mTileCount < 0) {
            int tileWidth = mDrawable.getIntrinsicWidth();
            for (int x = 0; x < scaledWidth; x += tileWidth) {
                mDrawable.setBounds(x, 0, x + tileWidth, tileHeight);
                mDrawable.draw(canvas);
            }
        } else {
            // 整个宽度均分成几份
            float tileWidth = scaledWidth / mTileCount;
            for (int i = 0; i < mTileCount; ++i) {
                int drawableWidth = mDrawable.getIntrinsicWidth();
                // 找到每一份的中间位置
                float tileCenter = tileWidth * (i + 0.5f);
                float halfDrawableWidth = (float) drawableWidth / 2;
                mDrawable.setBounds(Math.round(tileCenter - halfDrawableWidth), 0,
                        Math.round(tileCenter + halfDrawableWidth), tileHeight);
                mDrawable.draw(canvas);
            }
        }
    }
}
