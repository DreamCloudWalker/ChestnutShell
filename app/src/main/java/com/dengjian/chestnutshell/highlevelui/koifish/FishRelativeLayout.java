package com.dengjian.chestnutshell.highlevelui.koifish;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

public class FishRelativeLayout extends RelativeLayout {
    private Paint mPaint;
    private ImageView mIvFish;
    private KoiFishDrawable mFishDrawable;

    private float mTouchX = 0f;
    private float mTouchY = 0f;
    private float mRipple = 0f;
    private int mAlpha = 0;

    public FishRelativeLayout(Context context) {
        this(context, null);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FishRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        setWillNotDraw(false);

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(8);

        mIvFish = new ImageView(context);
        LayoutParams lp = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        mIvFish.setLayoutParams(lp);

        mFishDrawable = new KoiFishDrawable();
        mIvFish.setImageDrawable(mFishDrawable);
        addView(mIvFish);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (null == event) {
            return super.onTouchEvent(event);
        }
        mTouchX = event.getX();
        mTouchY = event.getY();

        // 属性动画反射调用set方法
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(this, "ripple", 0, 1f).setDuration(
                1000);
        objectAnimator.start();

        makeTrail();

        return super.onTouchEvent(event);
    }

    public void setRipple(float ripple) {
        this.mRipple = ripple;
        this.mAlpha = (int) (150 * (1 - ripple));

        invalidate();
    }

    private void makeTrail() {
        PointF fishRelativeMiddlePnt = mFishDrawable.getMiddlePnt();
        PointF fishMiddle = new PointF(mIvFish.getX() + fishRelativeMiddlePnt.x,
                mIvFish.getY() + fishRelativeMiddlePnt.y);
        // 第一个控制点
        PointF fishHead = new PointF(mIvFish.getX() + mFishDrawable.getHeadPnt().x,
                mIvFish.getY() + mFishDrawable.getHeadPnt().y);
        PointF touchPnt = new PointF(mTouchX, mTouchY);
        float angle = includedAngle(fishMiddle, fishHead, touchPnt);    // 点击点夹角
        float delta = includedAngle(fishMiddle, new PointF(fishMiddle.x + 1, fishMiddle.y),
                fishHead);
        // 控制鱼游动的贝塞尔曲线控制点
        PointF controlPnt = KoiFishDrawable.calculatePoint(fishMiddle,
                KoiFishDrawable.FISH_HEAD_RADIUS * 1.6f, angle / 2 + delta);
        Path path = new Path();
        path.moveTo(fishMiddle.x - fishRelativeMiddlePnt.x, fishMiddle.y - fishRelativeMiddlePnt.y);
        path.cubicTo(fishHead.x - fishRelativeMiddlePnt.x, fishHead.y - fishRelativeMiddlePnt.y,
                controlPnt.x - fishRelativeMiddlePnt.x, controlPnt.y - fishRelativeMiddlePnt.y,
                mTouchX - fishRelativeMiddlePnt.x, mTouchY - fishRelativeMiddlePnt.y);
        ObjectAnimator objectAnimator = ObjectAnimator.ofFloat(mIvFish, "x", "y", path);
        objectAnimator.setDuration(2000);

        PathMeasure pathMeasure = new PathMeasure(path, false);
        float[] tan = new float[2];
        objectAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float fraction = animation.getAnimatedFraction();
                pathMeasure.getPosTan(pathMeasure.getLength() * fraction, null, tan);
                float angle = (float) Math.toDegrees(Math.atan2(-tan[1], tan[0]));
                mFishDrawable.setFishMainAngle(angle);
            }
        });

        objectAnimator.start();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        mPaint.setAlpha(mAlpha);
        canvas.drawCircle(mTouchX, mTouchY, mRipple * 150, mPaint);
    }

    public static float includedAngle(PointF O, PointF A, PointF B) {
        // OA * OB = (Ax - Ox) * (Bx - Ox) + (Ay - Oy) * (B.y - O.y)
        float AOB = (A.x - O.x) * (B.x - O.x) + (A.y - O.y) * (B.y - O.y);
        float OALength = (float) Math.sqrt((A.x - O.x) * (A.x - O.x) + (A.y - O.y) * (A.y - O.y));
        float OBLength = (float) Math.sqrt((B.x - O.x) * (B.x - O.x) + (B.y - O.y) * (B.y - O.y));
        // cosAOB = (OA * OB) / (|OA| * |OB|)
        float cosAOB = AOB / (OALength * OBLength);

        float angleAOB = (float) Math.toDegrees(Math.acos(cosAOB));
        // 判断方向，通过tan相减得到点击坐标所在象限
        float direction =
                (A.y - B.y) / (A.x - B.x) - (O.y - B.y) / (O.x - B.x);// tan(BAX) - tan(BOX)
        if (0 == direction) {
            if (AOB >= 0) {
                return 0;
            } else {
                return 180;
            }
        } else {
            if (direction > 0) {    // 右侧顺时针为负
                return -angleAOB;
            } else {
                return angleAOB;
            }
        }
    }
}
