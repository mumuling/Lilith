//package com.youloft.lilith.common.net;
//
//import android.content.Context;
//import android.content.SharedPreferences;
//import android.text.TextUtils;
//import android.text.format.DateFormat;
//import android.util.Log;
//
//import org.json.JSONObject;
//
//import java.util.Date;
//
///**
// * 保存Token
// * <p/>
// * Created by javen on 15/6/19.
// */
//public class TokenStore {
//
//    private SharedPreferences mTokenSp = null;
//
//    private String token;
//
//    public TokenStore(Context context) {
//        mTokenSp = context.getSharedPreferences("com_youloft_cache_small", Context.MODE_PRIVATE);
//        token = mTokenSp.getString("cached_token", null);
//    }
//
//
//    /**
//     * 加载Token
//     */
//    public boolean loadToken() {
//        if (TextUtils.isEmpty(token)) {
//            String dateStr = DateFormat.format("yyyyMMdd", new Date()).toString();
//            String cardVer = "6.0";
//            String tag = "Youloft_Android";
//            final String url = "https://c.51wnl.com/API/GetAuthorize.aspx?date=" + dateStr + "&version=" + cardVer + "&clientid=" + tag;
//
//            Log.d("TAG_TOKEN", "token request in network");
//            try {
//                String json = OkHttpUtils.getInstance().getString(url, null, false);
//                JSONObject object = new JSONObject(json);
//                if (object.optInt("status") == 200) {
//                    String t = object.optJSONObject("msg").optString("token");
//                    token = t;
//                    mTokenSp.edit().putString("cached_token", token).commit();
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//        }
//        return true;
//    }
//
//    public String getToken() {
//        return token;
//    }
//}
