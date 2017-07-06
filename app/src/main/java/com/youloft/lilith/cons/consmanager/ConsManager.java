package com.youloft.lilith.cons.consmanager;

import com.youloft.lilith.R;

import java.util.HashMap;

/**
 * Created by zchao on 2017/6/30.
 * desc:
 * version:
 */

public class ConsManager {
    private static final HashMap<String, ConsInfo> mConsImg = new HashMap<>();

    /**
     * 获取星座图片
     *
     * @param cons
     * @return
     */
    public static ConsInfo getConsSrc(String cons) {
        if (mConsImg.isEmpty()) {
            initConsMap();
        }
        return mConsImg.get(cons);
    }

    /**
     * 初始化数据
     */
    private static synchronized void initConsMap() {
        if (mConsImg.isEmpty()) {
            mConsImg.put("0",   new ConsInfo("双鱼座", "2.19-3.20",    R.drawable.constellation_pisces_pic,        new float[]{344 / 386f, 190.5f / 232f, 111 / 386f, 189.5f / 232f, 85 / 386f, 40.5f / 232f}));
            mConsImg.put("1",   new ConsInfo("水瓶座", "1.20-2.18",    R.drawable.constellation_aquarius_pic,      new float[]{224 / 386f, 131.5f / 232f, 312 / 386f, 128.5f / 232f, 45.5f / 386f, 87.5f / 232f}));
            mConsImg.put("2",   new ConsInfo("双鱼座", "2.19-3.20",    R.drawable.constellation_pisces_pic,        new float[]{344 / 386f, 190.5f / 232f, 111 / 386f, 189.5f / 232f, 85 / 386f, 40.5f / 232f}));
            mConsImg.put("3",   new ConsInfo("白羊座", "3.21-4.19",    R.drawable.constellation_aries_pic,         new float[]{211 / 386f, 92.5f / 232f, 314 / 386f, 189.5f / 232f, 71 / 386f, 42.5f / 232f}));
            mConsImg.put("4",   new ConsInfo("金牛座", "4.20-5.20",    R.drawable.constellation_taurus_pic,        new float[]{162 / 386f, 138.5f / 232f, 347 / 386f, 192.5f / 232f, 95 / 386f, 40.5f / 232f}));
            mConsImg.put("5",   new ConsInfo("双子座", "5.21-6.21",    R.drawable.constellation_gemini_pic,        new float[]{297 / 386f, 173.5f / 232f, 319 / 386f, 48.5f / 232f, 46 / 386f, 160.5f / 232f}));
            mConsImg.put("6",   new ConsInfo("巨蟹座", "6.22-7.22",    R.drawable.constellation_cancer_pic,        new float[]{184 / 386f, 97.5f / 232f, 325 / 386f, 183.5f / 232f, 61 / 386f, 37.5f / 232f}));
            mConsImg.put("7",   new ConsInfo("狮子座", "7.23-8.22",    R.drawable.constellation_leo_pic,           new float[]{93 / 386f, 194.5f / 232f, 320 / 386f, 122.5f / 232f, 207 / 386f, 83.5f / 232f}));
            mConsImg.put("8",   new ConsInfo("处女座", "8.23-9.22",    R.drawable.constellation_virgo_pic,         new float[]{350 / 386f, 161.5f / 232f, 248 / 386f, 67.5f / 232f, 89 / 386f, 108.5f / 232f}));
            mConsImg.put("9",   new ConsInfo("天秤座", "9.23-10.23",   R.drawable.constellation_libra_pic,         new float[]{192 / 386f, 34.5f / 232f, 294 / 386f, 161.5f / 232f, 119 / 386f, 109.5f / 232f}));
            mConsImg.put("10",  new ConsInfo("天蝎座", "10.24-11.21",  R.drawable.constellation_scorpio_pic,       new float[]{236 / 386f, 143.5f / 232f, 352 / 386f, 57.5f / 232f, 108 / 386f, 109.5f / 232f}));
            mConsImg.put("11",  new ConsInfo("射手座", "11.22-12.21",  R.drawable.constellation_sagittarius_pic,   new float[]{227 / 386f, 75.5f / 232f, 175 / 386f, 196.5f / 232f, 42 / 386f, 112.5f / 232f}));
            mConsImg.put("12",  new ConsInfo("摩羯座", "12.23-1.19",   R.drawable.constellation_capricorn_pic,     new float[]{333 / 386f, 116.5f / 232f, 248 / 386f, 37.5f / 232f, 53 / 386f, 193.5f / 232f}));

        }
    }

    /**
     * 星座图片的信息
     */
    public static class ConsInfo {
        public ConsInfo() {
        }

        public ConsInfo(String pKey, String pRang, int pImgDrawable, float[] pCirclePosition) {
            this.pKey = pKey;
            this.pImgDrawable = pImgDrawable;
            this.pCirclePosition = pCirclePosition;
            this.pRang = pRang;

        }

        public String pKey;                    //名称
        public int pImgDrawable;               //img资源
        public float[] pCirclePosition;        //图片中的几个点在图片中的比例值；默认三个点，0，1号位置放第一个点的x,y坐标；后边依次
        public String pRang;                   //星座的时间范围
    }
}
