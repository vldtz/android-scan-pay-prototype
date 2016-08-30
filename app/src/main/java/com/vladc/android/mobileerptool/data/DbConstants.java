package com.vladc.android.mobileerptool.data;

public class DbConstants {

    private DbConstants() {
        super();
    }

    public static class ProductTable {
        public static final String TABLE_NAME = "products";

        public static final String COLUMN_ID = "p_id";
        public static final String COLUMN_NAME = "p_name";
        public static final String COLUMN_DESCRIPTION = "p_description";
        public static final String COLUMN_IMAGE_URL = "p_image_url";
        public static final String COLUMN_BARCODE = "p_barcode";
        public static final String COLUMN_PRICE = "p_price";

        public static final String TABLE_NAME_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME + " TEXT, " +
                COLUMN_DESCRIPTION + " TEXT, " +
                COLUMN_BARCODE + " TEXT, " +
                COLUMN_IMAGE_URL + " TEXT, " +
                COLUMN_PRICE + " DECIMAL(10,2) " +
            " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class CartTable {
        public static final String TABLE_NAME = "cart";

        public static final String COLUMN_ID = "c_id";
        public static final String COLUMN_DATE = "c_date";
        public static final String COLUMN_CLOSED_DATE = "c_closed_date";
        public static final String COLUMN_EXTERNAL_ID = "c_external_id";

        public static final String TABLE_NAME_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_DATE + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                COLUMN_CLOSED_DATE + " TIMESTAMP DEFAULT NULL, " +
                COLUMN_EXTERNAL_ID + " TEXT " +
            " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }

    public static class ProductToCartTable {
        public static final String TABLE_NAME = "products_to_cart";

        public static final String COLUMN_ID = "pc_id";
        public static final String COLUMN_CART_ID = "pc_c_id";
        public static final String COLUMN_PRODUCT_ID = "pc_p_id";
        public static final String COLUMN_QUANTITY = "pc_quantity";

        public static final String[] COLUMNS = {
                COLUMN_ID,
                COLUMN_CART_ID,
                COLUMN_PRODUCT_ID,
                COLUMN_QUANTITY
        };

        public static final String TABLE_NAME_CREATE = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_CART_ID + " INTEGER, " +
                COLUMN_PRODUCT_ID + " INTEGER, " +
                COLUMN_QUANTITY + " INTEGER DEFAULT 1, " +
                "FOREIGN KEY(" + COLUMN_CART_ID + ") REFERENCES " + CartTable.TABLE_NAME + "(" + CartTable.COLUMN_ID + ")  ON DELETE CASCADE, " +
                "FOREIGN KEY(" + COLUMN_PRODUCT_ID + ") REFERENCES " + ProductTable.TABLE_NAME + "(" + ProductTable.COLUMN_ID + ")  ON DELETE CASCADE" +
            " )";

        public static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
    
    public static void main(String[] args) {
        System.out.println(ProductTable.TABLE_NAME_CREATE);
        System.out.println(CartTable.TABLE_NAME_CREATE);
        System.out.println(ProductToCartTable.TABLE_NAME_CREATE);
    }

}
