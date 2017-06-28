package com.youloft.lilith.common.widgets.I18N.trans;

import android.content.Context;
import android.text.TextUtils;

import java.util.Locale;

/**
 * Created by javen on 14-8-16.
 */
public class I18N {


    private static I18NConvert sConvert = null;

    public static void updateConvert(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            if (locale.getCountry().equalsIgnoreCase("tw") || locale.getCountry().equalsIgnoreCase("hk")) {
                sConvert = new S2TConvert();
                CTSConverter.initConvert(context);
            } else {
                sConvert = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            sConvert = null;
        }

    }

    public static boolean isSimpleChinese(Context context) {
        try {
            Locale locale = context.getResources().getConfiguration().locale;
            if (locale.getCountry().equalsIgnoreCase("tw") || locale.getCountry().equalsIgnoreCase("hk")) {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
    /**
     * 真正的转换
     *
     * @param text
     * @return
     */
    public static String convert(CharSequence text) {

        if (TextUtils.isEmpty(text)) {
            return "";
        }
        if (sConvert != null) {

            return sConvert.convert(text.toString());
        }
        return text.toString();

//        return "I18N";
    }


    public interface I18NConvert {
        String convert(String text);
    }

    public static class S2TConvert implements I18NConvert {

        @Override
        public String convert(String text) {
            return CTSConverter.S2T(text);
        }
    }


}
