package com.youloft.lilith.topic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

/**    用户点赞信息表
 *version
 *@author  slj
 *@time    2017/7/4 14:51
 *@class   TopicLikingTable
 */

public class TopicLikingTable implements Serializable {
    public static String TYPE_TOPIC_POINT = "topic_point";
    public static String TYPE_POINT_REPLY = "point_reply";
    public static String TABLE_NAME = "liking";
    public int mId = -1;
    public int mIsLike;
    public String mType;

    public TopicLikingTable(){}
    public TopicLikingTable(int uid,int isLike,String type) {
        this.mId = uid;
        this.mIsLike = isLike;
        this.mType = type;
    }

    /**
     *    创建点赞数据表
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + Columns._ID + " INTEGER PRIMARY KEY,"
                + Columns.UID + " INTEGER,"
                + Columns.ISLIKE + " INTEGER,"
                + Columns.TYPE + " TEXT" + ");");
    }

    /**
     *    创建一条点赞的信息
     * @return
     */
    public ContentValues createContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.UID, mId);
        contentValues.put(Columns.ISLIKE, mIsLike);
        contentValues.put(Columns.TYPE, mType);
        return contentValues;
    }
    public TopicLikingTable fromCursor(Cursor cursor) {
        this.mId = cursor.getInt(cursor.getColumnIndex(Columns.UID));
        this.mIsLike = cursor.getInt(cursor.getColumnIndex(Columns.ISLIKE));
        this.mType = cursor.getString(cursor.getColumnIndex(Columns.TYPE));
        return this;
    }
    public interface Columns extends BaseColumns {
         String UID = "uid";  //点赞对象的ID
        String ISLIKE = "like";//点赞状态
        String TYPE = "type";//点赞的是观点还是回复
    }
}
