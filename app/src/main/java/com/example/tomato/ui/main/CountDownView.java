package com.example.tomato.ui.main;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;
import com.example.tomato.R;

import androidx.annotation.Nullable;

public class CountDownView extends View {

    private int mOuterColor = Color.RED;
    private int mInnerColor = Color.BLUE;
    private int mBorderWidth = 20 ; // 此处的20为20px
    private int mTimeTextSize;
    private int mTimeTextColor;
    private int mTotalTime = 200;
    private int mCurrentTime = 100;


    private Paint mOuterPaint, mInnerPaint, mTextPaint;

    public CountDownView(Context context) {
        this(context, null);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CountDownView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        // 在自定义View中获取自定义属性
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.CountDownView);
        mInnerColor = array.getColor(R.styleable.CountDownView_innerColor, mInnerColor);
        mOuterColor = array.getColor(R.styleable.CountDownView_outerColor, mOuterColor);
        mBorderWidth = (int)array.getDimension(R.styleable.CountDownView_borderWidth, mBorderWidth);
        mTimeTextColor = array.getColor(R.styleable.CountDownView_timeTextColor, mTimeTextColor);
        mTimeTextSize = array.getDimensionPixelSize(R.styleable.CountDownView_timeTextSize, mTimeTextSize);
        array.recycle();

        // 初始化画笔
        mOuterPaint = new Paint();
        mOuterPaint.setAntiAlias(true);
        mOuterPaint.setColor(mOuterColor);
        mOuterPaint.setStrokeWidth(mBorderWidth);
        mOuterPaint.setStyle(Paint.Style.STROKE);
        mOuterPaint.setStrokeCap(Paint.Cap.ROUND);

        mInnerPaint = new Paint();
        mInnerPaint.setAntiAlias(true);
        mInnerPaint.setColor(mInnerColor);
        mInnerPaint.setStrokeWidth(mBorderWidth);
        mInnerPaint.setStyle(Paint.Style.STROKE);
        mInnerPaint.setStrokeCap(Paint.Cap.ROUND);

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(mTimeTextColor);
        mTextPaint.setTextSize(mTimeTextSize);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        // 宽度高度不一致时，取最小值，保证是一个正方形
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        setMeasuredDimension(width>height?height:width, width>height?height:width);
    }

    // 画内外圆弧并显示剩余时间
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画外圆
        int center = getWidth()/2;
        int radius = getHeight()/2 - mBorderWidth;
        RectF rectf = new RectF(center - radius, center - radius, center + radius, center + radius);
        canvas.drawArc(rectf, 270, 360, false, mOuterPaint);


        // 画内圆弧
        if (mTotalTime == 0) return ;

        float sweepAngle = (float) mCurrentTime/mTotalTime;
        canvas.drawArc(rectf, 272, sweepAngle*360, false, mInnerPaint);

        // 显示剩余时间
        int timeLeft = (mTotalTime - mCurrentTime)/1000;
        String timeText;
        int minute = timeLeft/60;
        int second = timeLeft%60;
        if( minute == 0 ) {
            if(second < 10) {
                timeText = "00:0" + second;
            } else {
                timeText = "00:" + second;
            }
        } else if (minute < 10) {
            if(second < 10) {
                timeText = "0" + minute + ":0" + second;
            } else {
                timeText = "0" + minute + ":" + second;
            }
        } else {
            if(second < 10) {
                timeText = minute + ":0" + second;
            } else {
                timeText = minute + ":" + second;
            }
        }

        Rect textBounds = new Rect();
        mTextPaint.getTextBounds(timeText, 0, timeText.length(), textBounds);
        int dx = getWidth()/2 - textBounds.width()/2;
        // 基线
        Paint.FontMetricsInt fontMetricsInt = mTextPaint.getFontMetricsInt();
        int dy = (fontMetricsInt.bottom - fontMetricsInt.top)/2 - fontMetricsInt.bottom;
        int baseLine = getHeight()/2 + dy;
        canvas.drawText(timeText, dx, baseLine, mTextPaint);
    }

    public synchronized void setTotalTime (int totalTime) {
        this.mTotalTime = totalTime;
    }

    public synchronized void setCurrentTime (int currentTime) {
        this.mCurrentTime = currentTime;
        // 反复绘制
        invalidate();
    }

}
