package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.login.bean.UserBean;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.bean.PointBean;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */

public class PointAnswerCache {
    private static PointAnswerCache mIns;
    private Context mContext;
    public synchronized static PointAnswerCache getIns(Context context) {
        if (mIns == null) {
            mIns = new PointAnswerCache(context.getApplicationContext());
        }
        return mIns;
    }

    private PointAnswerCache(Context context) {
        mContext = context;
    }

    /**
     *   插入点赞信息
     * @param pointAnswerTable
     */
    public void insertData(PointAnswerTable pointAnswerTable) {
        if (pointAnswerTable == null || pointAnswerTable.rid == -1) {
            return;
        }
        //更新正常
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getWritableDatabase();
        if (updateDataByCode(pointAnswerTable) <= 0) {
            db.insert(PointAnswerTable.TABLE_NAME, null, pointAnswerTable.createContentValues());
        }
    }
    public void handlePointTableInfo(List<PointBean.DataBean> data) {
        UserBean userInfo = AppSetting.getUserInfo();
        if (userInfo == null) return;
        for (int i = 0; i < data.size(); i ++) {
            if (data.get(i).userId == userInfo.data.userInfo.id){
                deleteData(data.get(i).id);
            }
        }
    }

    /**
     * 更新数据
     * @param pointAnswerTable
     */
    public int updateDataByCode(PointAnswerTable pointAnswerTable) {
        if (pointAnswerTable == null)return -1;
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getWritableDatabase();
        return db.update(PointAnswerTable.TABLE_NAME, pointAnswerTable.createContentValues(),
                PointAnswerTable.Columns.RID+ " =? "
                , new String[]{String.valueOf(pointAnswerTable.rid)});
    }

    /**
     *
     * @param uid
     */
    public void deleteData(int uid) {
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointAnswerTable.TABLE_NAME, PointAnswerTable.Columns.RID + "=? "
                , new String[]{String.valueOf(uid)});
    }

    public void deletePointData(int uid) {
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointAnswerTable.TABLE_NAME, PointAnswerTable.Columns.PID + "=? "
                , new String[]{String.valueOf(uid)});
    }

    public void deleteTable() {
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointAnswerTable.TABLE_NAME,PointAnswerTable.Columns._ID + " >? ",new String[]{"0"});
        db.close();
    }


    /**
     *   通过ID 和 Type查找点赞信息
     * @param id
     * @return
     */
    public PointAnswerTable getInforByCode(int id) {
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(PointAnswerTable.TABLE_NAME, null, PointAnswerTable.Columns.RID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) {
            return null;
        }
        PointAnswerTable pointAnswerTable = null;
        if (cursor.moveToNext()) {
            pointAnswerTable = new PointAnswerTable().fromCursor(cursor);
        }

        cursor.close();
        return pointAnswerTable;
    }

    public ArrayList<PointAnswerTable> getAnswerListByCode(int id) {
        SQLiteDatabase db = PointAnswerTableHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(PointAnswerTable.TABLE_NAME, null, PointAnswerTable.Columns.PID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) {
            return null;
        }
        ArrayList<PointAnswerTable> tableList = new ArrayList<>();

        while (cursor.moveToNext()) {
            PointAnswerTable  pointAnswerTable = new PointAnswerTable().fromCursor(cursor);
            tableList.add(pointAnswerTable);
        }

        cursor.close();
        return tableList;
    }
}
