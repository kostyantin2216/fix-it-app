package com.fixit.core.database;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fixit.core.data.DataModelObject;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Kostyantin on 6/6/2015.
 */
public abstract class BaseDAO<T extends DataModelObject> implements CommonDAO<T> {

    private final static String LOG_TAG = "#BaseDao";

    private final DatabaseManager dbManager;

    public BaseDAO(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    @Override
    public boolean insert(T obj) {
        SQLiteDatabase db = dbManager.openDatabase();

        ContentValues values = extractContentValues(obj);

        boolean createSuccessful = db.insert(getTableName(), null, values) != -1;

        dbManager.closeDatabase();

        return createSuccessful;
    }

    @Override
    public int insert(List<T> objs) {
        SQLiteDatabase db = dbManager.openDatabase();

        int success = 0;
        for(T obj : objs) {
            ContentValues values = extractContentValues(obj);

            if(db.insert(getTableName(), null, values) != -1) {
                success++;
            }
        }

        dbManager.closeDatabase();

        return success;
    }

    @Override
    public T findById(Integer id) {
        SQLiteDatabase db = dbManager.openDatabase();

        String where = getIdKey() + " = '" + id + "'";
        Cursor c = db.query(getTableName(), null, where, null, null, null, null);

        T obj = null;

        if(c.moveToFirst()) {
            obj = getObject(c);
        }

        c.close();
        dbManager.closeDatabase();

        return obj;
    }

    @Override
    public List<T> findByQuery(String query, String[] values) {
        SQLiteDatabase db = dbManager.openDatabase();

        Cursor c = db.rawQuery(query, values);

        List<T> objs = null;
        if(c.moveToFirst()) {
            objs = new ArrayList<>();
            do {
                objs.add(getObject(c));
            } while(c.moveToNext());
        }

        c.close();
        dbManager.closeDatabase();

        return objs;
    }

    @Override
    public T findOneByProperty(String property, String value) {
        List<T> results = findByProperty(property, value);
        if(results.size() > 0) {
            return results.get(0);
        }
        return null;
    }

    @Override
    public List<T> findByProperty(String property, String value) {
        SQLiteDatabase db = dbManager.openDatabase();

        String selection = property + " = ?";
        String[] selectionArgs = new String[] {value};
        Cursor c = db.query(getTableName(), null, selection, selectionArgs, null, null, null);

        List<T> objs = null;
        if(c.moveToFirst()) {
            objs = new ArrayList<>();
            do {
                objs.add(getObject(c));
            } while(c.moveToNext());
        }

        c.close();
        dbManager.closeDatabase();

        if(objs == null)  {
            objs = Collections.emptyList();
        }

        return objs;
    }

    @Override
    public List<T> findAll() {
        return findAll(null);
    }

    @Override
    public List<T> findAll(String orderBy) {
        SQLiteDatabase db = dbManager.openDatabase();

        Cursor c = db.query(getTableName(), null, null, null, null, null, orderBy);

        List<T> objs = null;
        if(c.moveToFirst()) {
            objs = new ArrayList<>();
            do {
                objs.add(getObject(c));
            } while(c.moveToNext());
        }

        c.close();
        dbManager.closeDatabase();

        if(objs == null)  {
            objs = Collections.emptyList();
        }

        return objs;
    }

    @Override
    public boolean update(T obj) {
        SQLiteDatabase db = dbManager.openDatabase();

        ContentValues values = extractContentValues(obj);

        boolean updateSuccesfull = db.update(getTableName(), values, getIdKey() + " = ?", new String[]{values.getAsString(getIdKey())}) > 0;

        dbManager.closeDatabase();

        return updateSuccesfull;
    }

    @Override
    public int updateAll(List<T> objs) {
        SQLiteDatabase db = dbManager.openDatabase();

        int failures = 0;
        for(T obj : objs) {
            ContentValues values = extractContentValues(obj);

            if(db.update(getTableName(), values, getIdKey() + " = ?", new String[]{values.getAsString(getIdKey())}) == 0) {
                failures++;
            }
        }

        dbManager.closeDatabase();

        return failures;
    }

    @Override
    public int truncate() {
        SQLiteDatabase db = dbManager.openDatabase();

        int deletes = db.delete(getTableName(), "1", null);

        dbManager.closeDatabase();

        return deletes;
    }

    @Override
    public boolean delete(String id) {
        SQLiteDatabase db = dbManager.openDatabase();

        boolean deleteSuccesfull = db.delete(getTableName(), getIdKey() + " = ?", new String[]{id}) != 0;

        dbManager.closeDatabase();

        return deleteSuccesfull;
    }

    @Override
    public boolean contains(Serializable id) {
        SQLiteDatabase db = dbManager.openDatabase();

        String query = "SELECT count(*) FROM " + getTableName() + " WHERE " + getIdKey() + " = '" + id + "'";
        Cursor c = db.rawQuery(query, null);

        boolean contains = false;
        if(c.moveToFirst()) {
            contains = c.getInt(0) > 0;
        }

        c.close();
        dbManager.closeDatabase();

        return contains;
    }

    @Override
    public int count() {
        SQLiteDatabase db = dbManager.openDatabase();

        String query = "SELECT count(*) FROM " + getTableName();
        Cursor c = db.rawQuery(query, null);

        int count = 0;
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }

        c.close();
        dbManager.closeDatabase();

        return count;
    }

    @Override
    public int countQuery(String query, String[] selectionArgs) {
        SQLiteDatabase db = dbManager.openDatabase();

        Cursor c = db.rawQuery(query, selectionArgs);

        int count = 0;
        if(c.moveToFirst()) {
            count = c.getInt(0);
        }

        c.close();
        dbManager.closeDatabase();

        return count;
    }

    protected abstract ContentValues extractContentValues(T obj);
    protected abstract T getObject(Cursor c);
    protected abstract String getIdKey();

    protected String getLogTag() {
        return "#" + getTableName() + "DAO";
    }

}
