package per.wsj.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;

import androidx.appcompat.content.res.AppCompatResources;

public class RattingAttr {

    private Context mContext;

    /**
     * star number
     */
    private int starCount;

    private int bgDrawable;

    private int starDrawable;

    private ColorStateList bgColor;

    private ColorStateList secondaryStarColor;

    private ColorStateList starColor;

    private boolean keepOriginColor;

    public RattingAttr(Context context, int starCount, int bgDrawable, int starDrawable, ColorStateList bgColor, ColorStateList secondaryStarColor, ColorStateList starColor, boolean keepOriginColor) {
        this.mContext = context;
        this.starCount = starCount;
        this.bgDrawable = bgDrawable;
        this.starDrawable = starDrawable;
        this.keepOriginColor = keepOriginColor;
        this.bgColor = bgColor;
        this.secondaryStarColor = secondaryStarColor;
        this.starColor = starColor;
    }

    public Drawable[] getLayerList() {
        return new Drawable[]{createLayerDrawableWithTintAttrRes(bgDrawable, R.attr.colorControlHighlight, keepOriginColor),
                createClippedLayerDrawableWithTintColor(starDrawable, Color.TRANSPARENT),
                createClippedLayerDrawableWithTintAttrRes(starDrawable, R.attr.colorControlActivated, keepOriginColor)};
    }

    /**
     * create background drawable
     *
     * @param tileRes
     * @param tintAttrRes
     * @param mKeepOriginColor
     * @return
     */
    private Drawable createLayerDrawableWithTintAttrRes(int tileRes, int tintAttrRes, boolean mKeepOriginColor) {
        int tintColor = -1;
        if (!mKeepOriginColor) {
            tintColor = getColorFromAttrRes(tintAttrRes);
        }
        return createLayerDrawableWithTintColor(tileRes, tintColor);
    }

    /**
     * create secondaryProgress drawable
     *
     * @param tileResId
     * @param tintColor
     * @return
     */
    @SuppressLint("RtlHardcoded")
    private Drawable createClippedLayerDrawableWithTintColor(int tileResId, int tintColor) {
        return new ClipDrawable(createLayerDrawableWithTintColor(tileResId, tintColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
    }

    private Drawable createLayerDrawableWithTintColor(int tileRes, int tintColor) {
        TileDrawable drawable = new TileDrawable(AppCompatResources.getDrawable(mContext,
                tileRes));
        drawable.mutate();
        if (tintColor != -1) {
            drawable.setTint(tintColor);
        }
        return drawable;
    }

    @SuppressLint("RtlHardcoded")
    private Drawable createClippedLayerDrawableWithTintAttrRes(int tileResId,
                                                               int tintAttrRes, boolean mKeepOriginColor) {
        return new ClipDrawable(createLayerDrawableWithTintAttrRes(tileResId, tintAttrRes,
                mKeepOriginColor), Gravity.LEFT, ClipDrawable.HORIZONTAL);
    }

    private int getColorFromAttrRes(int attrRes) {
        TypedArray a = mContext.obtainStyledAttributes(new int[]{attrRes});
        try {
            return a.getColor(0, 0);
        } finally {
            a.recycle();
        }
    }

    public int getStarCount() {
        return starCount;
    }

    public void setStarCount(int starCount) {
        this.starCount = starCount;
    }

    public int getBgDrawable() {
        return bgDrawable;
    }

    public void setBgDrawable(int bgDrawable) {
        this.bgDrawable = bgDrawable;
    }

    public int getStarDrawable() {
        return starDrawable;
    }

    public void setStarDrawable(int starDrawable) {
        this.starDrawable = starDrawable;
    }

    public boolean isKeepOriginColor() {
        return keepOriginColor;
    }

    public void setKeepOriginColor(boolean keepOriginColor) {
        this.keepOriginColor = keepOriginColor;
    }

    public ColorStateList getBgColor() {
        return bgColor;
    }

    public void setBgColor(ColorStateList bgColor) {
        this.bgColor = bgColor;
    }

    public ColorStateList getSecondaryStarColor() {
        return secondaryStarColor;
    }

    public void setSecondaryStarColor(ColorStateList secondaryStarColor) {
        this.secondaryStarColor = secondaryStarColor;
    }

    public ColorStateList getStarColor() {
        return starColor;
    }

    public void setStarColor(ColorStateList starColor) {
        this.starColor = starColor;
    }
}
