package com.vladc.android.mobileerptool.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Vlad.
 */
public class MobileErpDbHelper extends SQLiteOpenHelper {
    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 2;
    public static final String DATABASE_NAME = "MobileErp.db";

    public MobileErpDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(MobileErpContract.ProductsTable.SQL_CREATE_TABLE);
        db.execSQL(MobileErpContract.ProductImagesTable.SQL_CREATE_TABLE);
    }
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion == newVersion) return;

        switch (newVersion){
            case 2:
                db.execSQL(MobileErpContract.ProductsTable.SQL_DROP_TABLE);
                db.execSQL(MobileErpContract.ProductImagesTable.SQL_DROP_TABLE);
                onCreate(db);
                break;
            default: return;
        }
    }
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }
}
