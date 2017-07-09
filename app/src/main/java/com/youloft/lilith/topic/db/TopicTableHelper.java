package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.common.database.SQLiteHelper;

/**
 *
 */

public class TopicTableHelper extends SQLiteHelper {

    public static final String DB_NAME = "topic.db";
    public static final int DB_VERSION = 1;
    public static TopicTableHelper mInstance;

    public TopicTableHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static TopicTableHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TopicTableHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TopicTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
