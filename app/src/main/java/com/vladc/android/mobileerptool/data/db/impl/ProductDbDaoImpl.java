package com.vladc.android.mobileerptool.data.db.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.data.DbConstants;
import com.vladc.android.mobileerptool.data.db.entities.Product;

/**
 * Created by Vlad.
 */
public class ProductDbDaoImpl extends DatabaseEntityDaoImpl<Product> {

    public ProductDbDaoImpl() {
        super(MobileERPApplication.getDatabaseHelper());
    }

    public Product findByBarcode(String barcode) {
        String selection = DbConstants.ProductTable.COLUMN_BARCODE + "=?";
        String[] selectionArgs = new String[] {barcode};
        Cursor cursor = db.query(getTableName(), getColumns(), selection, selectionArgs, null, null, null);
        return loadUniqueAndCloseCursor(cursor);
    }

    @Override
    protected String getTableName() {
        return DbConstants.ProductTable.TABLE_NAME;
    }

    @Override
    protected String[] getColumns() {
        return new String[] {
                DbConstants.ProductTable.COLUMN_ID,
                DbConstants.ProductTable.COLUMN_NAME,
                DbConstants.ProductTable.COLUMN_DESCRIPTION,
                DbConstants.ProductTable.COLUMN_IMAGE_URL,
                DbConstants.ProductTable.COLUMN_BARCODE,
                DbConstants.ProductTable.COLUMN_PRICE,
                DbConstants.ProductTable.COLUMN_INGREDIENTS,
                DbConstants.ProductTable.COLUMN_PRODUCER,
                DbConstants.ProductTable.COLUMN_PRODUCER_URL,
                DbConstants.ProductTable.COLUMN_PROMOTIONS,
                DbConstants.ProductTable.COLUMN_STORING_CONDITIONS,
        };
    }

    @Override
    protected Product transform(Cursor c) {
        Product entity = new Product();
        entity.setId(c.getLong(0));
        entity.setName(c.getString(1));
        entity.setDescription(c.getString(2));
        entity.setImageUrl(c.getString(3));
        entity.setBarcode(c.getString(4));
        entity.setPrice(c.getFloat(5));
        entity.setIngredients(c.getString(6));
        entity.setProducer(c.getString(7));
        entity.setProducerUrl(c.getString(8));
        entity.setPromotions(c.getString(9));
        entity.setStoringConditions(c.getString(10));
        return entity;
    }

    @Override
    protected ContentValues transformBack(Product entity) {
        final ContentValues contentValues = new ContentValues(getColumns().length);
        //putValue(contentValues, DbConstants.StudentTable.COLUMN_ID, entity.getId());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_NAME, entity.getName());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_DESCRIPTION, entity.getDescription());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_INGREDIENTS, entity.getIngredients());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_PRODUCER, entity.getProducer());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_PRODUCER_URL, entity.getProducerUrl());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_PROMOTIONS, entity.getPromotions());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_STORING_CONDITIONS, entity.getStoringConditions());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_IMAGE_URL, entity.getImageUrl());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_BARCODE, entity.getBarcode());
        putValue(contentValues, DbConstants.ProductTable.COLUMN_PRICE, entity.getPrice());
        return contentValues;
    }
}
