package com.youloft.lilith.topic.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import com.youloft.lilith.common.utils.ViewUtil;

/**
 *
 */

public class VoteView extends View {
    private Paint mPaintLeft1;
    private Paint mPaintRight1;
    private Paint mPaintArc;
    private int centerX;
    private int centerY;

    public VoteView(Context context) {
        super(context);
        initPaint();
    }

    public VoteView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        centerX = getWidth()/2;
        centerY = getWidth()/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float leftX = ViewUtil.dp2px(55);
        float radius1 = ViewUtil.dp2px(35);
        float bRadius = ViewUtil.dp2px(42);
        float rightX = ViewUtil.dp2px(220);
        float dp15 = ViewUtil.dp2px(15);
        float dp5 = ViewUtil.dp2px(5);
        int length = (int) ViewUtil.dp2px(130);
        Point pLeftCenter = new Point();
        pLeftCenter.x = centerX - length;pLeftCenter.y = centerY;
        Point pLeftTop = new Point();
        pLeftTop.x = (int) (pLeftCenter.x + bRadius * sin(15) );
        pLeftTop.y = (int) (centerY - bRadius * cos(15) );
        Point pLeftBottom = new Point();
        pLeftBottom.x = (int) (pLeftTop.x );
        pLeftBottom.y = (int) (centerY + bRadius * cos(15) );
        Point pControlLeftTop = new Point();
        pControlLeftTop.x = (int) (pLeftCenter.x - (0.5/0.375) *  bRadius * cos(15) - 25);
        int bDistanceY = (int) (( pLeftBottom.x - pControlLeftTop.x) * tan(15));
        pControlLeftTop.y = pLeftTop.y - bDistanceY;
        Point pControlLeftBottom = new Point();
        pControlLeftBottom.x = pControlLeftTop.x;
        pControlLeftBottom.y = pLeftBottom.y + bDistanceY;
        Path bPath = new Path();
        bPath.moveTo(pLeftTop.x,pLeftTop.y);
        bPath.cubicTo(pControlLeftTop.x,pControlLeftTop.y,pControlLeftBottom.x,pControlLeftBottom.y,pLeftBottom.x,pLeftBottom.y);
        canvas.drawPath(bPath,mPaintArc);
        canvas.drawCircle(pLeftCenter.x,pLeftCenter.y,bRadius,mPaintLeft1);



    }

    public void initPaint() {
        mPaintLeft1 = new Paint();
        mPaintRight1 = new Paint();
        mPaintArc = new Paint();
        mPaintLeft1.setColor(Color.parseColor("#df815b"));
        mPaintRight1.setColor(Color.parseColor("#3470b4"));
        mPaintArc.setColor(Color.parseColor("#ffffff"));
        mPaintArc.setStyle(Paint.Style.STROKE);
        mPaintArc.setAntiAlias(true);
        mPaintLeft1.setAntiAlias(true);
        mPaintLeft1.setStyle(Paint.Style.STROKE);

    }

    private double sqrt(double num) {
        return Math.sqrt(num * Math.PI/180);
    }
    private double sin(double num) {
        return  Math.sin(num * Math.PI/180);
    }

    private double cos(double num) {
        return Math.cos(num * Math.PI/180);
    }
    private double tan(double num) {
        return Math.tan(15 * Math.PI/180);
    }

}
