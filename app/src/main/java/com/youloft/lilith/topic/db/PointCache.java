package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.bean.PointBean;

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
    public void handlePointTableInfo(List<PointBean.DataBean> data) {
        if (!AppConfig.LOGIN_STATUS || AppSetting.getUserInfo() == null) return;
        for (int i = 0; i < data.size(); i ++) {
            if (data.get(i).userId == AppSetting.getUserInfo().data.userInfo.id){
                deleteData(data.get(i).id);
            }
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
        db.delete(PointTable.TABLE_NAME,null,null);
        db.close();

    }

    /**
     *
     * @param uid
     */
    public void deleteData(int uid) {
        SQLiteDatabase db = PointTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(PointTable.TABLE_NAME, PointTable.Columns.TID + "=? "
               , new String[]{String.valueOf(uid)});
    }


    /**
     *   通过ID 和 Type查找点赞信息
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
}
