package com.youloft.lilith.cons.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Utils;
import com.youloft.lilith.common.utils.ViewUtil;

import java.util.HashMap;

/**
 * Created by zchao on 0.2f17/6/0.3f.
 * desc: 星座首页顶部的图生成器，保证文字能刚好对准图片的几个圆圈
 * version:
 */

public class ConstellationViewFactory {
    private Paint mPaint;
    private TextPaint mTextPaint;
    private Canvas mCanvas;

    private static final HashMap<String, ConsInfo> mConsImg = new HashMap<>();

    private static ConstellationViewFactory instance;

    public static ConstellationViewFactory getInstance(){
        if (instance == null) {
            synchronized (ConstellationViewFactory.class){
                if (instance == null) {
                    instance = new ConstellationViewFactory();
                }
            }
        }
        return instance;
    }

    private ConstellationViewFactory() {
        initConsMap();
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        mTextPaint =  new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(ViewUtil.dp2px(20));
        mTextPaint.setColor(Color.WHITE);
    }

    /**
     * 获取星座的图片
     * @param consName
     * @param text1
     * @param text2
     * @param text3
     * @return
     */
    public Bitmap getConsImg(String consName, String text1, String text2, String text3){
        ConsInfo consSrc = getConsSrc(consName);
        Bitmap srcBit = BitmapFactory.decodeResource(Utils.getContext().getResources(), consSrc.pImgDrawable);
        int width = srcBit.getWidth();
        int height = srcBit.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);

        //绘制图片
        mCanvas.drawBitmap(srcBit, 0,0,mPaint);
        srcBit = null;
        //绘绘制文字
        drawWord(text1, consSrc.pCirclePosition[0] * width, consSrc.pCirclePosition[1] * height);
        drawWord(text2, consSrc.pCirclePosition[2] * width, consSrc.pCirclePosition[3] * height);
        drawWord(text3, consSrc.pCirclePosition[4] * width, consSrc.pCirclePosition[5] * height);

        return bitmap;
    }

    /**
     * 文字中心点坐标
     * @param word
     * @param x
     * @param y
     */
    private void drawWord(String word, float x, float y) {
        mCanvas.drawCircle(x, y, 40f, mPaint);
        ViewUtil.renderTextByCenter(mCanvas, word, x, y, mTextPaint);
    }

    /**
     * 获取星座图片
     * @param cons
     * @return
     */
    private ConsInfo getConsSrc(String cons){
        if (mConsImg.isEmpty()) {
            initConsMap();
        }
        return mConsImg.get(cons);
    }

    /**
     * 初始化数据
     */
    private synchronized void initConsMap() {
        if (mConsImg.isEmpty()){
            mConsImg.put("白羊座", new ConsInfo("白羊座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("金牛座", new ConsInfo("金牛座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("双子座", new ConsInfo("双子座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("巨蟹座", new ConsInfo("巨蟹座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("狮子座", new ConsInfo("狮子座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("处女座", new ConsInfo("处女座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));

            mConsImg.put("天秤座", new ConsInfo("天秤座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("天蝎座", new ConsInfo("天蝎座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("射手座", new ConsInfo("射手座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("摩羯座", new ConsInfo("摩羯座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("水瓶座", new ConsInfo("水瓶座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));
            mConsImg.put("双鱼座", new ConsInfo("双鱼座", R.drawable.cons, new float[]{0.2f,0.4f,0.5f,0.6f,0.7f,0.3f}));

        }
    }


    /**
     * 星座图片的信息
     */
    public class ConsInfo{
        public ConsInfo() {
        }

        public ConsInfo(String pKey, int pImgDrawable, float[] pCirclePosition) {
            this.pKey = pKey;
            this.pImgDrawable = pImgDrawable;
            this.pCirclePosition = pCirclePosition;
        }

        String pKey;                    //名称
        int pImgDrawable;               //img资源
        float[] pCirclePosition;        //图片中的几个点在图片中的比例值；默认三个点，0，1号位置放第一个点的x,y坐标；后边依次
    }
}
