package com.fixit.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.fixit.data.JobReason;

import java.util.Date;

/**
 * Created by Kostyantin on 8/27/2017.
 */

public class JobReasonDAO extends BaseDAO<JobReason> {

    public final static String TABLE_NAME = "JobReasons";

    public final static String KEY_ID = "id";
    public final static String KEY_PROFESSION_ID = "professionId";
    public final static String KEY_NAME = "name";
    public final static String KEY_COMMENT = "comment";
    public final static String KEY_UPDATED_AT = "updatedAt";

    public final static String CMD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_PROFESSION_ID + " INTEGER, "
            + KEY_NAME + " TEXT, "
            + KEY_COMMENT + " TEXT, "
            + KEY_UPDATED_AT + " INTEGER);";

    public JobReasonDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<JobReason> getEntityType() {
        return JobReason.class;
    }

    @Override
    protected ContentValues extractContentValues(JobReason obj) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, obj.getId());
        cv.put(KEY_PROFESSION_ID, obj.getProfessionId());
        cv.put(KEY_NAME, obj.getName());
        cv.put(KEY_COMMENT, obj.getComment());
        cv.put(KEY_UPDATED_AT, obj.getUpdatedAt().getTime());
        return cv;
    }

    @Override
    protected JobReason getObject(Cursor c) {
        JobReason jobReason = new JobReason();
        jobReason.setId(c.getInt(c.getColumnIndex(KEY_ID)));
        jobReason.setProfessionId(c.getColumnIndex(KEY_PROFESSION_ID));
        jobReason.setName(c.getString(c.getColumnIndex(KEY_NAME)));
        jobReason.setComment(c.getString(c.getColumnIndex(KEY_COMMENT)));
        jobReason.setUpdatedAt(new Date(c.getLong(c.getColumnIndex(KEY_UPDATED_AT))));
        return jobReason;
    }

    @Override
    protected String getIdKey() {
        return KEY_ID;
    }
}
