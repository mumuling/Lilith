package com.youloft.lilith.cons.consmanager;

import com.youloft.lilith.R;
import com.youloft.lilith.common.utils.Utils;

import java.util.HashMap;

/**
 * Created by zchao on 2017/6/30.
 * desc:
 * version:
 */

public class ConsManager {
    private static final HashMap<String, ConsInfo> mConsImg = new HashMap<>();
    private static final HashMap<String, Integer[]> mConsIconImg = new HashMap<>();

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
     * 获取星座图片
     *
     * @param consKey
     * @return
     */
    public static Integer[] getConsIconSrc(String consKey) {
        if (mConsIconImg.isEmpty()) {
            initConsIconMap();
        }
        return mConsIconImg.get(consKey);
    }

    /**
     * 初始化上升星座的集合
     */
    public static int getAscendantConsIcon(String index){

        switch (Integer.parseInt(index)) {
            case 1: //水瓶座
                return R.drawable.ascendant_aquarius_icon;
            case 2: //双鱼座
                return R.drawable.ascendant_pisces_icon;
            case 3:  //白羊座
                return R.drawable.ascendant_aries_icon;
            case 4:  //金牛座
                return R.drawable.ascendant_taurus_icon;
            case 5:  //双子座
                return R.drawable.ascendant_gemini_icon;
            case 6: //巨蟹座
                return R.drawable.ascendant_cancer_icon;
            case 7:  //狮子座
                return R.drawable.ascendant_leo_icon;
            case 8:  //处女座
                return R.drawable.ascendant_virgo_icon;
            case 9:  //天秤座
                return R.drawable.ascendant_libra_icon;
            case 10:  //天蝎座
                return R.drawable.ascendant_scorpio_icon;
            case 11:  //射手座
                return R.drawable.ascendant_sagittarius_icon;
            case 12:  //摩羯座
                return R.drawable.ascendant_capricorn_icon;
            default:  //默认返回白羊座
                return R.drawable.ascendant_aries_icon;
        }

    }

    /**
     * 初始化太阳星座的集合
     */
    public static int getSunConsIcon(String index){

        switch (Integer.parseInt(index)) {
            case 1: //水瓶座
                return R.drawable.sun_aquarius_icon;
            case 2: //双鱼座
                return R.drawable.sun_pisces_icon;
            case 3:  //白羊座
                return R.drawable.sun_aries_icon;
            case 4:  //金牛座
                return R.drawable.sun_taurus_icon;
            case 5:  //双子座
                return R.drawable.sun_gemini_icon;
            case 6: //巨蟹座
                return R.drawable.sun_cancer_icon;
            case 7:  //狮子座
                return R.drawable.sun_leo_icon;
            case 8:  //处女座
                return R.drawable.sun_virgo_icon;
            case 9:  //天秤座
                return R.drawable.sun_libra_icon;
            case 10:  //天蝎座
                return R.drawable.sun_scorpio_icon;
            case 11:  //射手座
                return R.drawable.sun_sagittarius_icon;
            case 12:  //摩羯座
                return R.drawable.sun_capricorn_icon;
            default:  //默认返回白羊座
                return R.drawable.sun_aries_icon;
        }

    }

    /**
     * 获取月亮星座
     * @param index
     * @return
     */
    public static int getMoonConsIcon(String index){

        switch (Integer.parseInt(index)) {
            case 1: //水瓶座
                return R.drawable.moonconstellation_aquarius_icon;
            case 2: //双鱼座
                return R.drawable.moonconstellation_pisces_icon;
            case 3:  //白羊座
                return R.drawable.moonconstellation_aries_icon;
            case 4:  //金牛座
                return R.drawable.moonconstellation_taurus_icon;
            case 5:  //双子座
                return R.drawable.moonconstellation_gemini_icon;
            case 6: //巨蟹座
                return R.drawable.moonconstellation_cancer_icon;
            case 7:  //狮子座
                return R.drawable.moonconstellation_leo_icon;
            case 8:  //处女座
                return R.drawable.moonconstellation_virgo_icon;
            case 9:  //天秤座
                return R.drawable.moonconstellation_libra_icon;
            case 10:  //天蝎座
                return R.drawable.moonconstellation_scorpio_icon;
            case 11:  //射手座
                return R.drawable.moonconstellation_sagittarius_icon;
            case 12:  //摩羯座
                return R.drawable.moonconstellation_capricorn_icon;
            default:  //默认返回白羊座
                return R.drawable.moonconstellation_aries_icon;
        }

    }

