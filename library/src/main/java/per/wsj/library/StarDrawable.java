package per.wsj.library;

import android.annotation.SuppressLint;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;

import java.lang.reflect.Field;

public class StarDrawable extends LayerDrawable {

    public StarDrawable(RattingAttr rattingAttr) {
        super(rattingAttr.getLayerList());

        setId(0, android.R.id.background);
        setId(1, android.R.id.secondaryProgress);
        setId(2, android.R.id.progress);

        initStyle(rattingAttr);
    }

    private void initStyle(RattingAttr rattingAttr) {
        TileDrawable background = getTileDrawableByLayerId(android.R.id.background);
        TileDrawable secondaryProgress = getTileDrawableByLayerId(android.R.id.secondaryProgress);
        TileDrawable progress = getTileDrawableByLayerId(android.R.id.progress);

        background.setTileCount(rattingAttr.getStarCount());
        secondaryProgress.setTileCount(rattingAttr.getStarCount());
        progress.setTileCount(rattingAttr.getStarCount());

        if (rattingAttr.getBgColor() != null) {
            background.setTintList(rattingAttr.getBgColor());
        }
        if (rattingAttr.getSecondaryStarColor() != null) {
            secondaryProgress.setTintList(rattingAttr.getSecondaryStarColor());
        }
        if (rattingAttr.getStarColor() != null) {
            progress.setTintList(rattingAttr.getStarColor());
        }
    }

    /**
     * 获取图标宽高比
     * @return
     */
    public float getTileRatio() {
        Drawable drawable = getTileDrawableByLayerId(android.R.id.progress).getDrawable();
        return (float) drawable.getIntrinsicWidth() / drawable.getIntrinsicHeight();
    }

    public void setStarCount(int count) {
        getTileDrawableByLayerId(android.R.id.background).setTileCount(count);
        getTileDrawableByLayerId(android.R.id.secondaryProgress).setTileCount(count);
        getTileDrawableByLayerId(android.R.id.progress).setTileCount(count);
    }

    @SuppressLint("NewApi")
    private TileDrawable getTileDrawableByLayerId(int id) {
        Drawable layerDrawable = findDrawableByLayerId(id);
        switch (id) {
            case android.R.id.background:
                return (TileDrawable) layerDrawable;
            case android.R.id.secondaryProgress:
            case android.R.id.progress: {
                ClipDrawable clipDrawable = (ClipDrawable) layerDrawable;
                // fix bug:sdk<23 class ClipDrawable has no getDrawable() #8
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    return (TileDrawable) clipDrawable.getDrawable();
                } else {
                    try {
                        String fieldState = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1 ? "mState" : "mClipState";
                        Field mStateField = clipDrawable.getClass().getDeclaredField(fieldState);
                        mStateField.setAccessible(true);
                        Object clipState = mStateField.get(clipDrawable);
                        Field mDrawableField = clipState.getClass().getDeclaredField("mDrawable");
                        mDrawableField.setAccessible(true);
                        return (TileDrawable) mDrawableField.get(clipState);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            default:
                // Should never reach here.
                throw new RuntimeException();
        }
    }
}
