package com.vladc.android.mobileerptool.dao.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.vladc.android.mobileerptool.dao.ProductImageDao;
import com.vladc.android.mobileerptool.dao.entity.ProductImage;
import com.vladc.android.mobileerptool.db.DbBitmapUtil;
import com.vladc.android.mobileerptool.db.MobileErpDbHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static com.vladc.android.mobileerptool.db.MobileErpContract.ProductImagesTable;

/**
 * Created by Vlad.
 */
public class ProductImageDaoImpl extends AbstractDaoImpl<Long,ProductImage> implements ProductImageDao {

    private Context mContext;
    private MobileErpDbHelper mDbHelper;
    private SQLiteDatabase mDb;

    public ProductImageDaoImpl(Context context) {
        mContext = context;
    }

    public ProductImageDaoImpl open() throws SQLException {
        mDbHelper = new MobileErpDbHelper(mContext);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

    @Override
    public ProductImage findById(Long id) {
        String selection = ProductImagesTable.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(id)};
        return fetchOne(selection, selectionArgs, null, null, null);
    }

    @Override
    public List<ProductImage> findByProductId(Long productId) {
        String selection = ProductImagesTable.COLUMN_NAME_PRODUCT_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productId)};
        return fetchList(selection, selectionArgs, null, null, null);
    }

    @Override
    public List<ProductImage> findAll() {
        return fetchList(null,null,null,null,null);
    }

    @Nullable
    private ProductImage fetchOne(String selection, String[] selectionArgs, String group, String filter, String sortOrder){
        Cursor cursor = mDb.query(
                ProductImagesTable.TABLE_NAME,                     // The table to query
                ProductImagesTable.COLUMNS,                               // The columns to return
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
    public Long insert(ProductImage productImage){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductImagesTable.COLUMN_NAME_PRODUCT_ID, productImage.getProductId());
        values.put(ProductImagesTable.COLUMN_NAME_IMAGE, DbBitmapUtil.getBytes(productImage.getImage()));
        values.put(ProductImagesTable.COLUMN_NAME_IMAGE_PATH, productImage.getImagePath());

        // Insert the new row, returning the primary key value of the new row
        long newRowId = mDb.insert(ProductImagesTable.TABLE_NAME, null, values);
        return newRowId;
    }

    @Override
    public Long update(ProductImage productImage){
        // Create a new map of values, where column names are the keys
        ContentValues values = new ContentValues();
        values.put(ProductImagesTable.COLUMN_NAME_PRODUCT_ID, productImage.getProductId());
        values.put(ProductImagesTable.COLUMN_NAME_IMAGE, DbBitmapUtil.getBytes(productImage.getImage()));
        values.put(ProductImagesTable.COLUMN_NAME_IMAGE_PATH, productImage.getImagePath());

        // Which row to update, based on the title
        String selection = ProductImagesTable.COLUMN_NAME_ID + " = ?";
        String[] selectionArgs = {String.valueOf(productImage.getId())};
        // Insert the new row, returning the primary key value of the new row
        long newRowId = mDb.update(ProductImagesTable.TABLE_NAME, values, selection, selectionArgs);
        return newRowId;
    }

    private List<ProductImage> fetchList(String selection, String[] selectionArgs, String group, String filter, String sortOrder){
        Cursor cursor = mDb.query(
                ProductImagesTable.TABLE_NAME,                     // The table to query
                ProductImagesTable.COLUMNS,                        // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                group,                                     // don't group the rows
                filter,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        if (cursor.moveToFirst()){
            List<ProductImage> entities = new ArrayList<>();
            do {
                entities.add(transform(cursor));
            } while (cursor.moveToNext());
            return entities;
        }

        return Collections.emptyList();
    }

    @NonNull
    @Override
    protected ProductImage transform(Cursor cursor) {
        ProductImage entity = new ProductImage();
        entity.setId(cursor.getLong(cursor.getColumnIndex(ProductImagesTable.COLUMN_NAME_ID)));
        entity.setProductId(cursor.getLong(cursor.getColumnIndex(ProductImagesTable.COLUMN_NAME_PRODUCT_ID)));
        entity.setImage(DbBitmapUtil.getImage(cursor.getBlob(cursor.getColumnIndex(ProductImagesTable.COLUMN_NAME_IMAGE))));
        entity.setImagePath(cursor.getString(cursor.getColumnIndex(ProductImagesTable.COLUMN_NAME_IMAGE_PATH)));

        return entity;
    }
}