    /**
     * 获取男星座
     * @param index
     * @return
     */
    public static int getBoyConsIcon(String index){

        switch (Integer.parseInt(index)) {
            case 1: //水瓶座
                return R.drawable.boy_aquariuss_icon;
            case 2: //双鱼座
                return R.drawable.boy_pisces_icon;
            case 3:  //白羊座
                return R.drawable.boy_aries_icon;
            case 4:  //金牛座
                return R.drawable.boy_taurus_icon;
            case 5:  //双子座
                return R.drawable.boy_gemini_icon;
            case 6: //巨蟹座
                return R.drawable.boy_cancer_icon;
            case 7:  //狮子座
                return R.drawable.boy_leo_icon;
            case 8:  //处女座
                return R.drawable.boy_virgo_icon;
            case 9:  //天秤座
                return R.drawable.boy_libra_icon;
            case 10:  //天蝎座
                return R.drawable.boy_scorpio_icon;
            case 11:  //射手座
                return R.drawable.boy_sagittarius_icon;
            case 12:  //摩羯座
                return R.drawable.boy_capricorn_icon;
            default:  //默认返回白羊座
                return R.drawable.boy_aries_icon;
        }

    }

    /**
     * 获取女星座
     * @param index
     * @return
     */
    public static int getGirlConsIcon(String index){

        switch (Integer.parseInt(index)) {
            case 1: //水瓶座
                return R.drawable.girl_aquariuss_icon;
            case 2: //双鱼座
                return R.drawable.girl_pisces_icon;
            case 3:  //白羊座
                return R.drawable.girl_aries_icon;
            case 4:  //金牛座
                return R.drawable.girl_taurus_icon;
            case 5:  //双子座
                return R.drawable.girl_gemini_icon;
            case 6: //巨蟹座
                return R.drawable.girl_cancer_icon;
            case 7:  //狮子座
                return R.drawable.girl_leo_icon;
            case 8:  //处女座
                return R.drawable.girl_virgo_icon;
            case 9:  //天秤座
                return R.drawable.girl_libra_icon;
            case 10:  //天蝎座
                return R.drawable.girl_scorpio_icon;
            case 11:  //射手座
                return R.drawable.girl_sagittarius_icon;
            case 12:  //摩羯座
                return R.drawable.girl_capricorn_icon;
            default:  //默认返回白羊座
                return R.drawable.girl_aries_icon;
        }

    }

    /**
     * 获取首页星座信息，英文模糊字
     * @param index
     * @return
     */
    public static int getConsBlurWord(int index){
        switch (index) {
            case 1: //水瓶座
                return R.drawable.aquarius;
            case 2: //双鱼座
                return R.drawable.pisces;
            case 3:  //白羊座
                return R.drawable.aries;
            case 4:  //金牛座
                return R.drawable.taurus;
            case 5:  //双子座
                return R.drawable.gemini;
            case 6: //巨蟹座
                return R.drawable.cancer;
            case 7:  //狮子座
                return R.drawable.leo;
            case 8:  //处女座
                return R.drawable.virgo;
            case 9:  //天秤座
                return R.drawable.libra;
            case 10:  //天蝎座
                return R.drawable.scorpio;
            case 11:  //射手座
                return R.drawable.sagittarius;
            case 12:  //摩羯座
                return R.drawable.capricorn;
            default:  //默认返回白羊座
                return R.drawable.aries;
        }

    }
    /**
     * 初始化tab数据
     */
    private static synchronized void initConsIconMap() {
        if (mConsIconImg.isEmpty()) {
            mConsIconImg.put("0", new Integer[]{R.drawable.icon_pisces, R.drawable.icon2_pisces});
            mConsIconImg.put("1", new Integer[]{R.drawable.icon_aquarius, R.drawable.icon2_aquarius});
            mConsIconImg.put("2", new Integer[]{R.drawable.icon_pisces, R.drawable.icon2_pisces});
            mConsIconImg.put("3", new Integer[]{R.drawable.icon_aries, R.drawable.icon2_aries});
            mConsIconImg.put("4", new Integer[]{R.drawable.icon_taurus, R.drawable.icon2_taurus});
            mConsIconImg.put("5", new Integer[]{R.drawable.icon_gemini, R.drawable.icon2_gemini});
            mConsIconImg.put("6", new Integer[]{R.drawable.icon_cancer, R.drawable.icon2_cancer});
            mConsIconImg.put("7", new Integer[]{R.drawable.icon_lion, R.drawable.icon2_lion});
            mConsIconImg.put("8", new Integer[]{R.drawable.icon_virgo, R.drawable.icon2_virgo});
            mConsIconImg.put("9", new Integer[]{R.drawable.icon_libra, R.drawable.icon2_libra});
            mConsIconImg.put("10", new Integer[]{R.drawable.icon_scorpio, R.drawable.icon2_scorpio});
            mConsIconImg.put("11", new Integer[]{R.drawable.icon_sagittarius, R.drawable.icon2_sagittarius});
            mConsIconImg.put("12", new Integer[]{R.drawable.icon_capricorn, R.drawable.icon2_capricorn});

        }
    }

