package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.common.database.SQLiteHelper;

/**
 *version 1.0.0
 *@author  slj
 *@time    2017/7/19 15:27
 *@class   PointAllTableHepler
 */

public class PointAllTableHepler extends SQLiteHelper {
    public static final String DB_NAME = "point_all.db";
    public static final int DB_VERSION = 1;
    public static PointAllTableHepler mInstance;
    public PointAllTableHepler(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    public synchronized static PointAllTableHepler getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PointAllTableHepler(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PointAllTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
