package com.fixit.core.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.text.TextUtils;

import com.fixit.core.data.JobLocation;
import com.fixit.core.data.JobReason;
import com.fixit.core.data.Order;
import com.fixit.core.data.Profession;
import com.fixit.core.data.Tradesman;
import com.google.gson.Gson;

import java.util.Date;

/**
 * Created by Kostyantin on 7/16/2017.
 */

public class OrderDAO extends BaseDAO<Order> {

    public final static String TABLE_NAME = "Orders";

    public final static String KEY_ID = "_id";
    public final static String KEY_JOB_LOCATION = "jobLocation";
    public final static String KEY_PROFESSION = "profession";
    public final static String KEY_TRADESMEN = "tradesmen";
    public final static String KEY_JOB_REASONS = "jobReasons";
    public final static String KEY_CREATION_DATE = "creationDate";
    public final static String KEY_COMPLETE = "complete";

    public final static String CMD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_JOB_LOCATION + " TEXT, "
            + KEY_PROFESSION + " TEXT, "
            + KEY_TRADESMEN + " TEXT, "
            + KEY_JOB_REASONS + " TEXT, "
            + KEY_CREATION_DATE + " INTEGER, "
            + KEY_COMPLETE + " INTEGER);";

    private final Gson gson;

    public OrderDAO(DatabaseManager dbManager) {
        super(dbManager);
        this.gson = new Gson();
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }

    @Override
    public Class<Order> getEntityType() {
        return Order.class;
    }

    @Override
    protected ContentValues extractContentValues(Order obj) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, obj.getId());
        putJsonValue(cv, KEY_JOB_LOCATION, obj.getJobLocation());
        putJsonValue(cv, KEY_PROFESSION, obj.getProfession());
        putJsonValue(cv, KEY_TRADESMEN, obj.getTradesmen());
        putJsonValue(cv, KEY_JOB_REASONS, obj.getJobReasons());
        cv.put(KEY_CREATION_DATE, obj.getCreationDate().getTime());
        cv.put(KEY_COMPLETE, obj.isComplete() ? 1 : 0);

        return cv;
    }

    @Override
    protected Order getObject(Cursor c) {
        return new Order(
                (long) c.getInt(c.getColumnIndex(KEY_ID)),
                extractJsonValue(c, KEY_JOB_LOCATION, JobLocation.class),
                extractJsonValue(c, KEY_PROFESSION, Profession.class),
                extractJsonValue(c, KEY_TRADESMEN, Tradesman[].class),
                extractJsonValue(c, KEY_JOB_REASONS, JobReason[].class),
                new Date(c.getLong(c.getColumnIndex(KEY_CREATION_DATE))),
                c.getInt(c.getColumnIndex(KEY_COMPLETE)) == 1
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
