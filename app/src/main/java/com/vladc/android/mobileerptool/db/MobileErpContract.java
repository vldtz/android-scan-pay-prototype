package com.vladc.android.mobileerptool.db;

import android.provider.BaseColumns;

/**
 * Created by Vlad.
 */
public final class MobileErpContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INT_TYPE = " INT(10)";
    private static final String VARCHAR_TYPE = " VARCHAR(100)";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MobileErpContract() {}

    /* Inner class that defines the table contents */
    public static class ProductEntry implements BaseColumns {
        public static final String TABLE_NAME = "products";
        public static final String COLUMN_NAME_ID = "p_id";
        public static final String COLUMN_NAME_NAME = "p_name";
        public static final String COLUMN_NAME_BARCODE = "p_barcode";
        public static final String COLUMN_NAME_QUANTITY = "p_quantity";

        public static final String[] COLUMNS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_NAME,
                COLUMN_NAME_BARCODE,
                COLUMN_NAME_QUANTITY
        };

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + ProductEntry.TABLE_NAME + " (" +
                        ProductEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        ProductEntry.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        ProductEntry.COLUMN_NAME_BARCODE + VARCHAR_TYPE + COMMA_SEP +
                        ProductEntry.COLUMN_NAME_QUANTITY + INT_TYPE + " )";

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + ProductEntry.TABLE_NAME;
    }
}