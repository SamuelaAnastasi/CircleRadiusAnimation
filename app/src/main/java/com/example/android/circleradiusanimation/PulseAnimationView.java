package com.example.android.circleradiusanimation;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by the_insider on 22/12/2017.
 */

public class PulseAnimationView extends View {

    // Constants
    private static final int COLOR_ADJUSTER = 5;
    private final static int ANIMATION_DURATION = 4000;
    private final static long ANIMATION_DELAY = 1000;

    // Member field
    private float mRadius;
    private float mX, mY;
    private final Paint mPaint = new Paint();
    private AnimatorSet mPulseAnimatorSet = new AnimatorSet();
    public PulseAnimationView(Context context) {
        this(context, null);
    }

    public PulseAnimationView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public void setRadius(float mRadius) {
        this.mRadius = mRadius;
        mPaint.setColor(Color.GREEN + (int) mRadius / COLOR_ADJUSTER);
        invalidate();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // First gorw animator
        ObjectAnimator growAnimator = ObjectAnimator.ofFloat(this,
                "radius", 0, getWidth());
        growAnimator.setDuration(ANIMATION_DURATION);
        growAnimator.setInterpolator(new LinearInterpolator());

        // Second shrink animator
        ObjectAnimator shrinkAnimator = ObjectAnimator.ofFloat(this,
                "radius", getWidth(), 0);
        shrinkAnimator.setDuration(ANIMATION_DURATION);
        shrinkAnimator.setInterpolator(new LinearOutSlowInInterpolator());
        shrinkAnimator.setStartDelay(ANIMATION_DELAY);

        // Third repeat animator
        ObjectAnimator repeatAnimator = ObjectAnimator.ofFloat(this,
                "radius", 0, getWidth());
        repeatAnimator.setStartDelay(ANIMATION_DELAY);
        repeatAnimator.setDuration(ANIMATION_DURATION);
        repeatAnimator.setRepeatCount(1);
        repeatAnimator.setRepeatMode(ValueAnimator.REVERSE);
        mPulseAnimatorSet.play(growAnimator).before(shrinkAnimator);
        mPulseAnimatorSet.play(repeatAnimator).after(shrinkAnimator);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(mX, mY, mRadius, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mX = event.getX();
            mY = event.getY();
        }
        if(mPulseAnimatorSet != null && mPulseAnimatorSet.isRunning()) {
            mPulseAnimatorSet.cancel();
        }
        mPulseAnimatorSet.start();
        return super.onTouchEvent(event);
    }

}
