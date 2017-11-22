package com.apollo.discounthunter.widgets;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Toast;

import com.apollo.discounthunter.R;

import java.lang.ref.WeakReference;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by wangpengbo on 2017/10/20.
 */

public class ClockView extends View {
    private Context mContext;
    private Paint mPaint;

    private float mDiscRadius = 250;//表盘半径
    private int mDiscColor = Color.BLACK;//表盘颜色
    private float mPointRadius = 10;//圆心小点半径
    private int mPointColor = Color.WHITE;//小圆点颜色
    private float mSecondRotate = 0;//秒针旋转度数
    private float mMinuteRotate = 0;//分针旋转度数
    private float mHourRotate = 0;//时针旋转度数
    private float mEdgeWidth = 20;//表盘边缘宽度
    private float mDiscLongWidth = 4;//长刻度的宽度
    private float mDiscLongLength = 50;//长刻度的长度
    private int mDiscLongColor = Color.WHITE;//长刻度的颜色
    private float mDiscShortWidth = 2;//短刻度的宽度
    private float mDiscShortLength = 25;//短刻度的长度
    private int mDiscShortColor = Color.WHITE;//短刻度的颜色
    private int mTextFont = 30;//数字大小
    private int mTextColor = Color.WHITE;//数字颜色
    private float mHourWidth = 10;//时针宽度
    private float mHourLength = mDiscRadius / 2;//时针长度
    private int mHourColor = Color.WHITE;//时针颜色
    private float mMinuteWidth = 10;//分针宽度
    private float mMinuteLength = mDiscRadius / 3 * 2;//分针长度
    private int mMinuteColor = Color.WHITE;//分针颜色
    private float mSecondWidth = 5;//秒针宽度
    private float mSecondLength = mDiscRadius / 5 * 4;//秒针长度
    private int mSecondColor = Color.RED;//秒针颜色

    private Timer mTimer = new Timer();
    private TimerTask mTask = new TimerTask() {
        @Override
        public void run() {
            if (mSecondRotate == 360) {
                mSecondRotate = 0;
            }
            mSecondRotate += 6;

            if (mMinuteRotate == 360) {
                mMinuteRotate = 0;
            }
            mMinuteRotate += 6f / 60;//1s 分针走动秒针的60分之一角度

            if (mHourRotate == 360) {
                mHourRotate = 0;
            }
            mHourRotate += 6f / 60 / 60;//1s 时针走动分针的60分之一角度

            postInvalidate();//调用 ondraw 刷新视图，在子线程用该方法，主线程 invalidate
        }
    };

    /**
     * 获取当前时间
     */
    public void getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date(System.currentTimeMillis());
        String time = format.format(date);
        String[] times = time.split(":");
        int hour = 0;
        int minute = 0;
        int second = 0;
        try {
            hour = Integer.valueOf(times[0]);
            minute = Integer.valueOf(times[1]);
            second = Integer.valueOf(times[2]);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        setTime(hour, minute, second);

    }

    /**
     * 设置时间
     *
     * @param hour   小时
     * @param minute 分钟
     * @param second 秒
     */
    public void setTime(int hour, int minute, int second) {
        if (hour >= 24 || hour < 0 || minute >= 60 || minute < 0 || second >= 60 || second < 0) {
            Toast.makeText(mContext, "输入时间不合法，请重新输入!", Toast.LENGTH_SHORT).show();
            return;
        }
        //以0点0分0秒为基准
        //计算秒角度
        float secondDegree = 6f * second;
        //计算分钟角度
        float minuteDegree = 6f * (minute + second * 1f / 60);
        //计算小时角度
        float hourDegree = 30f * (hour + minute * 1f / 60 + second * 1f / 60 / 60);

        mSecondRotate = secondDegree % 360;
        mMinuteRotate = minuteDegree % 360;
        mHourRotate = hourDegree % 360;

        invalidate();
    }

    /**
     * 开始走时间
     */
    public void startTime() {
        mTimer.schedule(mTask, 0, 1000);
        getCurrentTime();
    }

    public ClockView(Context context) {
        super(context);
        mContext = new WeakReference<>(context).get();
        initPaint();
    }


    public ClockView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = new WeakReference<>(context).get();

