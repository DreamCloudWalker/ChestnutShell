package com.dengjian.chestnutshell.highlevelui.particleexplode;

import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class ExplosionAnimator extends ValueAnimator {
    private static final int DEFAULT_DURATION = 1000;
    private final Particle[] mParticles;
    private final ExplosionView mAnimContainer;
    private final Paint mPaint;

    public ExplosionAnimator(ParticleFactory particleFactory, ExplosionView animContainer,
            Bitmap bitmap, Rect rect) {
        this.mAnimContainer = animContainer;
        this.mParticles = particleFactory.generateParticles(bitmap, rect);
        this.mPaint = new Paint();
        setDuration(DEFAULT_DURATION);
        setFloatValues(0, 1);   // 设置起始帧
    }

    public void draw(Canvas canvas) {
        if (!isStarted()) {
            return ;
        }
        for (Particle particle: mParticles) {
            particle.advance(canvas, mPaint, (Float) getAnimatedValue());
        }
        mAnimContainer.invalidate();
    }

    @Override
    public void start() {
        super.start();
        mAnimContainer.invalidate();
    }
}
