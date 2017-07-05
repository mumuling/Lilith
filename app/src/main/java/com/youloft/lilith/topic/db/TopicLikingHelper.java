package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.common.database.SQLiteHelper;

/**       点赞信息表
 *version
 *@author  slj
 *@time    2017/7/4 14:44
 *@class   TopicLikingHelper
 */

public class TopicLikingHelper extends SQLiteHelper {

    public static final String DB_NAME = "like.db";
    public static final int DB_VERSION = 1;
    public static TopicLikingHelper mInstance;

    public TopicLikingHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    public synchronized static TopicLikingHelper getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new TopicLikingHelper(context);
        }
        return mInstance;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        TopicLikingTable.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onUpgrade(db, oldVersion, newVersion);
    }
}
