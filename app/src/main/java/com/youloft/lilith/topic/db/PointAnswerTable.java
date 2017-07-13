package com.youloft.lilith.topic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 *
 */

public class PointAnswerTable implements Serializable {
    public static String TABLE_NAME = "my_point_answer";
    public int rid;//回复的ID
    public int pid;//观点的ID
    public String viewPoint;//回复内容
    public int tid;//回复的谁
    public String replyName;//回复人的名字
    public String buildDate;//回复时间
    public long time;
    public PointAnswerTable(){}

    /**
     *    创建点赞数据表
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + PointAnswerTable.Columns._ID + " INTEGER PRIMARY KEY,"
                + Columns.REPLY_ID + " INTEGER,"
                + Columns.PID + " INTEGER,"
                + Columns.RID + " INTEGER,"
                + Columns.BUILD_DATE + " TEXT,"
                + Columns.REPLY_NAME + " TEXT,"
                + Columns.TIME + " LONG,"
                + Columns.VIEWPOINT + " TEXT" + ");");
    }

    /**
     *    创建一条点赞的信息
     * @return
     */
    public ContentValues createContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.REPLY_ID, tid);
        contentValues.put(Columns.PID, pid);
        contentValues.put(Columns.RID, rid);
        contentValues.put(Columns.VIEWPOINT,viewPoint);
        contentValues.put(Columns.BUILD_DATE,buildDate);
        contentValues.put(Columns.REPLY_NAME,replyName);
        contentValues.put(Columns.TIME,System.currentTimeMillis());
        return contentValues;
    }
    public PointAnswerTable fromCursor(Cursor cursor) {
        this.tid = cursor.getInt(cursor.getColumnIndex(Columns.REPLY_ID));
        this.pid = cursor.getInt(cursor.getColumnIndex(PointAnswerTable.Columns.PID));
        this.viewPoint = cursor.getString(cursor.getColumnIndex(PointAnswerTable.Columns.VIEWPOINT));
        this.rid = cursor.getInt(cursor.getColumnIndex(Columns.RID));
        this.replyName =  cursor.getString(cursor.getColumnIndex(Columns.REPLY_NAME));
        this.buildDate = cursor.getString(cursor.getColumnIndex(PointAnswerTable.Columns.BUILD_DATE));
        this.time = cursor.getLong(cursor.getColumnIndex(Columns.TIME));

        return this;
    }

    public interface Columns extends BaseColumns {
        String RID = "rid";  //此条回复的ID
        String PID = "pid";//观点的ID
        String VIEWPOINT = "view_point";//回复内容
        String REPLY_ID = "tid";//回复的谁
        String REPLY_NAME = "reply_name";//回复人的名字
        String BUILD_DATE = "build_date";
        String TIME = "time";
    }
}
