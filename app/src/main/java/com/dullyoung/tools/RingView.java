package com.dullyoung.tools;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Xfermode;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/*
 * Created by　Dullyoung on 2021/1/20
 */
public class RingView extends ConstraintLayout {
    public RingView(@NonNull Context context, int radius) {
        super(context);
        setBackgroundColor(Color.TRANSPARENT);
        this.radius = radius;
    }

    private int radius;
    private int[] colors;
    private float[] percents;
    private int ringWidths;

    /**
     * @param colors    颜色
     * @param percents  占比
     * @param ringWidth 圆环宽度 单位px
     */
    public void setData(int[] colors, float[] percents, int ringWidth) {
        this.colors = colors;
        this.percents = percents;
        this.ringWidths = ringWidth;
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setRadius(radius);
    }

    public void setRadius(int radius) {
        ViewGroup.LayoutParams layoutParams = getLayoutParams();
        layoutParams.width = radius;
        layoutParams.height = radius;
        setLayoutParams(layoutParams);
    }


    Paint[] mPaints;

    private void init() {
        if (colors == null || percents == null ||
                colors.length == 0 || percents.length == 0 || percents.length != colors.length) {
            Toast.makeText(getContext(), "颜色和百分比未设置或两者数量不相等", Toast.LENGTH_SHORT).show();
            return;
        }
        mPaints = new Paint[colors.length];
        for (int i = 0; i < colors.length; i++) {
            mPaints[i] = new Paint();
            mPaints[i].setColor(colors[i]);
            mPaints[i].setAntiAlias(true);//抗锯齿功能
            mPaints[i].setStrokeWidth(ringWidths);//设置画笔宽度
            mPaints[i].setStrokeCap(Paint.Cap.BUTT);
            mPaints[i].setStyle(Paint.Style.STROKE);//设置填充样式
        }
    }

    /**
     * @param index 当前模块的坐标
     * @return 到当前模块结束总共占据多数度 返回度数
     */
    private float getTotalPercent(int index) {
        if (index < 0) {
            return 0;
        }
        float range = 0;
        for (int i = index; i >= 0; i--) {
            range = range + percents[i];
        }
        return range * 360;
    }

    /**
     * @param index 当前模块的坐标
     * @return 上一个模块绘制的终点  即这个模块绘制的起点
     */
    private float getLastEnd(int index) {
        return getTotalPercent(index - 1);
    }


    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        RectF oval = new RectF(ringWidths, ringWidths, getWidth() - ringWidths, getHeight() - ringWidths);

        for (int i = 0; i < getCurrentCount(); i++) {
            canvas.drawArc(oval, getLastEnd(i), currentAngle - getLastEnd(i), false, mPaints[i]);
        }

    }

    /**
     * @return 当前绘制到了第几个模块
     */
    private int getCurrentCount() {
        int count = 0;
        for (int i = 0; i < percents.length; i++) {
            if (currentAngle > getTotalPercent(i - 1)) {
                count++;
            }
        }
        return count;
    }


    private int currentAngle = 0;

    public void setDurationAndStartAnim(int duration) {
        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, 360);
        valueAnimator.setDuration(duration);
        valueAnimator.addUpdateListener(animation -> {
            currentAngle = (int) animation.getAnimatedValue();
            if (currentAngle >= 360) {
                invalidate();
                return;
            }
            postInvalidate();
        });
        valueAnimator.start();
    }


}
