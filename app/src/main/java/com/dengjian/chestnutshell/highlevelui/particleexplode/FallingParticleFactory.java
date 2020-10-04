package com.dengjian.chestnutshell.highlevelui.particleexplode;

import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Rect;

import java.util.ArrayList;
import java.util.List;

public class FallingParticleFactory extends ParticleFactory {
    public static final float PART_WH = 10.0f;

    @Override
    public Particle[] generateParticles(Bitmap bitmap, Rect rect) {
        int w = rect.width();
        int h = rect.height();

        // 横向个数
        int partWCnt = (int) (w / PART_WH);
        int partHCnt = (int) (h / PART_WH);
        partWCnt = partWCnt > 0 ? partWCnt : 1;
        partHCnt = partHCnt > 0 ? partHCnt : 1;

        int bitmapPartW = bitmap.getWidth() / partWCnt;
        int bitmapPartH = bitmap.getHeight() / partHCnt;

        List<Particle> particleArray = new ArrayList<>();
        for (int row = 0; row < partHCnt; row++) {
            for (int column = 0; column < partWCnt; column++) {
                // 获取图片中像素的颜色
                int color = bitmap.getPixel(column * bitmapPartW, row * bitmapPartH);
                if (0 == Color.alpha(color)) {
                    continue;
                }
                float x = rect.left + PART_WH * column;
                float y = rect.top + PART_WH * row;
                // 关联粒子对象
                particleArray.add(new FallingParticle(color, (int) x, (int) y, rect));
            }
        }
        Particle[] particles = new Particle[particleArray.size()];
        return particleArray.toArray(particles);
    }
}
