package com.youloft.lilith.topic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.io.Serializable;

/**
 *
 */

public class PointTable implements Serializable {
    public static String TABLE_NAME = "my_point";
    public int tid;//话题的ID
    public int pid;//观点的ID
    public String viewPoint;//观点内容
    public int oid;//支持的ID
    public String buildDate;

    public PointTable(){}
    public PointTable(int oid,int tid,int pid,String viewPoint,String buildDate) {
        this.tid = tid;
        this.pid = pid;
        this.oid = oid;
        this.viewPoint = viewPoint;
        this.buildDate = buildDate;

    }
    /**
     *    创建点赞数据表
     * @param db
     */
    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + Columns._ID + " INTEGER PRIMARY KEY,"
                + Columns.TID + " INTEGER,"
                + Columns.PID + " INTEGER,"
                + Columns.OID + " INTEGER,"
                + Columns.BUILD_DATE + " TEXT,"
                + Columns.VIEWPOINT + " TEXT" + ");");
    }

    /**
     *    创建一条点赞的信息
     * @return
     */
    public ContentValues createContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.TID, tid);
        contentValues.put(Columns.PID, pid);
        contentValues.put(Columns.OID, oid);
        contentValues.put(Columns.VIEWPOINT,viewPoint);
        contentValues.put(Columns.BUILD_DATE,buildDate);
        return contentValues;
    }
    public PointTable fromCursor(Cursor cursor) {
        this.tid = cursor.getInt(cursor.getColumnIndex(Columns.TID));
        this.pid = cursor.getInt(cursor.getColumnIndex(Columns.PID));
        this.viewPoint = cursor.getString(cursor.getColumnIndex(Columns.VIEWPOINT));
        this.oid = cursor.getInt(cursor.getColumnIndex(Columns.OID));
        this.buildDate = cursor.getString(cursor.getColumnIndex(Columns.BUILD_DATE));

        return this;
    }

    public interface Columns extends BaseColumns {
        String TID = "tid";  //话题的ID
        String PID = "pid";//观点的ID
        String VIEWPOINT = "view_point";//观点内容
        String OID = "oid";//支持的id
        String BUILD_DATE = "build_date";
    }
}
