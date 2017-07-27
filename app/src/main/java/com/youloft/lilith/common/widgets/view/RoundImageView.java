package com.youloft.lilith.common.widgets.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import com.youloft.lilith.R;

/**
 * Desc: 可以控制圆角的Imageview
 * Change:
 *
 * @author zchao created at 2017/7/5 15:49
 * @see
 */
public class RoundImageView extends AppCompatImageView {
    private float mRadius, mLeftTop, mRightTop, mLeftBottom, mRightBottom = 0;

    private Context context;               //圆角大小
    private Paint mPaint;

    public RoundImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initStyleable(attrs);
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_IN));


        resetMaskPath();
    }

    private void initStyleable(AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.RoundImageView);
        mRadius = typedArray.getDimension(R.styleable.RoundImageView_radius, 0);
        mLeftTop = typedArray.getDimension(R.styleable.RoundImageView_radius_left_top, mRadius);
        mLeftBottom = typedArray.getDimension(R.styleable.RoundImageView_radius_left_bottom, mRadius);
        mRightBottom = typedArray.getDimension(R.styleable.RoundImageView_radius_right_bottom, mRadius);
        mRightTop = typedArray.getDimension(R.styleable.RoundImageView_radius_right_top, mRadius);
        typedArray.recycle();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (w != 0 && h != 0) {
            resetMaskPath();
        }
    }

    @Override
    public void draw(Canvas canvas) {
        canvas.saveLayer(0, 0, getWidth(), getHeight(), null, Canvas.ALL_SAVE_FLAG);
        super.draw(canvas);
        canvas.drawPath(mClipPath, mPaint);
        canvas.restore();
    }


    private Path mClipPath = new Path();
    private RectF mClipRect = new RectF();

    private void resetMaskPath() {
        mClipRect.set(0, 0, getWidth(), getHeight());
        mClipPath.reset();
        mClipPath.addRoundRect(mClipRect, new float[]{mLeftTop, mLeftTop, mRightTop, mRightTop, mRightBottom, mRightBottom, mLeftBottom, mLeftBottom}, Path.Direction.CCW);
    }
}
