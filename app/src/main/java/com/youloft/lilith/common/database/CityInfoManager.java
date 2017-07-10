package com.youloft.lilith.common.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.text.TextUtils;

import com.youloft.lilith.AppConfig;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by zchao on 2017/6/29.
 * desc: 所有城市的提供；使用了缓存；用于citypicker复用
 * version:
 */

public class CityInfoManager {
    private static String DB_NAME = "city.db";
    private static String TABLE_NAME = "city";
    private static int DB_VERSION = 1;
    protected Context mContext;
    private SQLiteDatabase db;

    private String[] mProvinceDatas;
    /**
     * key - 省 value - 市
     */
    protected Map<String, String[]> mProvinceDatasMap = new HashMap<String, String[]>();

    /**
     * key - 市 values - 区
     */
    protected Map<String, String[]> mCitysDatasMap = new HashMap<String, String[]>();

    /**
     * key区 value 经读 纬度
     */
    protected Map<String, String[]> mLongAndLat = new HashMap<String, String[]>();


    private static CityInfoManager instance = null;

    private CityInfoManager(Context context) {
        mContext = context;
        db = openDatabase();
    }

    public static CityInfoManager getInstance(Context context) {
        if (instance == null) {
            synchronized (CityInfoManager.class) {
                if (instance == null) {
                    instance = new CityInfoManager(context);
                }
            }
        }
        return instance;
    }

    public synchronized String[] getProvinceDatas() {
        if (mProvinceDatas == null || mProvinceDatas.length == 0) {
            String[] provinces = queryAllProvince();
            mProvinceDatas = provinces;
        }
        return mProvinceDatas;
    }

    public synchronized Map<String, String[]> getProvinceAndCityDates() {

        if (mProvinceDatasMap.isEmpty()) {
            if (mProvinceDatas == null || mProvinceDatas.length <= 0) {
                getProvinceDatas();
            }
            if (mProvinceDatas == null) {
                return null;
            }
            for (int i = 0; i < mProvinceDatas.length; i++) {
                String pro = mProvinceDatas[i];   //省名字
                Cursor cur = rawQueryByKey("province", pro, "city");

                String[] citys = new String[cur.getCount()];
                while (cur.moveToNext()) {
                    String cityname = cur.getString(cur.getColumnIndex("city"));
                    citys[cur.getPosition()] = cityname;
                }
                cur.close();
                mProvinceDatasMap.put(pro, citys); //填充省市数据
            }
        }

        return mProvinceDatasMap;
    }


    /**
     * 获取市县数据
     *
     * @return
     */
    public synchronized Map<String, String[]> getCityAndDistrictDate() {
        if (mProvinceDatasMap.isEmpty()) {
            getProvinceAndCityDates();
        }
        if (mCitysDatasMap.isEmpty()) {
            for (Map.Entry<String, String[]> pro :
                    mProvinceDatasMap.entrySet()) {
                String[] citys = pro.getValue();
                for (int i = 0; i < citys.length; i++) {
                    String safeArrayData = citys[i];
                    if (safeArrayData == null) {
                        continue;
                    }
                    Cursor districts = rawQueryByKey("city", safeArrayData, "");
                    String[] dis = new String[districts.getCount()];
                    String[] lonandlat = new String[2];
                    while (districts.moveToNext()) {
                        String district = districts.getString(districts.getColumnIndex("district"));
                        String longitude = districts.getString(districts.getColumnIndex("longitude"));
                        String latitude = districts.getString(districts.getColumnIndex("latitude"));
                        dis[districts.getPosition()] = district;
                        lonandlat[0] = longitude;
                        lonandlat[1] = latitude;
                        mLongAndLat.put(district, lonandlat);
                    }
                    districts.close();
                    mCitysDatasMap.put(safeArrayData, dis);
                }
            }
        }
        return mCitysDatasMap;
    }

    /**
     * 获取所有城市经纬度
     * @return
     */
    public synchronized Map<String, String[]> getDistrictLongAndLati(){
        if (mLongAndLat.isEmpty()) {
            getCityAndDistrictDate();
        }
        return mLongAndLat;
    }


    private Cursor rawQueryByKey(String row, String value, String distinct) {
        String distinctsql = TextUtils.isEmpty(distinct) ? "*" : "distinct " + distinct;
        String sql = "select "+ distinctsql +" from " + TABLE_NAME + " where " + row + "='" + value + "'";
        if (db == null || !db.isOpen()) {
            db = openDatabase();
        }
        return db.rawQuery(sql, null);
    }

    /**
     * 查询所有省
     */
    private String[] queryAllProvince() {

        String sql = "select distinct province from " + TABLE_NAME + " where province not null";
        if (db == null || !db.isOpen()) {
            db = openDatabase();
        }
        Cursor cursor = db.rawQuery(sql, null);
        String[] provinces = new String[cursor.getCount()];
        while (cursor.moveToNext()) {
            String province = cursor.getString(cursor.getColumnIndex("province"));
            provinces[cursor.getPosition()] = province;
        }
        cursor.close();
        return provinces;
    }


    private final int BUFFER_SIZE = 400000;
    public static final String PACKAGE_NAME = AppConfig.Bundle;
    public static final String DB_PATH = "/data" +Environment.getDataDirectory().getAbsolutePath() + "/"
            + PACKAGE_NAME + "/databases";
    String dbfile = DB_PATH + "/city.db";
    private SQLiteDatabase openDatabase() {
        try {
            if (!(new File(dbfile).exists())) { //判断数据库文件是否存在，若不存在则执行导入，否则直接打开数据库
                InputStream is = mContext.getResources().getAssets().open("city.db");
                File file = new File(DB_PATH);
                if (!(file.exists())) {
                    file.mkdirs();
                }
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
