package com.fixit.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.fixit.data.JobLocation;
import com.fixit.data.OrderData;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Kostyantin on 7/16/2017.
 */

public class OrderDataDAO extends BaseDAO<OrderData> {

    public final static String TABLE_NAME = "Orders";

    public final static String KEY_ID = "id";
    public final static String KEY_JOB_LOCATION = "jobLocation";
    public final static String KEY_PROFESSION_ID = "professionId";
    public final static String KEY_TRADESMEN = "tradesmen";
    public final static String KEY_JOB_REASONS = "jobReasons";
    public final static String KEY_COMMENT = "comment";
    public final static String KEY_FEEDBACK_PROVIDED = "feedbackProvided";
    public final static String KEY_CREATED_AT = "createdAt";

    public final static String CMD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + KEY_ID + " TEXT PRIMARY KEY, "
            + KEY_JOB_LOCATION + " TEXT, "
            + KEY_PROFESSION_ID + " INTEGER, "
            + KEY_TRADESMEN + " TEXT, "
            + KEY_JOB_REASONS + " TEXT, "
            + KEY_COMMENT + " TEXT, "
            + KEY_FEEDBACK_PROVIDED + " INTEGER, "
            + KEY_CREATED_AT + " INTEGER);";

    private final Gson gson;

    public OrderDataDAO(DatabaseManager dbManager) {
        super(dbManager);
        this.gson = new Gson();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<OrderData> getEntityType() {
        return OrderData.class;
    }

    @Override
    protected ContentValues extractContentValues(OrderData obj) {
        ContentValues cv = new ContentValues();

        cv.put(KEY_ID, obj.get_id());
        cv.put(KEY_PROFESSION_ID, obj.getProfessionId());
        cv.put(KEY_COMMENT, obj.getComment());
        cv.put(KEY_FEEDBACK_PROVIDED, obj.isFeedbackProvided() ? 1 : 0);
        cv.put(KEY_CREATED_AT, obj.getCreatedAt().getTime());

        putJsonValue(cv, KEY_JOB_LOCATION, obj.getLocation());
        putJsonValue(cv, KEY_TRADESMEN, obj.getTradesmen());
        putJsonValue(cv, KEY_JOB_REASONS, obj.getJobReasons());

        return cv;
    }

    @Override
    protected OrderData getObject(Cursor c) {
        return new OrderData(
                c.getString(c.getColumnIndex(KEY_ID)),
                extractJsonValue(c, KEY_TRADESMEN, String[].class),
                c.getLong(c.getColumnIndex(KEY_PROFESSION_ID)),
                extractJsonValue(c, KEY_JOB_LOCATION, JobLocation.class),
                extractJsonValue(c, KEY_JOB_REASONS, long[].class),
                c.getString(c.getColumnIndex(KEY_COMMENT)),
                c.getInt(c.getColumnIndex(KEY_FEEDBACK_PROVIDED)) == 1,
                new Date(c.getLong(c.getColumnIndex(KEY_CREATED_AT)))
        );
    }

    private <V> void putJsonValue(ContentValues cv, String key, V value) {
        String json = null;
        if(value != null) {
            json = gson.toJson(value);
        }
        cv.put(key, json);
    }

    private <V> V extractJsonValue(Cursor c, String key, Class<V> type) {
        V result = null;

        String json = c.getString(c.getColumnIndex(key));
        if(!TextUtils.isEmpty(json)) {
            result = gson.fromJson(json, type);
        }

        return result;
    }

    @Override
    protected String getIdKey() {
        return KEY_ID;
    }
}
