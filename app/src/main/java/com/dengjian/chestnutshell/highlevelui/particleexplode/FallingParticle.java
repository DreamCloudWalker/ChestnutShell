package com.dengjian.chestnutshell.highlevelui.particleexplode;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.dengjian.common.utils.Utils;

public class FallingParticle extends Particle {
    private final Rect mBound;
    private float mRadius = FallingParticleFactory.PART_WH;
    private float mAlpha = 1.0f;

    public FallingParticle(int color, int cx, int cy, Rect bound) {
        super(color, cx, cy);
        mBound = bound;
    }

    @Override
    protected void calculate(float factor) {
        cx = cx + factor * Utils.RANDOM.nextInt(mBound.width()) * (Utils.RANDOM.nextFloat() * 2f - 1f);
        cy = cy + factor * Utils.RANDOM.nextInt(mBound.height() / 2);

        mRadius = mRadius - factor * Utils.RANDOM.nextInt(2);
        mAlpha = (1.0f - factor) * (1 + Utils.RANDOM.nextFloat());
    }

    @Override
    protected void draw(Canvas canvas, Paint paint) {
        paint.setColor(color);
        paint.setAlpha((int) (Color.alpha(color) * mAlpha));
        canvas.drawCircle(cx, cy, mRadius, paint);
    }
}