        //获取自定义属性
        TypedArray typedArray = mContext.obtainStyledAttributes(attrs, R.styleable.ClockView);//属性集合
        for (int i = 0; i < typedArray.getIndexCount(); i++) {
            int attr = typedArray.getIndex(i);
            switch (attr) {
                case R.styleable.ClockView_discRadius:
                    mDiscRadius = typedArray.getInt(attr, 250);
                    //求得圆盘半径后，根据圆盘半径按比例求时分秒针长度
                    mHourLength = mDiscRadius / 2;
                    mMinuteLength = mDiscRadius / 3 * 2;
                    mSecondLength = mDiscRadius / 5 * 4;
                    break;
                case R.styleable.ClockView_pointRadius:
                    mPointRadius = typedArray.getInt(attr, 10);
                    break;
                case R.styleable.ClockView_edgeWidth:
                    mEdgeWidth = typedArray.getInt(attr, 20);
                    break;
                case R.styleable.ClockView_discColor:
                    mDiscColor = typedArray.getColor(attr, Color.BLACK);
                    break;
                case R.styleable.ClockView_pointColor:
                    mPointColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ClockView_discLongWidth:
                    mDiscLongWidth = typedArray.getInt(attr, 4);
                    break;
                case R.styleable.ClockView_discLongLength:
                    mDiscLongLength = typedArray.getInt(attr, 50);
                    break;
                case R.styleable.ClockView_discLongColor:
                    mDiscLongColor = typedArray.getColor(attr, Color.WHITE);
                case R.styleable.ClockView_discShortWidth:
                    mDiscShortWidth = typedArray.getInt(attr, 2);
                    break;
                case R.styleable.ClockView_discShortLength:
                    mDiscShortLength = typedArray.getInt(attr, 25);
                    break;
                case R.styleable.ClockView_discShortColor:
                    mDiscShortColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ClockView_textFont:
                    mTextFont = typedArray.getInt(attr, 30);
                    break;
                case R.styleable.ClockView_textColor:
                    mTextColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ClockView_hourWidth:
                    mHourWidth = typedArray.getInt(attr, 10);
                    break;
                case R.styleable.ClockView_hourLength:
                    mHourLength = typedArray.getInt(attr, (int) (mDiscRadius * 1f / 2));
                    break;
                case R.styleable.ClockView_hourColor:
                    mHourColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ClockView_minuteWidth:
                    mMinuteWidth = typedArray.getInt(attr, 10);
                    break;
                case R.styleable.ClockView_minuteLength:
                    mMinuteLength = typedArray.getInt(attr, (int) (mDiscRadius * 1f / 3 * 2));
                    break;
                case R.styleable.ClockView_minuteColor:
                    mMinuteColor = typedArray.getColor(attr, Color.WHITE);
                    break;
                case R.styleable.ClockView_secondWidth:
                    mSecondWidth = typedArray.getInt(attr, 5);
                    break;
                case R.styleable.ClockView_secondLength:
                    mSecondLength = typedArray.getInt(attr, (int) (mDiscRadius * 1f / 5 * 4));
                    break;
                case R.styleable.ClockView_secondColor:
                    mSecondColor = typedArray.getColor(attr, Color.RED);
                    break;
            }
        }
        typedArray.recycle();

        initPaint();

    }

    /**
     * 初始化画笔
     */
    private void initPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.BLACK);
