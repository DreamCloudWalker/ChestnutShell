package com.dengjian.chestnutshell.highlevelui.particleexplode;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.dengjian.common.utils.BitmapUtil;
import com.dengjian.common.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class ExplosionView extends View {
    private final List<ExplosionAnimator> mExplosionAnimators;
    private ParticleFactory mParticleFactory;
    private OnClickListener onClickListener;

    public ExplosionView(Context context) {
        super(context);
        mExplosionAnimators = new ArrayList<>();
        attachToActivity(); // 把动画放到所有View的最上层防止被其他View压盖
    }

    private void attachToActivity() {
        ViewGroup decorView = (ViewGroup) ((Activity) getContext()).getWindow().getDecorView();

        // 动画场景覆盖全屏
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        decorView.addView(this, layoutParams);
    }

    private OnClickListener getOnClickListener() {
        if (null == onClickListener) {
            onClickListener = new OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 触发先震动后爆炸
                    shake(v);
                }
            };
        }

        return onClickListener;
    }

    private void shake(View view) {
        if (view.getVisibility() != View.VISIBLE || view.getAlpha() != 1) {
            return ;
        }
        final Rect rect = new Rect();

        // 震动
        ValueAnimator animator = ValueAnimator.ofFloat(0, 1).setDuration(150);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                // 横竖向随机一个值震动
                view.setTranslationX(Utils.RANDOM.nextFloat() - 0.5f * view.getWidth() * 0.05f);
                view.setTranslationY(Utils.RANDOM.nextFloat() - 0.5f * view.getHeight() * 0.05f);
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                view.setTranslationX(0);
                view.setTranslationY(0);
                // 获取控件在屏幕的坐标
                view.getGlobalVisibleRect(rect);
                explode(view, rect);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }

    private void explode(View view, Rect rect) {
        ExplosionAnimator explosionAnimator = new ExplosionAnimator(mParticleFactory, this,
                BitmapUtil.createBitmapFromView(view), rect);
        mExplosionAnimators.add(explosionAnimator);
        explosionAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.animate().setDuration(150).scaleX(1).scaleY(1).alpha(1).start();
            }

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                view.animate().setDuration(150).scaleX(0).scaleY(0).alpha(0).start();
            }
        });
        explosionAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        for (ExplosionAnimator explosionAnimator : mExplosionAnimators) {
            explosionAnimator.draw(canvas);
        }
        super.onDraw(canvas);
    }

    public void setParticleFactory(ParticleFactory factory) {
        mParticleFactory = factory;
    }

    public void addListener(View view) {
        view.setOnClickListener(getOnClickListener());
    }
}
