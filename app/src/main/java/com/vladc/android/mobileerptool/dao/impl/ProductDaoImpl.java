package com.vladc.android.mobileerptool.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladc.android.mobileerptool.dao.ProductDao;
import com.vladc.android.mobileerptool.dao.entity.Product;
import com.vladc.android.mobileerptool.db.MobileErpDbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vladc.android.mobileerptool.db.MobileErpContract.ProductEntry;

/**
 * Created by Vlad.
 */
public class ProductDaoImpl extends AbstractDaoImpl<Long,Product> implements ProductDao {

    private Context mContext;
    private MobileErpDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    public ProductDaoImpl(Context context) {
        mContext = context;
    }

    public ProductDaoImpl open() throws SQLException {
        mDbHelper = new MobileErpDbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    @Override
    public Product findById(Long id) {
        String selection = ProductEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return fetchOne(selection, selectionArgs, null, null, null);
    }

    @Override
    public Product findByBarcode(String barcode) {
        String selection = ProductEntry.COLUMN_NAME_BARCODE + " = ?";
        String[] selectionArgs = {barcode};
        return fetchOne(selection, selectionArgs, null, null, null);
    }

    @Override
    public List<Product> findAll() {
        return fetchList(null,null,null,null,null);
    }

    @Nullable
    private Product fetchOne(String selection, String[] selectionArgs, String group, String filter, String sortOrder){
        Cursor cursor = mDb.query(
                ProductEntry.TABLE_NAME,                     // The table to query
                ProductEntry.COLUMNS,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                group,                                     // don't group the rows
                filter,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (cursor.moveToFirst()){
            return transform(cursor);
        }

        return null;
    }

    @Override
    public Long insert(Product product){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_BARCODE, product.getBarcode());
        values.put(ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductEntry.COLUMN_NAME_QUANTITY, product.getQuantity());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = mDb.insert(ProductEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    @Override
    public Long update(Product product){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_BARCODE, product.getBarcode());
        values.put(ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductEntry.COLUMN_NAME_QUANTITY, product.getQuantity());

        // Which row to update, based on the title
        String selection = ProductEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};
        // Insert the new row, returning the primary key value of the new row
        long newRowId = mDb.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        return newRowId;
    }

    private List<Product> fetchList(String selection, String[] selectionArgs, String group, String filter, String sortOrder){
        Cursor cursor = mDb.query(
                ProductEntry.TABLE_NAME,                     // The table to query
                ProductEntry.COLUMNS,                        // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                group,                                     // don't group the rows
                filter,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (cursor.moveToFirst()){
            List<Product> entities = new ArrayList<>();
            do {
                entities.add(transform(cursor));
            } while (cursor.moveToNext());
            return entities;
        }

        return Collections.emptyList();
    }

    @NonNull
    @Override
    protected Product transform(Cursor cursor) {
        Product entity = new Product();
        entity.setId(cursor.getLong(cursor.getColumnIndex(ProductEntry.COLUMN_NAME_ID)));
        entity.setName(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_NAME_NAME)));
        entity.setBarcode(cursor.getString(cursor.getColumnIndex(ProductEntry.COLUMN_NAME_BARCODE)));
        entity.setQuantity(cursor.getLong(cursor.getColumnIndex(ProductEntry.COLUMN_NAME_QUANTITY)));

        return entity;
    }
}
