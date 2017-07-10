package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.common.database.SQLiteHelper;

/**
 *
 */

public class PointAnswerTableHelper extends SQLiteHelper {
    public static final String DB_NAME = "point_answer.db";
    public static final int DB_VERSION = 1;
    public static PointAnswerTableHelper mInstance;

    public PointAnswerTableHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static PointAnswerTableHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new PointAnswerTableHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        PointAnswerTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