//        mPaint.setStyle(Paint.Style.STROKE);//圆环宽度
//        mPaint.setStrokeWidth(5);//圆环宽度


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(measureWidth(widthMeasureSpec), measurHeight(heightMeasureSpec));
    }

    /**
     * 自定义测量控件宽度方法
     *
     * @param measureSpec 测量模式
     */
    private int measureWidth(int measureSpec) {
        int result;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {//layout 里有具体的宽度值
            result = size;
        } else {
            result = (int) ((mDiscRadius + mEdgeWidth) * 2);//控件宽度等于圆盘直径加边缘直径
            if (mode == MeasureSpec.AT_MOST) {//layout 里 宽度wrapcontent
                result = Math.min(result, size);
            }
        }
        return result;
    }

    /**
     * 自定义测量控件高度方法
     *
     * @param measureSpec 测量模式
     */
    private int measurHeight(int measureSpec) {
        int result;
        int size = MeasureSpec.getSize(measureSpec);
        int mode = MeasureSpec.getMode(measureSpec);
        if (mode == MeasureSpec.EXACTLY) {//layout 里有具体的宽度值
            result = size;
        } else {
            result = (int) ((mDiscRadius + mEdgeWidth) * 2);
            if (mode == MeasureSpec.AT_MOST) {//layout 里 宽度wrapcontent
                result = Math.min(result, size);
            }
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //1.画表盘
        drawDisc(canvas);
        //画表盘外圈
        drawDiscEdge(canvas);
        //2.画圆心
        drawPoint(canvas);
//        //3.设置刻度线宽度
//        mPaint.setStrokeWidth(2);
        //4.移动画布到圆心
        canvas.translate(getWidth() / 2, getHeight() / 2);
        //5.画刻度线
        drawScale(canvas);
        //6.画数字
        drawNumber(canvas);
        //7.画时针
        drawHour(canvas);
        //8.画分针
        drawMinute(canvas);
        //9.画秒针
        drawSecond(canvas);

    }

    /**
     * 画表盘边缘
     *
     * @param canvas 画布
     */
    private void drawDiscEdge(Canvas canvas) {
        canvas.save();
        mPaint.setColor(mContext.getResources().getColor(R.color.richGold));
        mPaint.setStrokeWidth(mEdgeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDiscRadius + mEdgeWidth / 2, mPaint);

        canvas.restore();
    }

    /**
     * 画秒针
     *
     * @param canvas 画布
     */
    private void drawSecond(Canvas canvas) {
        canvas.save();
        mPaint.setColor(mSecondColor);
        mPaint.setStrokeWidth(mSecondWidth);
        canvas.rotate(mSecondRotate);
        canvas.drawLine(0, 0, 0, -mSecondLength, mPaint);
        canvas.restore();
    }

    /**
     * 画分针
     *
     * @param canvas 画布
     */
    private void drawMinute(Canvas canvas) {
        canvas.save();
        mPaint.setStrokeWidth(mMinuteWidth);
        mPaint.setColor(mMinuteColor);
        canvas.rotate(mMinuteRotate);
        canvas.drawLine(0, 0, 0, -mMinuteLength, mPaint);
        canvas.restore();
    }

    /**
     * 画时针
     *
     * @param canvas 画布
     */
    private void drawHour(Canvas canvas) {
        canvas.save();
        mPaint.setStrokeWidth(mHourWidth);
        mPaint.setColor(mHourColor);
        canvas.rotate(mHourRotate);
        canvas.drawLine(0, 0, 0, -mHourLength, mPaint);
        canvas.restore();
    }

    /**
     * 画数字
     *
     * @param canvas 画布
     */
    private void drawNumber(Canvas canvas) {
        canvas.save();
        mPaint.setTextSize(mTextFont);
        mPaint.setColor(mTextColor);
        mPaint.setStrokeWidth(3);
        Rect textBounds = new Rect();//创建一个矩形，将文字放入其中，用于测量文字宽高
        for (int i = 0; i < 12; i++) {
            if (i == 0) {
//                mPaint.getTextBounds("12", 0, "12".length(), textBounds);
//                int offset = textBounds.width() / 2;
//                canvas.drawText("12", -offset, -(mDiscRadius - 80), mPaint);//为了使文字居中，左移 offset 量
//                canvas.rotate(30);
                drawNumberText(canvas, 30 * i, "12", mPaint);
            } else {
//                mPaint.getTextBounds(i + "", 0, (i + "").length(), textBounds);
//                int offset = textBounds.width() / 2;
//                canvas.drawText(i + "", -offset, -(mDiscRadius - 80), mPaint);
//                canvas.rotate(30);
                drawNumberText(canvas, 30 * i, i + "", mPaint);
            }
        }

        canvas.restore();

    }

    /**
     * 画校正角度的数字
     *
     * @param canvas 画布
     * @param degree 旋转角度
     * @param text   画的文字
     * @param paint  画笔
     */
    private void drawNumberText(Canvas canvas, float degree, String text, Paint paint) {
        //以画1点的1字为例：
        //1.画布旋转30度，2.移动画笔中心到字体中心坐标，3.画布旋转-30度，4.画数字，5.旋转画布30度，6.移动画笔中心到圆心,7.复位画布角度到0度
        Rect textBounds = new Rect();
        mPaint.getTextBounds(text, 0, text.length(), textBounds);
        int xOffset = textBounds.width() / 2;
        int yOffset = textBounds.height() / 2;

        canvas.rotate(degree);
        canvas.translate(0, (80 - mDiscRadius));
        canvas.rotate(-degree);
        canvas.drawText(text, -xOffset, yOffset, mPaint);//画数字，起始点x，y 坐标位于文字中心向左 xoffset，向下 yoffset 处
        canvas.rotate(degree);
        canvas.translate(0, (mDiscRadius - 80));
        canvas.rotate(-degree);
    }

    /**
     * 画刻度线
     *
     * @param canvas 画布
     */
    private void drawScale(Canvas canvas) {
        canvas.save();//因为复用画笔，颜色不同，所以在每次画完一个颜色前 save下，画完后restore，相当于在 save 和 restore 之间画完的是在不同图层上画形状，最后合成一张图
        int scaleLength;//刻度长度
        for (int i = 0; i < 60; i++) {
            if (i % 5 == 0) {//长刻度
                scaleLength = (int) mDiscLongLength;
                mPaint.setStrokeWidth(mDiscLongWidth);
                mPaint.setColor(mDiscLongColor);
            } else {//短刻度
                scaleLength = (int) mDiscShortLength;
                mPaint.setStrokeWidth(mDiscShortWidth);
                mPaint.setColor(mDiscShortColor);
            }
            canvas.drawLine(mDiscRadius - scaleLength, 0, mDiscRadius, 0, mPaint);
            canvas.rotate(6);
        }
        canvas.restore();
    }

    /**
     * 画圆心
     *
     * @param canvas 画布
     */
    private void drawPoint(Canvas canvas) {
        canvas.save();
        mPaint.setColor(mPointColor);
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mPointRadius, mPaint);
        canvas.restore();
    }

    /**
     * 画表盘
     *
     * @param canvas 画布
     */
    private void drawDisc(Canvas canvas) {
        canvas.save();
        mPaint.setColor(mDiscColor);
        mPaint.setStyle(Paint.Style.FILL);//圆饼
        canvas.drawCircle(getWidth() / 2, getHeight() / 2, mDiscRadius, mPaint);
        canvas.restore();
    }


}
