package com.vladc.android.mobileerptool.db;

import android.provider.BaseColumns;

/**
 * Created by Vlad.
 */
public final class MobileErpContract {
    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String VARCHAR_TYPE = " VARCHAR(100)";
    private static final String BLOB_TYPE = " BLOB";
    private static final String UNIQUE = " UNIQUE";
    private static final String COMMA_SEP = ",";

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private MobileErpContract() {
    }

    /* Inner class that defines the table contents */
    public static class ProductsTable implements BaseColumns {
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
                "CREATE TABLE " + ProductsTable.TABLE_NAME + " (" +
                        ProductsTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        ProductsTable.COLUMN_NAME_NAME + TEXT_TYPE + COMMA_SEP +
                        ProductsTable.COLUMN_NAME_BARCODE + VARCHAR_TYPE + UNIQUE + COMMA_SEP +
                        ProductsTable.COLUMN_NAME_QUANTITY + INTEGER_TYPE + " )";

        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + ProductsTable.TABLE_NAME;
    }

    public static class ProductImagesTable implements BaseColumns {
        public static final String TABLE_NAME = "product_images";
        public static final String COLUMN_NAME_ID = "pi_id";
        public static final String COLUMN_NAME_PRODUCT_ID = "pi_p_id";
        public static final String COLUMN_NAME_IMAGE = "pi_image_data";
        public static final String COLUMN_NAME_IMAGE_PATH = "pi_image_path";

        public static final String[] COLUMNS = {
                COLUMN_NAME_ID,
                COLUMN_NAME_PRODUCT_ID,
                COLUMN_NAME_IMAGE,
                COLUMN_NAME_IMAGE_PATH
        };

        public static final String SQL_CREATE_TABLE =
                "CREATE TABLE " + ProductImagesTable.TABLE_NAME + " (" +
                        ProductImagesTable.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                        ProductImagesTable.COLUMN_NAME_PRODUCT_ID + INTEGER_TYPE + COMMA_SEP +
                        ProductImagesTable.COLUMN_NAME_IMAGE_PATH + TEXT_TYPE + COMMA_SEP +
                        ProductImagesTable.COLUMN_NAME_IMAGE + BLOB_TYPE + COMMA_SEP +
                        "FOREIGN KEY(" + ProductImagesTable.COLUMN_NAME_PRODUCT_ID + ") " +
                        "REFERENCES " + ProductsTable.TABLE_NAME + "(" + ProductsTable.COLUMN_NAME_ID + ")" +
                        ")";

        public static final String SQL_DROP_TABLE =
                "DROP TABLE IF EXISTS " + ProductImagesTable.TABLE_NAME;
    }
}