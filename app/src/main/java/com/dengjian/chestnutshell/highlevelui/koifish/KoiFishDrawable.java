package com.dengjian.chestnutshell.highlevelui.koifish;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class KoiFishDrawable extends Drawable {
    public static final float FISH_HEAD_RADIUS = 30;
    private static final int FISH_OTHER_ALPHA = 110; // 鱼身之外的透明度
    private static final int FISH_BODY_ALPHA = 160;

    private static final float BODY_LENGTH = 3.2f * FISH_HEAD_RADIUS;
    // ----------------鱼鳍------------------
    // 寻找鱼鳍开始点的线长
    private static final float FIND_FINS_LENGTH = 0.9f * FISH_HEAD_RADIUS;
    // 鱼鳍的长度
    private static final float FINS_LENGTH = 1.3f * FISH_HEAD_RADIUS;

    // ----------------鱼尾------------------
    // 尾部大圆的半径
    private static final float BIG_CIRCLE_RADIUS = FISH_HEAD_RADIUS * 0.7f;
    // 尾部中圆的半径
    private static final float MIDDLE_CIRCLE_RADIUS = BIG_CIRCLE_RADIUS * 0.6f;
    // 尾部小圆半径
    private static final float SMALL_CIRCLE_RADIUS = MIDDLE_CIRCLE_RADIUS * 0.4f;
    // 寻找尾部中圆圆心的线长
    private static final float FIND_MIDDLE_CIRCLE_LENGTH = BIG_CIRCLE_RADIUS + MIDDLE_CIRCLE_RADIUS;
    // 寻找尾部小圆圆心的线长
    private static final float FIND_SMALL_CIRCLE_LENGTH = MIDDLE_CIRCLE_RADIUS * (0.4f + 2.7f);
    // 寻找大三角形底边中心点的线长
    private static final float FIND_TRIANBLE_LENGTH = MIDDLE_CIRCLE_RADIUS * 2.7f;

    private final Path mPath;
    private final Paint mPaint;

    private PointF mHeadPnt;
    private PointF mMiddlePnt;
    private float mFishMainAngle = 90f;
    private float mCurrentValue = 0f;

    public KoiFishDrawable() {
        mPath = new Path();
        mPaint = new Paint();

        init();
    }

    private void init() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setARGB(FISH_OTHER_ALPHA, 244, 92, 71);
        mPaint.setAntiAlias(true);
        mPaint.setDither(true); // 防抖

        mMiddlePnt = new PointF(4.18f * FISH_HEAD_RADIUS, 4.18f * FISH_HEAD_RADIUS);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(0, 360);
        valueAnimator.setDuration(1500);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animator) {
                mCurrentValue = (float) animator.getAnimatedValue();
                invalidateSelf();
            }
        });
        valueAnimator.start();
    }

    /**
     * @param startPoint
     * @param length
     * @param angle      两点连线与x轴的夹角
     * @return
     */
    public static PointF calculatePoint(PointF startPoint, float length, float angle) {
        float deltaX = (float) (Math.cos(Math.toRadians(angle)) * length);
        float deltaY = (float) (Math.sin(Math.toRadians(angle)) * length);
        return new PointF(startPoint.x + deltaX, startPoint.y + deltaY);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        float fishAngle = (float) (mFishMainAngle + Math.sin(Math.toRadians(mCurrentValue)) * 10);

        // draw fish head
        mHeadPnt = calculatePoint(mMiddlePnt, BODY_LENGTH / 2, fishAngle);
        canvas.drawCircle(mHeadPnt.x, mHeadPnt.y, FISH_HEAD_RADIUS, mPaint);

        // draw fish fins
        PointF rightFinsPnt = calculatePoint(mHeadPnt, FIND_FINS_LENGTH, fishAngle - 110);
        makeFins(canvas, rightFinsPnt, fishAngle, true);

        PointF leftFinsPnt = calculatePoint(mHeadPnt, FIND_FINS_LENGTH, fishAngle + 110);
        makeFins(canvas, leftFinsPnt, fishAngle, false);

        // draw segment 1
        PointF bodyBottomCenterPnt = calculatePoint(mHeadPnt, BODY_LENGTH, fishAngle - 180);
        PointF middleCircleCenterPnt = makeSegment(canvas, bodyBottomCenterPnt, BIG_CIRCLE_RADIUS,
                MIDDLE_CIRCLE_RADIUS, FIND_MIDDLE_CIRCLE_LENGTH, fishAngle, true);

        // draw segment 2
        makeSegment(canvas, middleCircleCenterPnt, MIDDLE_CIRCLE_RADIUS, SMALL_CIRCLE_RADIUS,
                FIND_SMALL_CIRCLE_LENGTH, fishAngle, false);

        // draw fish tail
        makeTriangle(canvas, middleCircleCenterPnt, FIND_TRIANBLE_LENGTH, BIG_CIRCLE_RADIUS,
                fishAngle);
        makeTriangle(canvas, middleCircleCenterPnt, FIND_TRIANBLE_LENGTH - 10,
                BIG_CIRCLE_RADIUS - 20, fishAngle);

        // draw body
        makeBody(canvas, mHeadPnt, bodyBottomCenterPnt, fishAngle);
    }

    private void makeBody(Canvas canvas, PointF headPnt, PointF bodyBottomCenterPnt,
            float fishAngle) {
        // 身体的4个点
        PointF topLeftPnt = calculatePoint(headPnt, FISH_HEAD_RADIUS, fishAngle + 90);
        PointF topRightPnt = calculatePoint(headPnt, FISH_HEAD_RADIUS, fishAngle - 90);
        PointF bottomLeftPnt = calculatePoint(bodyBottomCenterPnt, BIG_CIRCLE_RADIUS,
                fishAngle + 90);
        PointF bottomRightPnt = calculatePoint(bodyBottomCenterPnt, BIG_CIRCLE_RADIUS,
                fishAngle - 90);

        // 二阶贝塞尔曲线的控制点，决定鱼的胖瘦
        PointF controlLeft = calculatePoint(headPnt, BODY_LENGTH * 0.56f, fishAngle + 130);
        PointF controlRight = calculatePoint(headPnt, BODY_LENGTH * 0.56f, fishAngle - 130);

        // draw body
        mPath.reset();
        mPath.moveTo(topLeftPnt.x, topLeftPnt.y);
        mPath.quadTo(controlLeft.x, controlLeft.y, bottomLeftPnt.x, bottomLeftPnt.y);
        mPath.lineTo(bottomRightPnt.x, bottomRightPnt.y);
        mPath.quadTo(controlRight.x, controlRight.y, topRightPnt.x, topRightPnt.y);
        mPaint.setAlpha(FISH_BODY_ALPHA);
        canvas.drawPath(mPath, mPaint);
    }

    private void makeFins(Canvas canvas, PointF startPnt, float fishAngle, boolean isRightFin) {
        float controlAngle = 115;
        PointF endPnt = calculatePoint(startPnt, FINS_LENGTH, fishAngle - 180);
        // 贝塞尔曲线控制点
        PointF controlPnt = calculatePoint(startPnt, 1.8f * FINS_LENGTH,
                isRightFin ? fishAngle - controlAngle : fishAngle + controlAngle);

        mPath.reset();
        mPath.moveTo(startPnt.x, startPnt.y);
        mPath.quadTo(controlPnt.x, controlPnt.y, endPnt.x, endPnt.y);
        canvas.drawPath(mPath, mPaint);
    }

    private PointF makeSegment(Canvas canvas, PointF bottomCenterPnt, float bigRadius,
            float smallRadius, float findSmallCircleLength, float fishAngle, boolean hasBigCircle) {
        float segmentAngle;
        if (hasBigCircle) {
            segmentAngle = (float) (mFishMainAngle + Math.cos(Math.toRadians(mCurrentValue * 2)) *
                    20);
        } else {
            segmentAngle = (float) (mFishMainAngle + Math.sin(Math.toRadians(mCurrentValue * 3)) *
                    20);
        }

        // 梯形上️底中心点
        PointF upperCenterPnt = calculatePoint(bottomCenterPnt, findSmallCircleLength,
                segmentAngle - 180);
        // 梯形的4个点
        PointF bottomLeftPnt = calculatePoint(bottomCenterPnt, bigRadius, segmentAngle + 90);
        PointF bottomRightPnt = calculatePoint(bottomCenterPnt, bigRadius, segmentAngle - 90);
        PointF upperLeftPnt = calculatePoint(upperCenterPnt, smallRadius, segmentAngle + 90);
        PointF upperRightPnt = calculatePoint(upperCenterPnt, smallRadius, segmentAngle - 90);
        if (hasBigCircle) {
            canvas.drawCircle(bottomCenterPnt.x, bottomCenterPnt.y, bigRadius, mPaint);
        }
        canvas.drawCircle(upperCenterPnt.x, upperCenterPnt.y, smallRadius, mPaint);
        // 画梯形
        mPath.reset();
        mPath.moveTo(bottomLeftPnt.x, bottomLeftPnt.y);
        mPath.lineTo(upperLeftPnt.x, upperLeftPnt.y);
        mPath.lineTo(upperRightPnt.x, upperRightPnt.y);
        mPath.lineTo(bottomRightPnt.x, bottomRightPnt.y);
        canvas.drawPath(mPath, mPaint);

        return upperCenterPnt;
    }

    private void makeTriangle(Canvas canvas, PointF startPnt, float findCenterLength,
            float findEdgeLength, float fishAngle) {
        float triangleAngle = (float) (mFishMainAngle + Math.sin(
                Math.toRadians(mCurrentValue * 3)) * 30);
        // 三角形底边中心点
        PointF centerPnt = calculatePoint(startPnt, findCenterLength, triangleAngle - 180);
        // 三角形底边的两点
        PointF leftPnt = calculatePoint(centerPnt, findEdgeLength, triangleAngle + 90);
        PointF rightPnt = calculatePoint(centerPnt, findEdgeLength, triangleAngle - 90);

        // draw triangle
        mPath.reset();
        mPath.moveTo(startPnt.x, startPnt.y);
        mPath.lineTo(leftPnt.x, leftPnt.y);
        mPath.lineTo(rightPnt.x, rightPnt.y);
        canvas.drawPath(mPath, mPaint);
    }

    @Override
    public void setAlpha(int alpha) {
        mPaint.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {
        mPaint.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return PixelFormat.TRANSLUCENT;
    }

    @Override
    public int getIntrinsicHeight() {
        return (int) (8.38f * FISH_HEAD_RADIUS); // 两倍重心到鱼尾长度
    }

    @Override
    public int getIntrinsicWidth() {
        return (int) (8.38f * FISH_HEAD_RADIUS);
    }

    public PointF getHeadPnt() {
        return mHeadPnt;
    }

    public PointF getMiddlePnt() {
        return mMiddlePnt;
    }

    public void setFishMainAngle(float fishMainAngle) {
        this.mFishMainAngle = fishMainAngle;
    }
}
