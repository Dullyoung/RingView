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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

/*
 * Created by　Dullyoung on 2021/1/20
 */
public class RingView extends ConstraintLayout {
    public RingView(@NonNull Context context) {
        super(context);
    }

    int[] colors;
    float[] percents;
    int ringWidths;

    public void setData(int[] colors, float[] percents, int ringWidth) {
        this.colors = colors;
        this.percents = percents;
        this.ringWidths = ringWidth;
        init();
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

    private float gteLastEnd(int index) {
        return getTotalPercent(index - 1);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        RectF oval = new RectF(ringWidths, ringWidths, getWidth() - ringWidths, getHeight() - ringWidths);
        //当所有的都绘制完了 就一次性把所有的绘制出来 不在做动画
        if (currentAngle >= 360) {
            if (currentColorIndex > 0) {
                for (int i = 0; i <= currentColorIndex; i++) {
                    canvas.drawArc(oval, gteLastEnd(i), getTotalPercent(i) - gteLastEnd(i), false, mPaints[i]);
                }
            }
        }


        if (currentAngle < getTotalPercent(currentColorIndex)) {
            //动态绘制新的圆环
            for (int i = 0; i <= currentColorIndex; i++) {
                canvas.drawArc(oval, gteLastEnd(currentColorIndex),
                        currentAngle - gteLastEnd(currentColorIndex), false, mPaints[currentColorIndex]);
            }

            //绘制之前已经动画走完的圆环
            if (currentColorIndex > 0) {
                for (int i = 0; i < currentColorIndex; i++) {
                    canvas.drawArc(oval, gteLastEnd(i),
                            getTotalPercent(i) - gteLastEnd(i), false, mPaints[i]);
                }
            }
        } else {//这个百分比的弧度绘制完了 下标+1继续绘制下一个
            currentColorIndex++;
            if (currentColorIndex >= percents.length) {
                currentColorIndex = percents.length - 1;
            }
        }
    }

    int currentColorIndex = 0;

    private int currentAngle = 0;

    public void startAnim(int duration) {
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