    /**
     * 初始化数据
     */
    private static synchronized void initConsMap() {
        if (mConsImg.isEmpty()) {
            mConsImg.put("0", new ConsInfo(getConsName(0), "2.19-3.20", R.drawable.constellation_pisces_pic, new float[]{344 / 386f, 190.5f / 232f, 111 / 386f, 189.5f / 232f, 85 / 386f, 40.5f / 232f}));
            mConsImg.put("1", new ConsInfo(getConsName(1), "1.20-2.18", R.drawable.constellation_aquarius_pic, new float[]{224 / 386f, 131.5f / 232f, 312 / 386f, 128.5f / 232f, 45.5f / 386f, 87.5f / 232f}));
            mConsImg.put("2", new ConsInfo(getConsName(2), "2.19-3.20", R.drawable.constellation_pisces_pic, new float[]{344 / 386f, 190.5f / 232f, 111 / 386f, 189.5f / 232f, 85 / 386f, 40.5f / 232f}));
            mConsImg.put("3", new ConsInfo(getConsName(3), "3.21-4.20", R.drawable.constellation_aries_pic, new float[]{211 / 386f, 92.5f / 232f, 314 / 386f, 189.5f / 232f, 71 / 386f, 42.5f / 232f}));
            mConsImg.put("4", new ConsInfo(getConsName(4), "4.21-5.21", R.drawable.constellation_taurus_pic, new float[]{162 / 386f, 138.5f / 232f, 347 / 386f, 192.5f / 232f, 95 / 386f, 40.5f / 232f}));
            mConsImg.put("5", new ConsInfo(getConsName(5), "5.22-6.21", R.drawable.constellation_gemini_pic, new float[]{297 / 386f, 173.5f / 232f, 319 / 386f, 48.5f / 232f, 46 / 386f, 160.5f / 232f}));
            mConsImg.put("6", new ConsInfo(getConsName(6), "6.22-7.22", R.drawable.constellation_cancer_pic, new float[]{184 / 386f, 97.5f / 232f, 325 / 386f, 183.5f / 232f, 61 / 386f, 37.5f / 232f}));
            mConsImg.put("7", new ConsInfo(getConsName(7), "7.23-8.22", R.drawable.constellation_leo_pic, new float[]{93 / 386f, 194.5f / 232f, 320 / 386f, 122.5f / 232f, 207 / 386f, 83.5f / 232f}));
            mConsImg.put("8", new ConsInfo(getConsName(8), "8.23-9.22", R.drawable.constellation_virgo_pic, new float[]{350 / 386f, 161.5f / 232f, 248 / 386f, 67.5f / 232f, 89 / 386f, 108.5f / 232f}));
            mConsImg.put("9", new ConsInfo(getConsName(9), "9.23-10.23", R.drawable.constellation_libra_pic, new float[]{192 / 386f, 34.5f / 232f, 294 / 386f, 161.5f / 232f, 119 / 386f, 109.5f / 232f}));
            mConsImg.put("10", new ConsInfo(getConsName(10), "10.24-11.22", R.drawable.constellation_scorpio_pic, new float[]{236 / 386f, 143.5f / 232f, 352 / 386f, 57.5f / 232f, 108 / 386f, 109.5f / 232f}));
            mConsImg.put("11", new ConsInfo(getConsName(11), "11.23-12.21", R.drawable.constellation_sagittarius_pic, new float[]{227 / 386f, 75.5f / 232f, 175 / 386f, 196.5f / 232f, 42 / 386f, 112.5f / 232f}));
            mConsImg.put("12", new ConsInfo(getConsName(12), "12.22-1.19", R.drawable.constellation_capricorn_pic, new float[]{333 / 386f, 116.5f / 232f, 248 / 386f, 37.5f / 232f, 53 / 386f, 193.5f / 232f}));

        }
    }

    /**
     * 获取星座国际化的名字
     * @param consKey
     * @return
     */
    public static String getConsName(int consKey){
        int consStringSrc = 0;
        switch (consKey) {
            case 1: //水瓶座
                consStringSrc= R.string.aquarius;
                break;
            case 2: //双鱼座
                consStringSrc= R.string.pisces;
                break;
            case 3:  //白羊座
                consStringSrc= R.string.aries;
                break;
            case 4:  //金牛座
                consStringSrc= R.string.taurus;
                break;
            case 5:  //双子座
                consStringSrc= R.string.gemini;
                break;
            case 6: //巨蟹座
                consStringSrc= R.string.cancer;
                break;
            case 7:  //狮子座
                consStringSrc= R.string.leo;
                break;
            case 8:  //处女座
                consStringSrc= R.string.virgo;
                break;
            case 9:  //天秤座
                consStringSrc= R.string.libra;
                break;
            case 10:  //天蝎座
                consStringSrc= R.string.scorpio;
                break;
            case 11:  //射手座
                consStringSrc= R.string.sagittarius;
                break;
            case 12:  //摩羯座
                consStringSrc= R.string.capricorn;
                break;
            default:  //默认返回白羊座
                consStringSrc= R.string.aries;
        }
        return Utils.getContext().getResources().getString(consStringSrc);
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
