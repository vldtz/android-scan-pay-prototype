package com.vladc.android.mobileerptool.data.db.impl;

import android.content.ContentValues;
import android.database.Cursor;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.data.DbConstants;
import com.vladc.android.mobileerptool.data.db.entities.Cart;

import java.util.Date;

/**
 * Created by Vlad.
 */
public class CartDbDaoImpl extends DatabaseEntityDaoImpl<Cart> {

    public CartDbDaoImpl() {
        super(MobileERPApplication.getDatabaseHelper());
    }

    @Override
    protected String getTableName() {
        return DbConstants.CartTable.TABLE_NAME;
    }

    @Override
    protected String[] getColumns() {
        return new String[] {
                DbConstants.CartTable.COLUMN_ID,
                DbConstants.CartTable.COLUMN_DATE,
                DbConstants.CartTable.COLUMN_CLOSED_DATE,
        };
    }

    @Override
    protected Cart transform(Cursor c) {
        Cart entity = new Cart();
        entity.setId(c.getLong(0));
        entity.setDateCreated(new Date(c.getLong(1)));
        entity.setDateClosed(!c.isNull(2) ? new Date(c.getLong(2)) : null);
        return entity;
    }

    @Override
    protected ContentValues transformBack(Cart entity) {
        final ContentValues contentValues = new ContentValues(getColumns().length);
        //putValue(contentValues, DbConstants.StudentTable.COLUMN_ID, entity.getId());
        putValue(contentValues, DbConstants.CartTable.COLUMN_DATE, entity.getDateCreated());
        putValue(contentValues, DbConstants.CartTable.COLUMN_CLOSED_DATE, entity.getDateClosed());
        return contentValues;
    }
}
