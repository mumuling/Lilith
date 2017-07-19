package com.youloft.lilith.topic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 *
 */

public class PointAllTable implements Serializable {
    public static String TABLE_NAME = "my_point_cache";
    public int pid;//观点的ID
    public long time;
    public PointAllTable(){}
    public PointAllTable(int pid,long time){
        this.pid = pid;
        this.time = time;
    }

    /**
     *    创建观点数据表
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + Columns._ID + " INTEGER PRIMARY KEY,"
                + Columns.PID + " INTEGER,"
                + Columns.TIME + " LONG" + ");");
    }

    public ContentValues creatContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.TIME, time);
        contentValues.put(Columns.PID, pid);
        return contentValues;
    }

    public ContentValues creatContentValuesNoTime() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.PID,pid);
        return contentValues;
    }

    public PointAllTable fromCursor(Cursor cursor) {
        this.pid = cursor.getInt(cursor.getColumnIndex(Columns.PID));
        this.time = cursor.getLong(cursor.getColumnIndex(Columns.TIME));
        return this;
    }
    public interface Columns extends BaseColumns {
        String PID = "pid";//观点的ID
        String TIME = "time";

    }
}
