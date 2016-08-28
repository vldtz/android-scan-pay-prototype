package com.vladc.android.mobileerptool.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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

    MobileErpDbHelper mDbHelper;

    ProductDaoImpl(Context context) {
        mDbHelper = new MobileErpDbHelper(context);
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
        return null;
    }

    @Nullable
    private Product fetchOne(String selection, String[] selectionArgs, String group, String filter, String sortOrder){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
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
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_BARCODE, product.getBarcode());
        values.put(ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductEntry.COLUMN_NAME_NAME, product.getQuantity());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.insert(ProductEntry.TABLE_NAME, null, values);
        return newRowId;
    }

    @Override
    public Long update(Product product){
        // Gets the data repository in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductEntry.COLUMN_NAME_BARCODE, product.getBarcode());
        values.put(ProductEntry.COLUMN_NAME_NAME, product.getName());
        values.put(ProductEntry.COLUMN_NAME_NAME, product.getQuantity());

        // Which row to update, based on the title
        String selection = ProductEntry.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(product.getId())};
        // Insert the new row, returning the primary key value of the new row
        long newRowId = db.update(ProductEntry.TABLE_NAME, values, selection, selectionArgs);
        return newRowId;
    }

    private List<Product> fetchList(String selection, String[] selectionArgs, String group, String filter, String sortOrder){
        SQLiteDatabase db = mDbHelper.getReadableDatabase();
        Cursor cursor = db.query(
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
