package com.youloft.lilith.cons.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.LogUtil;
import com.youloft.lilith.common.utils.SafeUtil;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.bean.LuckData;
import com.youloft.lilith.cons.consmanager.ConsDrawableManager;

import java.util.ArrayList;

/**
 * Created by zchao on 2017/6/30.
 * desc: 各种运势图
 * version:
 */

public class LuckView extends View {
    private Paint mPaint, mShaderPaint;
    private TextPaint mTextPaint;
    private LuckData mData;
    private Drawable mIconRes;
    private Path mLinePath;
    private Path mShaderPath;
    private Path mCirclePath;
    private int mColor;
    private float mPathRectHeight = ViewUtil.dp2px(96);
    private float mPathTop = ViewUtil.dp2px(52);
    private int mTitleLineWidth = (int) ViewUtil.dp2px(63);
    private float mWordCentY = ViewUtil.dp2px(17.5f);
    private float mDP10 = ViewUtil.dp2px(10);
    private float mDP8 = ViewUtil.dp2px(8);
    private float mSizeDate = ViewUtil.dp2px(15);
    private float mSizeTitle = ViewUtil.dp2px(18);
    private float mBsLineWidth = ViewUtil.dp2px(2);
    private float mLineWidth = ViewUtil.dp2px(1);
    private String mTitle;
    private RectF mPathRect;
    private int[] mPathY = new int[9];
    private int mItemWidth = 0;
    private LinearGradient mShader;
    private float mCircleRadius = ViewUtil.dp2px(3);
    ArrayList<String> mDateStr = new ArrayList<>();
    private ArrayList<Point> mPositions;
    private Point mCurPosition;

    //顶部左右线
    private static Drawable mLeftConsLine;
    private static Drawable mRightConsLine;

    public LuckView(Context context) {
        this(context, null);
    }

    public LuckView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LuckView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStrokeWidth(mBsLineWidth);
        mPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new TextPaint();

        mShaderPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mShaderPaint.setStyle(Paint.Style.FILL);
        mShaderPaint.setColor(Color.WHITE);

        mPathRect = new RectF();

        mLinePath = new Path();
        mShaderPath = new Path();
        mCirclePath = new Path();

        mPositions = new ArrayList<>();

