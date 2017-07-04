package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.VectorDrawable;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.graphics.drawable.VectorDrawableCompat;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.ViewUtil;

/**
 *
 */

public class VoteView extends View {

    private Context mContext;
    private int mCenterX,mCenterY;
    private Paint bgPaint;
    private Paint textPaint;
    private Paint leftCirclePaint;
    private Paint rightCirclePaint;
    private  Bitmap mPointBg;
    private  Bitmap leftCircle;
    private  Bitmap rightCircle;
    private int bgWidth;
    private int bgHeight;
    private int leftCircleWidth;
    private int leftCircleHeight;
    private int rightCircleWidth;
    private int rightCircleHeight;
    private int leftDistance;
    private int rightDistance;
    private int leftCenterX;
    private int rightCenterX;


    private float changeRadius1 = 00;
    private int alpha1 = 255;
    private float changeRadius2 = 00;
    private int alpha2 = 255;
    private float changeRadius3 = 00;
    private int alpha3 = 255;

    public VoteView(Context context) {
        this(context,null);
        this.mContext = context;
    }

    public VoteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        init();
    }

    private void init() {
        VectorDrawableCompat vectorDrawableCompat = VectorDrawableCompat.create(getResources(),R.drawable.topic_viewpoint_bg,mContext.getTheme());

        mPointBg = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.topic_viewpoint_bg);
        leftCircle = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.topic_point1_circle);
        rightCircle = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.topic_point2_circle);
         bgPaint = new Paint();
        textPaint = new Paint();
        leftCirclePaint = new Paint();
        rightCirclePaint = new Paint();
        textPaint.setColor(Color.parseColor("#ffffff"));
        textPaint.setTextSize(ViewUtil.sp2px(20));
        textPaint.setTextAlign(Paint.Align.CENTER);
        leftCirclePaint.setColor(Color.parseColor("#df815b"));
        leftCirclePaint.setAntiAlias(true);
        leftCirclePaint.setStyle(Paint.Style.FILL);


        rightCirclePaint.setColor(Color.parseColor("#3470b4"));
        rightCirclePaint.setAntiAlias(true);
        rightCirclePaint.setStyle(Paint.Style.FILL);


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mCenterX = getWidth()/2;
        mCenterY = getHeight()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

         bgWidth = mPointBg.getWidth();
         bgHeight = mPointBg.getHeight();
         leftCircleWidth = leftCircle.getWidth();
         leftCircleHeight = leftCircle.getHeight();
         rightCircleWidth = rightCircle.getWidth();
         rightCircleHeight = rightCircle.getHeight();
         leftDistance =(bgHeight - leftCircleHeight) / 2;
         rightDistance = (bgHeight - rightCircleHeight) / 2;
         leftCenterX = mCenterX-bgWidth/2 + leftDistance + leftCircleWidth/2;
         rightCenterX = mCenterX + bgWidth/2 - rightDistance - rightCircleWidth/2;
        canvas.drawBitmap(mPointBg,mCenterX - bgWidth/2,mCenterY - bgHeight/2,bgPaint);
        drawFirstCircle(canvas);
        drawSecondCircle(canvas);
        drawThirdCircle(canvas);
        canvas.drawBitmap(leftCircle,mCenterX-bgWidth/2 + leftDistance,mCenterY - bgHeight/2 + leftDistance,bgPaint);
        canvas.drawBitmap(rightCircle,rightCenterX - rightCircleWidth/2,mCenterY - rightCircleHeight/2,bgPaint);
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bott
        canvas.drawText("相似",leftCenterX,mCenterY-top/2-bottom/2,textPaint);
        canvas.drawText("互补",rightCenterX,mCenterY-top/2-bottom/2,textPaint);
    }
    private void drawFirstCircle(Canvas canvas) {
        leftCirclePaint.setAlpha(alpha1);
        rightCirclePaint.setAlpha(alpha1);
        canvas.drawCircle(leftCenterX,mCenterY,leftCircleWidth/2 + changeRadius1,leftCirclePaint);
        canvas.drawCircle(rightCenterX,mCenterY,rightCircleWidth/2 + changeRadius1,rightCirclePaint);
    }

    private void drawSecondCircle(Canvas canvas) {
        leftCirclePaint.setAlpha(alpha2);
        rightCirclePaint.setAlpha(alpha2);
        canvas.drawCircle(leftCenterX,mCenterY,leftCircleWidth/2 + changeRadius2,leftCirclePaint);
        canvas.drawCircle(rightCenterX,mCenterY,rightCircleWidth/2 + changeRadius2,rightCirclePaint);
    }

    private void drawThirdCircle(Canvas canvas) {
        leftCirclePaint.setAlpha(alpha3);
        rightCirclePaint.setAlpha(alpha3);
        canvas.drawCircle(leftCenterX,mCenterY,leftCircleWidth/2 + changeRadius3,leftCirclePaint);
        canvas.drawCircle(rightCenterX,mCenterY,rightCircleWidth/2 + changeRadius3,rightCirclePaint);
    }


    public void setChangeValue1(float radius1,int alpha1) {
        this.changeRadius1 = radius1;
        this.alpha1 =  alpha1;
        invalidate();
    }
    public void setChangeValue2(float radius1,int alpha1) {
        this.changeRadius2 = radius1;
        this.alpha2 =  alpha1;
        invalidate();
    }
    public void setChangeValue3(float radius1,int alpha1) {
        this.changeRadius3 = radius1;
        this.alpha3 =  alpha1;
        invalidate();
    }
}
