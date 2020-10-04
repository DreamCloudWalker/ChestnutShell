package com.dengjian.chestnutshell.highlevelui.particleexplode;

import android.graphics.Canvas;
import android.graphics.Paint;

public abstract class Particle {
    int color;
    float cx;
    float cy;

    public Particle(int color, float cx, float cy) {
        this.color = color;
        this.cx = cx;
        this.cy = cy;
    }

    // 根据动画进度计算粒子位置
    protected abstract void calculate(float factor);
    protected abstract void draw(Canvas canvas, Paint paint);

    public void advance(Canvas canvas, Paint paint, float factor) {
        calculate(factor);
        draw(canvas, paint);
    }
}
