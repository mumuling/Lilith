package com.youloft.lilith.cons.view;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;

import com.youloft.lilith.common.utils.Utils;
import com.youloft.lilith.common.utils.ViewUtil;
import com.youloft.lilith.cons.consmanager.ConsManager;

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

    private static final HashMap<String, ConsManager.ConsInfo> mConsImg = new HashMap<>();

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
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(Color.BLUE);

        mTextPaint =  new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setTextSize(ViewUtil.dp2px(13));
        mTextPaint.setColor(Color.WHITE);
    }

    public Bitmap getConsImg(int consCode, int love, int career, int wealth) {
        return getConsImg(String.valueOf(consCode),String.valueOf(love),String.valueOf(career),String.valueOf(wealth));
    }


    /**
     * 获取星座的图片
     * @param consCode
     * @param love
     * @param career
     * @param wealth
     * @return
     */
    public Bitmap getConsImg(String consCode, String love, String career, String wealth){
        ConsManager.ConsInfo consSrc = ConsManager.getConsSrc(consCode);
        Bitmap srcBit = BitmapFactory.decodeResource(Utils.getContext().getResources(), consSrc.pImgDrawable);
        int width = srcBit.getWidth();
        int height = srcBit.getHeight();

        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mCanvas = new Canvas(bitmap);

        //绘制图片
        mCanvas.drawBitmap(srcBit, 0,0,mPaint);
        srcBit = null;
        //绘绘制文字
        drawWord(love, consSrc.pCirclePosition[0] * width, consSrc.pCirclePosition[1] * height);
        drawWord(career, consSrc.pCirclePosition[2] * width, consSrc.pCirclePosition[3] * height);
        drawWord(wealth, consSrc.pCirclePosition[4] * width, consSrc.pCirclePosition[5] * height);

        return bitmap;
    }

    /**
     * 文字中心点坐标
     * @param word
     * @param x
     * @param y
     */
    private void drawWord(String word, float x, float y) {
        ViewUtil.renderTextByCenter(mCanvas, word, x, y, mTextPaint);
    }

}
