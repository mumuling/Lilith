package com.youloft.lilith.topic.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import com.youloft.lilith.topic.bean.TopicDetailBean;

import java.io.Serializable;

/**
 *
 */

public class TopicTable implements Serializable {

    public static String TABLE_NAME = "topic_info";
    public int mId = -1;
    public String mTitle;
    public String mBackImg;
    public int mIsPost;
    public int mIsVote;
    public int option1_id = -1;
    public String option1_shortTitle;
    public String option1_title;
    public int option1_vote;
    public int option2_id = -1;
    public String option2_shortTitle;
    public String option2_title;
    public int option2_vote;
    public int vote_id;

    public TopicTable(TopicDetailBean.DataBean info,int vote_id){
        this.mId = info.id;
        this.mIsPost = 1;
        this.vote_id = vote_id;
        this.mBackImg = info.backImg;
        this.mIsVote = 1;
        this.mTitle = info.title;
        this.option1_id = info.option.get(0).id;
        this.option1_shortTitle = info.option.get(0).shortTitle;
        this.option1_title = info.option.get(0).title;
        this.option1_vote =info.option.get(1).vote;
        this.option2_id = info.option.get(1).id;
        this.option2_shortTitle = info.option.get(1).shortTitle;
        this.option2_title = info.option.get(1).title;
        this.option2_vote =info.option.get(1).vote;

    }
    public TopicTable(){}

    public static void createTable(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + TopicTable.Columns._ID + " INTEGER PRIMARY KEY,"
                + TopicTable.Columns.UID + " INTEGER,"
                + TopicTable.Columns.ISPOST + " INTEGER,"
                + TopicTable.Columns.TITLE + " TEXT,"
                + TopicTable.Columns.BACKIMG + " TEXT,"
                + TopicTable.Columns.IS_VOTE + " INTEGER,"
                + TopicTable.Columns.OPTION1_ID + " INTEGER,"
                + TopicTable.Columns.OPTION1_TITLE + " TEXT,"
                + TopicTable.Columns.OPTION1_SHORT_TITLE + " TEXT,"
                + TopicTable.Columns.OPTION2_ID + " INTEGER,"
                + TopicTable.Columns.OPTION2_TITLE + " TEXT,"
                + TopicTable.Columns.OPTION2_SHORT_TITLE + " TEXT,"
                + TopicTable.Columns.OPTION1_VOTE + " INTEGER,"
                + TopicTable.Columns.VOTE_ID + " INTEGER,"
                + TopicTable.Columns.OPTION2_VOTE + " INTEGER" + ");");
    }

    /**
     *    创建一条点赞的信息
     * @return
     */
    public ContentValues createContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Columns.UID, mId);
        contentValues.put(Columns.ISPOST, mIsPost);
        contentValues.put(Columns.TITLE, mTitle);
        contentValues.put(Columns.BACKIMG, mBackImg);
        contentValues.put(Columns.IS_VOTE, mIsVote);
        contentValues.put(Columns.OPTION1_ID,option1_id);
        contentValues.put(Columns.OPTION1_TITLE, option1_title);
        contentValues.put(Columns.OPTION1_SHORT_TITLE, option1_shortTitle);
        contentValues.put(Columns.OPTION1_VOTE,option1_vote);
        contentValues.put(Columns.OPTION2_ID, option2_id);
        contentValues.put(Columns.OPTION2_TITLE, option2_title);
        contentValues.put(Columns.OPTION2_SHORT_TITLE,option2_shortTitle);
        contentValues.put(Columns.OPTION2_VOTE,option2_vote);
        contentValues.put(Columns.VOTE_ID,vote_id);
        return contentValues;
    }

    public TopicTable fromCursor(Cursor cursor) {
        this.mId = cursor.getInt(cursor.getColumnIndex(Columns.UID));
        this.mTitle = cursor.getString(cursor.getColumnIndex(Columns.TITLE));
        this.mBackImg = cursor.getString(cursor.getColumnIndex(Columns.BACKIMG));
        this.mIsPost = cursor.getInt(cursor.getColumnIndex(Columns.ISPOST));
        this.mIsVote = cursor.getInt(cursor.getColumnIndex(Columns.IS_VOTE));
        this.option1_id = cursor.getInt(cursor.getColumnIndex(Columns.OPTION1_ID));
        this.option1_shortTitle = cursor.getString(cursor.getColumnIndex(Columns.OPTION1_SHORT_TITLE));
        this.option1_title = cursor.getString(cursor.getColumnIndex(Columns.OPTION1_TITLE));
        this.option1_vote = cursor.getInt(cursor.getColumnIndex(Columns.OPTION1_VOTE));
        this.option2_id = cursor.getInt(cursor.getColumnIndex(Columns.OPTION2_ID));
        this.option2_shortTitle = cursor.getString(cursor.getColumnIndex(Columns.OPTION2_SHORT_TITLE));
        this.option2_title = cursor.getString(cursor.getColumnIndex(Columns.OPTION2_TITLE));
        this.option2_vote = cursor.getInt(cursor.getColumnIndex(Columns.OPTION2_VOTE));
        this.vote_id =  cursor.getInt(cursor.getColumnIndex(Columns.VOTE_ID));

        return this;
    }

    public interface Columns extends BaseColumns {
        String UID = "tid";  //topic的ID
        String ISPOST = "post";//是否提交成功
        String TITLE = "title";
        String BACKIMG = "backimg";//背景图片
        String IS_VOTE = "is_vote";//是否投票
        String OPTION1_ID = "option1_id";
        String OPTION1_TITLE = "option1_title";
        String OPTION1_SHORT_TITLE = "option1_short_title";
        String OPTION1_VOTE = "option1_vote";
        String OPTION2_ID = "option2_id";
        String OPTION2_TITLE = "option2_title";
        String OPTION2_SHORT_TITLE = "option2_short_title";
        String OPTION2_VOTE = "option2_vote";
        String VOTE_ID = "vote_id";

    }
}