        mLeftConsLine = ConsDrawableManager.getLeftLine();
        mRightConsLine = ConsDrawableManager.getRightLine();

    }

    /**
     * 设置数据
     * @param date
     */
    public void setDate(LuckData date) {
        if (date == null) {
            return;
        }
        mData = date;
        readyData();
    }

    private void readyData() {
        if (mData == null) {
            return;
        }
        new Thread(new Runnable() {
            @Override
            public void run() {
                initTitle();
                setTitleLineBound();
                initPathData();
                postInvalidate();
            }
        }).start();
    }

    /**
     * 初始化path的Y的点
     */
    private void initPathData() {
        ArrayList<LuckData.LuckItem> data = mData.data;
        if (data.isEmpty()) {
            return;
        }
        //先算出范围大小
        int min = 0;
        int max = 0;
        mDateStr.clear();
        for (int i = 0; i < data.size(); i++) {
            LuckData.LuckItem luckItem = SafeUtil.getSafeData(data, i);
            if (luckItem == null) {
                continue;
            }
            min = Math.min(min, luckItem.luckLevel);
            max = Math.max(max, luckItem.luckLevel);

            String str = TextUtils.isEmpty(luckItem.day) ? "未知" : luckItem.day;
            mDateStr.add(str);
        }
        int range = max - min;

        for (int i = 0; i < mPathY.length; i++) {
            LuckData.LuckItem safeData = SafeUtil.getSafeData(data, i);
            if (safeData != null) {
                float height = (mPathRectHeight - mDP10) * (1.0f - safeData.luckLevel * 1.0f / range);
                mPathY[i] = Math.round(height);
            } else {
                mPathY[i] = Math.round(mPathRectHeight / 2);
            }
        }
    }

    @Override
    public void draw(Canvas canvas) {
        long start = System.currentTimeMillis();
        super.draw(canvas);
        LogUtil.d("LuckView","draw time=" + (System.currentTimeMillis() - start));
    }

    /**
     * 生成path,只有与path有关的才能在这里边处理；在这里边处理是应为path跟View大小有关。
     * 在fragment中有时候回出现重建后view第一时间没有宽度，导致path绘制不出来；
     */
    private void fixPath() {
        mLinePath.rewind();
        mShaderPath.rewind();
        mPositions.clear();
        for (int i = 0; i < mPathY.length; i++) {
            mPositions.add(new Point(i * mItemWidth, mPathY[i]));
            mCirclePath.addCircle(i * mItemWidth, mPathY[i], mCircleRadius, Path.Direction.CCW);
        }
        mLinePath = ViewUtil.createBezierPath(mLinePath, mPositions, 0.2f);
        mShaderPath.addPath(mLinePath);

        mShaderPath.lineTo(mPathY.length * mItemWidth, mPathRectHeight);
        mShaderPath.lineTo(0, mPathRectHeight);
        mShaderPath.lineTo(mPositions.get(0).x, mPositions.get(0).y);
        mShaderPath.close();

        //处理小icon
        mCurPosition = SafeUtil.getSafeData(mPositions, 1);
        if (mCurPosition != null) {
            mIconRes.setBounds((int)(mCurPosition.x - mDP10), (int)(mCurPosition.y- mDP10), (int)(mCurPosition.x + mDP10), (int)(mCurPosition.y+ mDP10));
        }

    }

    private void setTitleLineBound() {
        mTextPaint.setTextSize(mSizeTitle);
        float titleWidth = mTextPaint.measureText(mTitle);
        float hafTitleWidth = titleWidth/2;
        int lex = (int) (getWidth()/2 - hafTitleWidth - mDP8 - mTitleLineWidth);
        int ley = (int) (mPathTop/2 - mBsLineWidth/2);
        Rect rectF = new Rect(lex, ley, (lex + mTitleLineWidth), (int)(ley + mBsLineWidth));
        mLeftConsLine.setBounds(rectF);
        rectF.offset((int)(mTitleLineWidth + mDP8 * 2 + titleWidth), 0);
        mRightConsLine.setBounds(rectF);
    }

    /**
     * 初始化title等数据
     */
    private void initTitle() {
        int iconRes = 0;
        switch (mData.type) {
            case 1:
                mTitle = "运势概括";
                iconRes = R.drawable.constellation_luck_icon;
                break;
            case 2:
                mTitle = "感情运";
                iconRes = R.drawable.constellation_love_icon;
                break;
            case 3:
                mTitle = "财富运";
                iconRes = R.drawable.constellation_money_icon;
                break;
            case 4:
                mTitle = "工作运";
                iconRes = R.drawable.constellation_work_icon;
                break;
            default:
                mTitle = "运势概括";
                iconRes = R.drawable.constellation_luck_icon;
        }

        mColor = getColor(mData.type, 0);
        mShader = new LinearGradient(0, -mBsLineWidth, 0, mPathRectHeight, getColor(mData.type, 1), getColor(mData.type, 2), Shader.TileMode.REPEAT);

        mIconRes = getResources().getDrawable(iconRes);

        mPaint.setColor(mColor);

        mShaderPaint.setColor(mColor);
        mShaderPaint.setShader(mShader);
    }

    /**
     * 通过组拿颜色
     *
     * @param type
     * @param index
     * @return
     */
    private int getColor(int type, int index) {
        if (type > 4 || index > 2) {
            return Color.TRANSPARENT;
        }
        String[] colorArray = getResources().getStringArray(R.array.cons_luck_line_color);
        String color = SafeUtil.getSafeArrayData(colorArray, (type-1));
        if (TextUtils.isEmpty(color)) {
            return Color.TRANSPARENT;
        }
        String[] split = color.split(",");
        String colorStr = SafeUtil.getSafeArrayData(split, index);
        if (!TextUtils.isEmpty(colorStr)) {
            return Color.parseColor(colorStr);
        }
        return Color.TRANSPARENT;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mItemWidth = (w - getPaddingRight() - getPaddingLeft()) / 7;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        fixPath();
        //绘制title
        mTextPaint.setTextSize(mSizeTitle);
        mTextPaint.setColor(mColor);
        //绘制顶部两根线
        ViewUtil.renderTextByCenter(canvas, mTitle, getWidth()/2, mPathTop/2, mTextPaint);
        if (mLeftConsLine != null) {
            mLeftConsLine.draw(canvas);
        }
        if (mRightConsLine != null) {
            mRightConsLine.draw(canvas);
        }

        //曲线范围
        mPathRect.set(getPaddingLeft(), mPathTop, getWidth() - getPaddingLeft() - getPaddingRight(), mPathTop + mPathRectHeight);
        canvas.save();
        canvas.translate(-mItemWidth / 2, mPathRect.top);
        canvas.drawPath(mShaderPath, mShaderPaint);

        //绘制线
        mPaint.setAlpha(255);
        mPaint.setColor(mColor);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(mBsLineWidth);
        canvas.drawPath(mLinePath, mPaint);

        //线上小圆点
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.WHITE);
        canvas.drawPath(mCirclePath, mPaint);

        //绘制细线
        if (mCurPosition != null) {
            canvas.drawLine(mCurPosition.x, mCurPosition.y, mCurPosition.x, mPathRect.height(), mPaint); //粗竖线

            mPaint.setAlpha(75);
            mPaint.setStrokeWidth(mLineWidth);
            canvas.drawLine(mCurPosition.x, 0, mCurPosition.x, mCurPosition.y, mPaint);                 //细竖线
            canvas.drawLine(0, mCurPosition.y, getWidth() + mItemWidth, mCurPosition.y, mPaint);        //细横线
        }

        //绘制icon
        if (mIconRes != null) {
            mIconRes.draw(canvas);
        }

        //绘制下方日期文字
        mTextPaint.setTextSize(mSizeDate);
        mTextPaint.setColor(Color.WHITE);
        ViewUtil.renderTextList(canvas, mDateStr, mTextPaint, 0, mPathRect.height() + mWordCentY, mItemWidth);
        canvas.restore();

    }

}
