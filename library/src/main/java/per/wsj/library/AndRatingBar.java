/*
 * Copyright (c) 2016 Zhang Hai <Dreaming.in.Code.ZH@Gmail.com>
 * All Rights Reserved.
 */

package per.wsj.library;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.RatingBar;

import androidx.appcompat.widget.TintTypedArray;

/**
 * Custom RatingBar
 *
 * @author shiju.wang
 * @date 2020-01-09
 */

// AppCompatRatingBar will add undesired measuring behavior.
@SuppressLint("AppCompatCustomView")
public class AndRatingBar extends RatingBar {

    /**
     * color of rating star
     */
    private ColorStateList mStarColor;

    /**
     * color of secondary rating star
     */
    private ColorStateList mSubStarColor;

    /**
     * background color of all star
     */
    private ColorStateList mBgColor;

    /**
     * customize star drawable
     */
    private int mStarDrawable;

    /**
     * customize background drawable
     */
    private int mBgDrawable;

    /**
     * if keep the origin color of star drawable
     */
    private boolean mKeepOriginColor;

    private StarDrawable mDrawable;

    /**
     * event listener
     */
    private OnRatingChangeListener mOnRatingChangeListener;

    private float mTempRating;

    public AndRatingBar(Context context) {
        this(context, null);
    }

    public AndRatingBar(Context context, AttributeSet attrs) {
        // notice:can't use this(context, attrs, 0); because ratingbar has it's own defStyleAttr
        super(context, attrs);
        init(context, attrs, 0);
    }

    public AndRatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs, defStyleAttr);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        TintTypedArray a = TintTypedArray.obtainStyledAttributes(context, attrs,
                R.styleable.AndRatingBar, defStyleAttr, 0);
        if (a.hasValue(R.styleable.AndRatingBar_starColor)) {
            mStarColor = a.getColorStateList(
                    R.styleable.AndRatingBar_starColor);
        }

        if (a.hasValue(R.styleable.AndRatingBar_subStarColor)) {
            mSubStarColor = a.getColorStateList(
                    R.styleable.AndRatingBar_subStarColor);
        }

        if (a.hasValue(R.styleable.AndRatingBar_bgColor)) {
            mBgColor = a.getColorStateList(
                    R.styleable.AndRatingBar_bgColor);
        }

        mKeepOriginColor = a.getBoolean(R.styleable.AndRatingBar_keepOriginColor,false);

        // get customize drawable
        mStarDrawable = a.getResourceId(R.styleable.AndRatingBar_starDrawable, R.drawable.ic_rating_star_solid);
        if (a.hasValue(R.styleable.AndRatingBar_bgDrawable)) {
            mBgDrawable = a.getResourceId(R.styleable.AndRatingBar_bgDrawable, R.drawable.ic_rating_star_solid);
        } else {
            mBgDrawable = mStarDrawable;
        }

        a.recycle();

        mDrawable = new StarDrawable(context, mStarDrawable, mBgDrawable, mKeepOriginColor);
        mDrawable.setStarCount(getNumStars());
        setProgressDrawable(mDrawable);
    }

    @Override
    public void setNumStars(int numStars) {
        super.setNumStars(numStars);
        if (mDrawable != null) {
            mDrawable.setStarCount(numStars);
        }
    }

    @Override
    protected synchronized void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = getMeasuredHeight();
        int width = Math.round(height * mDrawable.getTileRatio() * getNumStars());
        setMeasuredDimension(View.resolveSizeAndState(width, widthMeasureSpec, 0), height);
    }

    @Override
    public void setProgressDrawable(Drawable d) {
        super.setProgressDrawable(d);
        applyProgressTints();
    }

    private void applyProgressTints() {
        if (getProgressDrawable() == null) {
            return;
        }
        applyPrimaryProgressTint();
        applyProgressBackgroundTint();
        applySecondaryProgressTint();
    }

    private void applyPrimaryProgressTint() {
        if (mStarColor != null) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.progress, true);
            if (target != null) {
                applyTintForDrawable(target, mStarColor);
            }
        }
    }

    private void applySecondaryProgressTint() {
        if (mSubStarColor != null) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.secondaryProgress,
                    false);
            if (target != null) {
                applyTintForDrawable(target, mSubStarColor);
            }
        }
    }

    private void applyProgressBackgroundTint() {
        if (mBgColor != null) {
            Drawable target = getTintTargetFromProgressDrawable(android.R.id.background, false);
            if (target != null) {
                applyTintForDrawable(target, mBgColor);
            }
        }
    }

    private Drawable getTintTargetFromProgressDrawable(int layerId, boolean shouldFallback) {
        Drawable progressDrawable = getProgressDrawable();
        if (progressDrawable == null) {
            return null;
        }
        progressDrawable.mutate();
        Drawable layerDrawable = null;
        if (progressDrawable instanceof LayerDrawable) {
            layerDrawable = ((LayerDrawable) progressDrawable).findDrawableByLayerId(layerId);
        }
        if (layerDrawable == null && shouldFallback) {
            layerDrawable = progressDrawable;
        }
        return layerDrawable;
    }

    // Progress drawables in this library has already rewritten tint related methods for
    // compatibility.
    @SuppressLint("NewApi")
    private void applyTintForDrawable(Drawable drawable, ColorStateList tintList) {
        if (tintList != null) {
            if (drawable instanceof BaseDrawable) {
                drawable.setTintList(tintList);
            } else {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    drawable.setTintList(tintList);
                }
            }
            // The drawable (or one of its children) may not have been
            // stateful before applying the tint, so let's try again.
            if (drawable.isStateful()) {
                drawable.setState(getDrawableState());
            }
        }
    }

    /**
     * Get the listener that is listening for rating change events.
     *
     * @return The listener, may be null.
     */
    public OnRatingChangeListener getOnRatingChangeListener() {
        return mOnRatingChangeListener;
    }

    /**
     * Sets the listener to be called when the rating changes.
     *
     * @param listener The listener.
     */
    public void setOnRatingChangeListener(OnRatingChangeListener listener) {
        mOnRatingChangeListener = listener;
    }

    @Override
    public synchronized void setSecondaryProgress(int secondaryProgress) {
        super.setSecondaryProgress(secondaryProgress);

        // HACK: Check and call our listener here because this method is always called by
        // updateSecondaryProgress() from onProgressRefresh().
        float rating = getRating();
        if (mOnRatingChangeListener != null && rating != mTempRating) {
            mOnRatingChangeListener.onRatingChanged(this, rating);
        }
        mTempRating = rating;
    }

    /**
     * A callback that notifies clients when the rating has been changed. This includes changes that
     * were initiated by the user through a touch gesture or arrow key/trackball as well as changes
     * that were initiated programmatically. This callback <strong>will</strong> be called
     * continuously while the user is dragging, different from framework's
     * {@link OnRatingBarChangeListener}.
     */
    public interface OnRatingChangeListener {

        /**
         * Notification that the rating has changed. This <strong>will</strong> be called
         * continuously while the user is dragging, different from framework's
         * {@link OnRatingBarChangeListener}.
         *
         * @param ratingBar The RatingBar whose rating has changed.
         * @param rating    The current rating. This will be in the range 0..numStars.
         */
        void onRatingChanged(AndRatingBar ratingBar, float rating);
    }
}
