package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**     点赞信息的增、删、查、改
 *version
 *@author  slj
 *@time    2017/7/4 15:30
 *@class   TopicLikeCache
 */

public class TopicLikeCache {
    private static TopicLikeCache mIns;
    private Context mContext;
    public synchronized static TopicLikeCache getIns(Context context) {
        if (mIns == null) {
            mIns = new TopicLikeCache(context.getApplicationContext());
        }
        return mIns;
    }

    private TopicLikeCache(Context context) {
        mContext = context;
    }

    /**
     *   插入点赞信息
     * @param topicLikingTable
     */
    public void insertData(TopicLikingTable topicLikingTable) {
        if (topicLikingTable == null || topicLikingTable.mId == -1) {
            return;
        }
        //更新正常
        SQLiteDatabase db = TopicLikingHelper.getInstance(mContext).getWritableDatabase();
        if (updateDataByCode(topicLikingTable) <= 0) {
            db.insert(TopicLikingTable.TABLE_NAME, null, topicLikingTable.createContentValues());
        }
    }

    /**
     * 更新数据
     * @param topicLikingTable
     */
    public int updateDataByCode(TopicLikingTable topicLikingTable) {
        SQLiteDatabase db = TopicLikingHelper.getInstance(mContext).getWritableDatabase();
        return db.update(TopicLikingTable.TABLE_NAME, topicLikingTable.createContentValues(),
                TopicLikingTable.Columns.UID+ " =? and  "
                + TopicLikingTable.Columns.TYPE + " =? ", new String[]{topicLikingTable.mId + "",topicLikingTable.mType});
    }

    public void deleteTable() {
        SQLiteDatabase db = TopicLikingHelper.getInstance(mContext).getWritableDatabase();
        db.delete(TopicLikingTable.TABLE_NAME,TopicLikingTable.Columns._ID + " >? ",new String[]{"0"});
        db.close();
    }

    /**
     * 通过城市uid删除点赞信息
     * @param uid
     */
    public void deleteData(int uid,String type) {
        SQLiteDatabase db = TopicLikingHelper.getInstance(mContext).getWritableDatabase();
        db.delete(TopicLikingTable.TABLE_NAME, TopicLikingTable.Columns.UID + "=? and " + TopicLikingTable.Columns.TYPE
                 + "=? ", new String[]{String.valueOf(uid),type});
    }


    /**
     *   通过ID 和 Type查找点赞信息
     * @param id
     * @return
     */
    public TopicLikingTable getInforByCode(int id,String type) {
        SQLiteDatabase db = TopicLikingHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(TopicLikingTable.TABLE_NAME, null, TopicLikingTable.Columns.UID + " =? and "
                        + TopicLikingTable.Columns.TYPE + " =? ",
                new String[]{String.valueOf(id),type}, null, null, null);
        if (cursor == null) {
            return null;
        }
        TopicLikingTable topicLikingTable = null;
        if (cursor.moveToNext()) {
            topicLikingTable = new TopicLikingTable().fromCursor(cursor);
        }

        cursor.close();
        return topicLikingTable;
    }
}
