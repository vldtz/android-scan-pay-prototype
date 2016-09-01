package com.vladc.android.mobileerptool.data.db.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.data.DbConstants;
import com.vladc.android.mobileerptool.data.db.entities.ProductToCart;

import java.util.List;

/**
 * Created by Vlad.
 */
public class ProductToCartDbDaoImpl extends DatabaseEntityDaoImpl<ProductToCart> {

    public ProductToCartDbDaoImpl() {
        super(MobileERPApplication.getDatabaseHelper());
    }

    @Override
    protected String getTableName() {
        return DbConstants.ProductToCartTable.TABLE_NAME;
    }

    @Override
    protected String[] getColumns() {
        return new String[] {
                DbConstants.ProductToCartTable.COLUMN_ID,
                DbConstants.ProductToCartTable.COLUMN_CART_ID,
                DbConstants.ProductToCartTable.COLUMN_PRODUCT_ID,
                DbConstants.ProductToCartTable.COLUMN_QUANTITY,
        };
    }

    @Override
    protected ProductToCart transform(Cursor c) {
        ProductToCart entity = new ProductToCart();
        entity.setId(c.getLong(0));
        entity.setCartId(c.getLong(1));
        entity.setProductId(c.getLong(2));
        entity.setQuantity(c.getLong(3));
        return entity;
    }

    @Override
    protected ContentValues transformBack(ProductToCart entity) {
        final ContentValues contentValues = new ContentValues(getColumns().length);
        //putValue(contentValues, DbConstants.StudentTable.COLUMN_ID, entity.getId());
        putValue(contentValues, DbConstants.ProductToCartTable.COLUMN_CART_ID, entity.getCartId());
        putValue(contentValues, DbConstants.ProductToCartTable.COLUMN_PRODUCT_ID, entity.getProductId());
        putValue(contentValues, DbConstants.ProductToCartTable.COLUMN_QUANTITY, entity.getQuantity());
        return contentValues;
    }

    public List<ProductToCart> getByCartId(Long cartId) {
        String selection = DbConstants.ProductToCartTable.COLUMN_CART_ID + "=?";
        String[] selectionArgs = new String[] {cartId + ""};
        Cursor cursor = db.query(getTableName(), getColumns(), selection, selectionArgs, null, null, null);
        return loadAllAndCloseCursor(cursor);
    }

    public ProductToCart getForProductAndCart(Long productId, Long cartId) {
        String selection = DbConstants.ProductToCartTable.COLUMN_CART_ID + "=? AND " + DbConstants.ProductToCartTable.COLUMN_PRODUCT_ID + "=?";
        String[] selectionArgs = new String[] {cartId + "", productId + ""};
        Cursor cursor = db.query(getTableName(), getColumns(), selection, selectionArgs, null, null, null);
        return loadUniqueAndCloseCursor(cursor);
    }
}
