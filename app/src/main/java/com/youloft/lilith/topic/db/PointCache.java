package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.bean.PointBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PointCache {
    private static PointCache mIns;
    private Context mContext;
    public synchronized static PointCache getIns(Context context) {
        if (mIns == null) {
            mIns = new PointCache(context.getApplicationContext());
        }
        return mIns;
    }

    private PointCache(Context context) {
        mContext = context;
    }

    /**
     *   插入点赞信息
     * @param pointTable
     */
    public void insertData(PointTable pointTable) {
        if (pointTable == null || pointTable.pid == -1) {
            return;
        }
        //更新正常
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getWritableDatabase();
        if (updateDataByCode(pointTable) <= 0) {
            db.insert(PointTable.TABLE_NAME, null, pointTable.createContentValues());
        }
    }

    /**
     * 更新数据
     * @param pointTable
     */
    public int updateDataByCode(PointTable pointTable) {
        if (pointTable == null)return -1;
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getWritableDatabase();
        return db.update(pointTable.TABLE_NAME, pointTable.createContentValues(),
                PointTable.Columns.PID+ " =? "
                       , new String[]{String.valueOf(pointTable.pid)});
    }

    public void deleteTable() {
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointTable.TABLE_NAME,PointTable.Columns._ID + " >? ",new String[]{"0"});
        db.close();

    }

    /**
     *   通过话题ID删除数据
     * @param uid
     */
    public void deleteData(int tid) {
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointTable.TABLE_NAME, PointTable.Columns.TID + "=? "
               , new String[]{String.valueOf(tid)});
    }

    /**
     *   通过观点ID删数据
     * @param pid
     */
    public void deletaDataByPid(int pid) {
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointTable.TABLE_NAME, PointTable.Columns.PID + "=? "
                , new String[]{String.valueOf(pid)});
    }


    /**
     *   通过topicID查找我的观点
     * @param id
     * @return
     */
    public PointTable getInforByCode(int id) {
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(PointTable.TABLE_NAME, null, PointTable.Columns.TID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) {
            return null;
        }
        PointTable pointTable = null;
        if (cursor.moveToNext()) {
            pointTable = new PointTable().fromCursor(cursor);
        }

        cursor.close();
        return pointTable;
    }
    /**
     *   通过pointID查找我的观点
     * @param id
     * @return
     */
    public PointTable getPointByPid(int id) {
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(PointTable.TABLE_NAME, null, PointTable.Columns.PID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) {
            return null;
        }
        PointTable pointTable = null;
        if (cursor.moveToNext()) {
            pointTable = new PointTable().fromCursor(cursor);
        }

        cursor.close();
        return pointTable;
    }

    public ArrayList<PointTable> getAllTablePoint() {
        ArrayList<PointTable> pointList = new ArrayList<>();
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(PointTable.TABLE_NAME, null, null,
                null, null, null, null);
        if (cursor == null) {
            return null;
        }
        PointTable pointTable = null;
        while (cursor.moveToNext()) {
            pointTable = new PointTable().fromCursor(cursor);
            pointList.add(pointTable);
        }
        cursor.close();
        return pointList;
    }
}
