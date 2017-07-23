package com.fixit.core.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.fixit.core.config.AppConfig;

/**
 * Created by Kostyantin on 7/29/2016.
 */
public class DatabaseManager extends SQLiteOpenHelper {

    public final static String DB_NAME = "lotto";

    private final static String DROP_TABLE = "DROP TABLE IF EXISTS ";

    private SQLiteDatabase mDatabase;

    private int dbOpenCounter = 0;

    public DatabaseManager(Context context) {
        super(context, DB_NAME, null, AppConfig.getInteger(context, AppConfig.KEY_DB_VERSION, 0));
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(ProfessionDAO.CMD_CREATE_TABLE);
        db.execSQL(OrderDAO.CMD_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DROP_TABLE + ProfessionDAO.TABLE_NAME);
        db.execSQL(DROP_TABLE + OrderDAO.TABLE_NAME);

        onCreate(db);
    }

    public synchronized SQLiteDatabase openDatabase() {
        dbOpenCounter++;
        if(dbOpenCounter == 1) {
            mDatabase = getWritableDatabase();
        }
        return mDatabase;
    }

    public synchronized void closeDatabase() {
        dbOpenCounter--;
        if(dbOpenCounter == 0) {
            mDatabase.close();
        }
    }
}
