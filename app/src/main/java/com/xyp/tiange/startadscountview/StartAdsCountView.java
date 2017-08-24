package com.xyp.tiange.startadscountview;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

/**
 * User: xyp
 * Date: 2017/8/23
 * Time: 16:01
 * 开机广告倒计时
 */

public class StartAdsCountView extends View {
    private Paint circlePaint;
    private Paint fontPaint;
    private Path pathCircle;
    private PathMeasure pathMeasure;
    private int measureW, measureH;
    private ValueAnimator circleAni;
    private float animationValue;
    private float fontWidth, fontHeight;

    //attr属性
    private int circleRadius;
    private float fontSize;
    private int circleColor, circleProgressColor, fontColor;
    private int aniDuration;
    private String text;

    public StartAdsCountView(Context context) {
        this(context, null);
    }

    public StartAdsCountView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StartAdsCountView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs);
        init();
        initAni();
    }

    private void initAttrs(Context context, AttributeSet attrs){
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StartAdsCountView);
        circleRadius = typedArray.getInteger(R.styleable.StartAdsCountView_circleRadius, 100);
        circleProgressColor = typedArray.getColor(R.styleable.StartAdsCountView_circleProgressColor, Color.WHITE);
        circleColor = typedArray.getColor(R.styleable.StartAdsCountView_circleColor, Color.WHITE);
        fontSize = typedArray.getDimension(R.styleable.StartAdsCountView_fontSize, 16);
        fontColor = typedArray.getColor(R.styleable.StartAdsCountView_fontColor, Color.WHITE);
        aniDuration = typedArray.getInteger(R.styleable.StartAdsCountView_aniDuration, 3000);
        text = typedArray.getString(R.styleable.StartAdsCountView_text);
        typedArray.recycle();
    }

    private void init() {
        fontPaint = new Paint();
        fontPaint.setAntiAlias(true);
        fontPaint.setColor(fontColor);
        fontPaint.setTextSize(fontSize);
        Rect rect = new Rect();
        fontPaint.getTextBounds(text, 0, text.length(), rect);
        fontWidth = rect.width();
        fontHeight = rect.height();

        circlePaint = new Paint();
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setStrokeWidth(8);
        circlePaint.setAntiAlias(true);
        //画笔结束点圆角
        circlePaint.setStrokeCap(Paint.Cap.ROUND);

        pathCircle = new Path();
        pathMeasure = new PathMeasure();
        pathCircle.addArc(-circleRadius, -circleRadius, circleRadius, circleRadius, -90, 359.9f);
        //测量圆环
        pathMeasure.setPath(pathCircle, false);
    }

    private void initAni() {
        circleAni = ValueAnimator.ofFloat(0, 1);
        circleAni.setDuration(aniDuration);
        circleAni.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                animationValue = (float) animation.getAnimatedValue();
                Log.d("xyp", "animationValue:"+animationValue);
                invalidate();
            }
        });


        circleAni.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(listener != null)
                    listener.onFinish();
            }
        });


    }

    public void startAni(){
        if(circleAni != null)
            circleAni.start();
    }

    public void stopAni(){
        //必须先滞空监听事件
        listener = null;

        if(circleAni != null){
            circleAni.cancel();
            circleAni = null;
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        measureW = w;
        measureH = h;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //把android坐标系（左上角为原点）移动为数学坐标系
        canvas.translate(measureW / 2, measureH / 2);
        canvas.drawText(text, -(fontWidth / 2), fontHeight / 2, fontPaint);
        circlePaint.setColor(circleColor);
        canvas.drawPath(pathCircle, circlePaint);

        pathMeasure.setPath(pathCircle, false);
        Path dst = new Path();
        pathMeasure.getSegment(pathMeasure.getLength() * animationValue, pathMeasure.getLength(), dst, true);
        circlePaint.setColor(circleProgressColor);
        canvas.drawPath(dst, circlePaint);

    }

    /**
     * 倒计时结束回调事件
     */
    public interface CountFinishListener{
        void onFinish();
    }

    private CountFinishListener listener;

    public void setCountFinishListener(CountFinishListener listener){
        this.listener = listener;
    }

}
