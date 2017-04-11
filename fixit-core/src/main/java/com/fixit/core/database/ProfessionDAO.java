package com.fixit.core.database;

import android.content.ContentValues;
import android.database.Cursor;

import com.fixit.core.data.Profession;
import com.fixit.core.utils.CommonUtils;

import java.util.Date;

/**
 * Created by konstantin on 3/29/2017.
 */

public class ProfessionDAO extends BaseDAO<Profession> {

    public final static String TABLE_NAME = "Profession";

    public final static String KEY_ID = "id";
    public final static String KEY_NAME = "name";
    public final static String KEY_DESCRIPTION = "description";
    public final static String KEY_IMAGE_URL = "imageUrl";
    public final static String KEY_IS_ACTIVE = "isActive";
    public final static String KEY_UPDATED_AT = "updatedAt";

    public final static String CMD_CREATE_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME + "("
            + KEY_ID + " INTEGER PRIMARY KEY, "
            + KEY_NAME + " TEXT, "
            + KEY_DESCRIPTION + " TEXT, "
            + KEY_IMAGE_URL + " TEXT, "
            + KEY_IS_ACTIVE + " INTEGER, "
            + KEY_UPDATED_AT + " INTEGER);";

    public ProfessionDAO(DatabaseManager dbManager) {
        super(dbManager);
    }

    @Override
    protected ContentValues extractContentValues(Profession obj) {
        ContentValues cv = new ContentValues();
        cv.put(KEY_ID, obj.getId());
        cv.put(KEY_NAME, obj.getName());
        cv.put(KEY_DESCRIPTION, obj.getDescription());
        cv.put(KEY_IMAGE_URL, obj.getImageUrl());
        cv.put(KEY_IS_ACTIVE, obj.getActive() ? 1 : 0);
        cv.put(KEY_UPDATED_AT, obj.getUpdatedAt().getTime());
        return cv;
    }

    @Override
    protected Profession getObject(Cursor c) {
        return new Profession(
                c.getInt(c.getColumnIndex(KEY_ID)),
                c.getString(c.getColumnIndex(KEY_NAME)),
                c.getString(c.getColumnIndex(KEY_DESCRIPTION)),
                c.getString(c.getColumnIndex(KEY_IMAGE_URL)),
                c.getInt(c.getColumnIndex(KEY_IS_ACTIVE)) == 1,
                new Date(c.getLong(c.getColumnIndex(KEY_UPDATED_AT)))
        );
    }

    public Profession findProfessionByName(String name) {
        return findOneByProperty(KEY_NAME, CommonUtils.capitalize(name));
    }

    @Override
    protected String getIdKey() {
        return KEY_ID;
    }

    @Override
    public String getTableName() {
        return TABLE_NAME;
    }
}
