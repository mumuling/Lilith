package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.topic.bean.PointBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PointAllCache {
    private static PointAllCache mIns;
    private Context mContext;
    public synchronized static PointAllCache getIns(Context context) {
        if (mIns == null) {
            mIns = new PointAllCache(context.getApplicationContext());
        }
        return mIns;
    }

    private PointAllCache(Context context) {
        mContext = context;
    }
    /**
     *   插入观点信息
     * @param pointAllTable
     */
    public void insertData(PointAllTable pointAllTable) {
        if (pointAllTable == null || pointAllTable.pid == -1) {
            return;
        }
        //更新正常
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getWritableDatabase();
        if (updateDataByCode(pointAllTable) <= 0) {
            db.insert(PointAllTable.TABLE_NAME, null, pointAllTable.creatContentValues());
        }
    }

    /**
     *    批量插入观点信息
     * @param pointAllTables
     */
    public void insertListData(List<PointAllTable> pointAllTables) {
        if (pointAllTables == null || pointAllTables.size() == 0) return;
        for (int i = 0; i < pointAllTables.size(); i ++) {
            insertData(pointAllTables.get(i));
        }
    }

    /**
     *   删除所有数据
     */
    public void deleteTable() {
        SQLiteDatabase db = PointAllTableHepler.getInstance(mContext).getWritableDatabase();
        db.delete(PointAllTable.TABLE_NAME,PointAllTable.Columns._ID + " >? ",new String[]{"0"});
        db.close();
    }

    /**
     *      通过时间查询观点列表
     * @param time
     * @return
     */
    public ArrayList<PointAllTable> getPointListByPid(long time) {
        SQLiteDatabase db = PointAllTableHepler.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(PointAllTable.TABLE_NAME, null, PointAllTable.Columns.TIME + " =? ",
                new String[]{String.valueOf(time)}, null, null, null);
        if (cursor == null) {
            return null;
        }
        ArrayList<PointAllTable> tableList = new ArrayList<>();

        while (cursor.moveToNext()) {
            PointAllTable  pointAllTable = new PointAllTable().fromCursor(cursor);
            tableList.add(pointAllTable);
        }

        cursor.close();
        return tableList;
    }
    private int updateDataByCode(PointAllTable pointAllTable) {
        if (pointAllTable == null)return -1;
        SQLiteDatabase db = PointAllTableHepler.getInstance(mContext).getWritableDatabase();
        return db.update(PointAllTable.TABLE_NAME, pointAllTable.creatContentValuesNoTime(),
                PointAllTable.Columns.PID+ " =? "
                , new String[]{String.valueOf(pointAllTable.pid)});
    }

    /**
     *   将请求的观点加入数据库
     * @param pointList
     * @param t
     */
    public void addPointListToDb(List<PointBean.DataBean> pointList, long t) {
        PointAllTable pointAllTable;
        for (int i= 0; i < pointList.size(); i ++) {
            pointAllTable = new PointAllTable(pointList.get(i).id,t);
           insertData(pointAllTable);
        }
    }

    /**
     *    判断观点是否过期
     * @param pid
     * @param t
     * @return
     */
    public boolean pointIsExperid(int pid,long t) {
        SQLiteDatabase db = PointAllTableHepler.getInstance(mContext).getWritableDatabase();
        Cursor cursor = db.query(PointAllTable.TABLE_NAME, null, PointAllTable.Columns.PID + " =? ",
                new String[]{String.valueOf(pid)}, null, null, null);
        if (cursor == null) {
            db.close();
            return false;
        }
        Cursor cursor1 = db.query(PointAllTable.TABLE_NAME, null, PointAllTable.Columns.PID + " =? and "
                 + PointAllTable.Columns.TIME  + " >=? ",
                new String[]{String.valueOf(pid),String.valueOf(t)}, null, null, null);

        if (cursor1 == null) {
            db.close();
            return true;
        } else {
            db.close();
            return false;
        }
    }
}
