package com.youloft.lilith.topic.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.youloft.lilith.AppConfig;
import com.youloft.lilith.common.rx.RxObserver;
import com.youloft.lilith.setting.AppSetting;
import com.youloft.lilith.topic.TopicRepo;
import com.youloft.lilith.topic.bean.PointBean;
import com.youloft.lilith.topic.bean.TopicDetailBean;
import com.youloft.lilith.topic.bean.VoteBean;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

/**
 *
 */

public class TopicInfoCache {

    private static TopicInfoCache mIns;
    private Context mContext;
    private TopicInfoCache(Context context) {
        mContext = context;
    }
    public synchronized static TopicInfoCache getIns(Context context) {
        if (mIns == null) {
            mIns = new TopicInfoCache(context.getApplicationContext());
        }
        return mIns;
    }

    /**
     *   插入点赞信息
     * @param topicTable
     */
    public void insertData(TopicTable topicTable) {
        if (topicTable == null || topicTable.mId == -1) {
            return;
        }
        //更新正常
        SQLiteDatabase db = TopicTableHelper.getInstance(mContext).getWritableDatabase();
        if (updateDataByCode(topicTable) <= 0) {
            db.insert(TopicTable.TABLE_NAME, null, topicTable.createContentValues());
        }
    }

    /**
     * 更新数据
     * @param topicTable
     */
    public int updateDataByCode(TopicTable topicTable) {
        SQLiteDatabase db = TopicTableHelper.getInstance(mContext).getWritableDatabase();
        return db.update(TopicTable.TABLE_NAME, topicTable.createContentValues(),
                TopicTable.Columns.UID+ " =? ", new String[]{String.valueOf(topicTable.mId)});
    }

    /**
     * 通过城市uid删除点赞信息
     * @param uid
     */
    public void deleteData(int uid) {
        SQLiteDatabase db = TopicTableHelper.getInstance(mContext).getWritableDatabase();
        db.delete(TopicTable.TABLE_NAME, TopicTable.Columns.UID + "=? ", new String[]{String.valueOf(uid)});
    }



    /**
     *   通过ID
     * @param id
     * @return
     */
    public TopicTable getInforByCode(int id) {
        SQLiteDatabase db = TopicTableHelper.getInstance(mContext).getReadableDatabase();
        Cursor cursor = db.query(TopicTable.TABLE_NAME, null, TopicTable.Columns.UID + " =? ",
                new String[]{String.valueOf(id)}, null, null, null);
        if (cursor == null) {
            return null;
        }
        TopicTable topicTable = null;
        if (cursor.moveToNext()) {
            topicTable = new TopicTable().fromCursor(cursor);
        }

        cursor.close();
        return topicTable;
    }


}
