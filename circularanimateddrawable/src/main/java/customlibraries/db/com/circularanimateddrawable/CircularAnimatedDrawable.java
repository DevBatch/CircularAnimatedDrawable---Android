package customlibraries.db.com.circularanimateddrawable;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Animatable;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;


public class CircularAnimatedDrawable extends Drawable implements Animatable {
    private static final Interpolator ANGLE_INTERPOLATOR = new LinearInterpolator();
    private static final Interpolator SWEEP_INTERPOLATOR = new DecelerateInterpolator();
    private static final int ANGLE_ANIMATOR_DURATION = 2000;
    private static final int SWEEP_ANIMATOR_DURATION = 900;
    private static final Float MIN_SWEEP_ANGLE = 30f;
    private final RectF fBounds = new RectF();
    private ValueAnimator mValueAnimatorAngle;
    private ValueAnimator mValueAnimatorSweep;
    private Paint mPaint;
    private View mAnimatedView;

    private float mBorderWidth;
    private float mCurrentGlobalAngle;
    private float mCurrentSweepAngle;
    private float mCurrentGlobalAngleOffset;

    private boolean mModeAppearing;
    private boolean mRunning;

    /**
     * Constructor with Params
     *
     * @param view        Specifies the View that will be animated
     * @param borderWidth Specifies Spining Bar width
     * @param arcColor    Specifies Spining Bar color
     * @see #setupAnimations()
     */
    public CircularAnimatedDrawable(View view, float borderWidth, int arcColor) {

        mAnimatedView = view;
        mBorderWidth = borderWidth;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(borderWidth);
        mPaint.setColor(arcColor);

        setupAnimations();
    }

    /**
     * To Initialize and Declare all types of animations
     * There are two kinds of animations: Sweep animation and Global angle animation.
     *
     * @see #setCurrentGlobalAngle(float)
     * @see #setCurrentSweepAngle(float)
     */
    private void setupAnimations() {
        mValueAnimatorSweep = ValueAnimator.ofFloat(0, 360f - 2 * MIN_SWEEP_ANGLE);
        mValueAnimatorSweep.setInterpolator(SWEEP_INTERPOLATOR);
        mValueAnimatorSweep.setDuration(SWEEP_ANIMATOR_DURATION);
        mValueAnimatorSweep.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimatorSweep.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationRepeat(Animator animation) {
                toggleAppearingMode();
            }
        });
        mValueAnimatorSweep.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurrentSweepAngle((float) animation.getAnimatedValue());
                mAnimatedView.invalidate();
            }
        });
        mValueAnimatorAngle = ValueAnimator.ofFloat(0, 360f);
        mValueAnimatorAngle.setInterpolator(ANGLE_INTERPOLATOR);
        mValueAnimatorAngle.setDuration(ANGLE_ANIMATOR_DURATION);
        mValueAnimatorAngle.setRepeatCount(ValueAnimator.INFINITE);
        mValueAnimatorAngle.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setCurrentGlobalAngle((float) animation.getAnimatedValue());
                mAnimatedView.invalidate();
            }
        });
    }

    /**
     * Specifies the spinning Bar's Global Angle
     *
     * @param currentGlobalAngle
     */
    public void setCurrentGlobalAngle(float currentGlobalAngle) {
        mCurrentGlobalAngle = currentGlobalAngle;
        invalidateSelf();
    }

    /**
     * This method is used to grow and contract sweep that is used by animation,
     * Every time an animation is performed ,this method gets called.
     */
    private void toggleAppearingMode() {
        mModeAppearing = !mModeAppearing;
        if (mModeAppearing) {
            mCurrentGlobalAngleOffset = (mCurrentGlobalAngleOffset + MIN_SWEEP_ANGLE * 2) % 360;
        }
    }

    /**
     * This method is used to perform fancy loading animation, It is used to specify the existing angle of sweep.
     *
     * @param currentSweepAngle
     */
    public void setCurrentSweepAngle(float currentSweepAngle) {
        mCurrentSweepAngle = currentSweepAngle;
        invalidateSelf();
    }

    /**
     * Starts the animation
     */
    @Override
    public void start() {
        if (isRunning()) {
            return;
        }
        mRunning = true;
        mValueAnimatorAngle.start();
        mValueAnimatorSweep.start();
    }

    /**
     * Stops the animation
     */
    @Override
    public void stop() {
        if (!isRunning()) {
            return;
        }

        mRunning = false;
        mValueAnimatorAngle.cancel();
        mValueAnimatorSweep.cancel();
    }

    /**
     * Method that informs, if the animation is in process or not
     *
     * @return boolean if the animation is running or not running
     */
    @Override
    public boolean isRunning() {
        return mRunning;
    }

    /**
     * Method called when the drawable is going to draw itself.
     *
     * @param canvas
     */
    @Override
    public void draw(Canvas canvas) {
        float startAngle = mCurrentGlobalAngle - mCurrentGlobalAngleOffset;
        float sweepAngle = mCurrentSweepAngle;
        if (!mModeAppearing) {
            startAngle = startAngle + sweepAngle;
            sweepAngle = 360 - sweepAngle - MIN_SWEEP_ANGLE;
        } else {
            sweepAngle += MIN_SWEEP_ANGLE;
        }

        canvas.drawArc(fBounds, startAngle, sweepAngle, false, mPaint);
    }

    /**
     * Specify an alpha value for the drawable
     *
     * @param alpha value for drawable
     */
    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    /**
     * Specify an optional color filter for the drawable.
     * <p>
     * The color filter to apply.
     */
    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    /**
     * Return the opacity/transparency of this Drawable
     */
    @Override
    public int getOpacity() {
        return PixelFormat.TRANSPARENT;
    }

    /**
     * Changes appearance based on the bounds.
     */
    @Override
    protected void onBoundsChange(Rect bounds) {
        super.onBoundsChange(bounds);
        fBounds.left = bounds.left + mBorderWidth / 2f + .5f;
        fBounds.right = bounds.right - mBorderWidth / 2f - .5f;
        fBounds.top = bounds.top + mBorderWidth / 2f + .5f;
        fBounds.bottom = bounds.bottom - mBorderWidth / 2f - .5f;
    }

    /**
     * Stops all changes applied and stops all types of animations and free the resources. Passes null to the Value Animators.
     */
    public void dispose() {
        if (mValueAnimatorAngle != null) {
            mValueAnimatorAngle.end();
            mValueAnimatorAngle.removeAllUpdateListeners();
            mValueAnimatorAngle.cancel();
        }

        mValueAnimatorAngle = null;

        if (mValueAnimatorSweep != null) {
            mValueAnimatorSweep.end();
            mValueAnimatorSweep.removeAllUpdateListeners();
            mValueAnimatorSweep.cancel();
        }

        mValueAnimatorSweep = null;
    }
}
