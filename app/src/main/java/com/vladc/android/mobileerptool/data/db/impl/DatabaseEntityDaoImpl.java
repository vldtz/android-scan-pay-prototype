package com.vladc.android.mobileerptool.data.db.impl;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.vladc.android.mobileerptool.data.EntityDao;
import com.vladc.android.mobileerptool.data.MobileErpDbHelper;
import com.vladc.android.mobileerptool.data.db.entities.DataObject;
import com.vladc.android.mobileerptool.shared.service.BaseDto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

/**
 * Created by Vlad.
 */
public abstract class DatabaseEntityDaoImpl<T extends DataObject<Long>> implements EntityDao<T, Long> {

    protected SQLiteOpenHelper helper = null;
    protected SQLiteDatabase db = null;

    protected SQLiteOpenHelper getHelper() {
        return helper;
    }

    /**
     * @deprecated use {@link #DatabaseEntityDaoImpl(SQLiteOpenHelper)} instead!
     * @param context application context
     */
    @Deprecated
    public DatabaseEntityDaoImpl(Context context) {
        super();
        this.helper = new MobileErpDbHelper(context);
        this.db = this.helper.getWritableDatabase();
    }

    public DatabaseEntityDaoImpl(SQLiteOpenHelper helper) {
        super();
        this.helper = helper;
        this.db = this.helper.getWritableDatabase();
    }

    protected SQLiteDatabase openReadOnly() {
        return helper.getReadableDatabase();
    }

    protected SQLiteDatabase openReadWrite() {
        return db;
    }

    @Override
    public T get(Long id) {
        SQLiteDatabase db = openReadOnly();
        final String selection = getPKColumn() + " = ?";
        final Cursor cursor = db.query(getTableName(), getColumns(), selection, new String[] { String.valueOf(id) }, /* groupBy */null, /* having */null, null);
        return loadUniqueAndCloseCursor(cursor);
    }

    protected void closeCursor(final Cursor cursor) {
        if (cursor != null && !cursor.isClosed()) {
            cursor.close();
        }
    }

    @Override
    public List<T> getAll() {
        return readAll(null);
    }

    public List<T> readAll(String orderBy) {
        SQLiteDatabase db = openReadOnly();
        final Cursor cursor = db.query(getTableName(), getColumns(), null, null, /* groupBy */null, /* having */null, null);
        return loadAllAndCloseCursor(cursor);
    }

    @Override
    public T create(T entity) {
        if (entity.getId() != null) {
            throw new UnsupportedOperationException("Entity is already saved in database, maybe you need to update it?");
        }
        ContentValues values = transformBack(entity);
        db.beginTransaction();
        try {
            Long id = db.insertWithOnConflict(getTableName(), null, values, SQLiteDatabase.CONFLICT_ABORT);
            entity.setId(id);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
        return entity;
    }

    public Collection<Long> batchCreate(Collection<T> entities) {
        List<Long> primaryKeys = new ArrayList<Long>();
        for (T entity : entities) {
            primaryKeys.add(create(entity).getId());
        }
        return primaryKeys;
    }

    @Override
    public void update(T entity) {
        if (entity.getId() == null) {
            throw new UnsupportedOperationException("Entity must be saved in database before updating it");
        }
        String whereClause = getPKColumn() + "=?";
        String[] whereArgs = new String[] { String.valueOf(entity.getId()) };
        db.beginTransaction();
        try {
            db.updateWithOnConflict(getTableName(), transformBack(entity), whereClause, whereArgs, SQLiteDatabase.CONFLICT_ABORT);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    /**
     * Delete an entity
     */
    @Override
    public void delete(T entity) {
        if (entity.getId() == null) {
            throw new UnsupportedOperationException("Entity must be saved before deleting");
        }
        delete(entity.getId());
    }
    /**
     * Delete an entity by id
     */
    public void delete(Long entityId) {
        db.beginTransaction();
        try {
            db.delete(getTableName(), getPKColumn() + "=?", new String[] { String.valueOf(entityId) });
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }
    /**
     * @return the table name for this dao
     */
    protected abstract String getTableName();
    /**
     * Implement this to return the columns from the table {@link #getTableName()}.
     * To simplify your work, put PK column first!
     * @return list of columns
     */
    protected abstract String[] getColumns();
    /**
     * Loads an entity from a coursor
     * @param c db cursor
     * @return an entity extending {@link BaseDto}
     */
    protected abstract T transform(Cursor c);
    /**
     * Transform an entity to a {@link ContentValues} object
     * @param entity
     * @return
     */
    protected abstract ContentValues transformBack(T entity);
    /**
     * Returns the primary key column. Usually, this is the first column returned by {@link #getColumns()}.
     * If this is not your case, override this function
     * @return
     */
    protected String getPKColumn() {
        return getColumns()[0];
    }

    @Deprecated
    protected ContentValues transformBackForUpdate(T entity) {
        ContentValues contentValues = transformBack(entity);
        contentValues.remove(getPKColumn());
        return contentValues;
    }

    protected List<T> list(Cursor c) {
        List<T> results = new ArrayList<T>();
        while (c.moveToNext()) {
            results.add(transform(c));
        }
        return results;
    }

    public <S> S singleValue(Cursor c, Class<S> returnType) {
        if (c.getColumnCount() != 0) {
            throw new IllegalArgumentException("Query returned multiple columns, you cannot use this function");
        }
        return null;
    }

    /**
     * Add a value to {@link ContentValues}, taking care of nulls
     * @param c {@link ContentValues} object to add value to
     * @param key value key
     * @param value value object, may be null
     */
    protected void putValue(ContentValues c, String key, Object value) {
        if (value == null) {
            c.putNull(key);
        } else {
            putValueNotNull(c, key, value);
        }
    }

    /**
     * Add a value to {@link ContentValues}
     * @param c {@link ContentValues} object to add value to
     * @param key value key
     * @param value object value, may NOT be null
     */
    private void putValueNotNull(ContentValues c, String key, Object value) {
        if (value == null) {
            throw new IllegalArgumentException("Value must not be null");
        }
        if (value instanceof String) {
            c.put(key, (String) value);
        } else if (value instanceof Long) {
            c.put(key, (Long)value);
        } else if (value instanceof Date) {
            c.put(key, ((Date)value).getTime());
        } else if (value instanceof Integer) {
            c.put(key, (Integer)value);
        } else if (value instanceof Float) {
            c.put(key, (Float)value);
        } else if (value instanceof Byte) {
            c.put(key, (Byte)value);
        } else if (value instanceof Boolean) {
            c.put(key, ((Boolean)value) ? 1 : 0);
        } else if (value instanceof Short) {
            c.put(key, (Short)value);
        }
    }

    protected T loadUniqueAndCloseCursor(Cursor cursor) {
        try {
            return loadUnique(cursor);
        } finally {
            closeCursor(cursor);
        }
    }

    protected T loadUnique(Cursor cursor) {
        boolean available = cursor.moveToFirst();
        if (!available) {
            return null;
        } else if (!cursor.isLast()) {
            throw new RuntimeException("Expected unique result, multiple rows were found " + cursor.getCount());
        }
        return transform(cursor);
    }

    protected List<T> loadAllAndCloseCursor(Cursor cursor) {
        try {
            return loadAllFromCursor(cursor);
        } finally {
            closeCursor(cursor);
        }
    }

    protected List<T> loadAllFromCursor(Cursor cursor) {
        int count = cursor.getCount();
        List<T> list = new ArrayList<T>(count);
        if (cursor.moveToFirst()) {
            do {
                list.add(transform(cursor));
            } while (cursor.moveToNext());
        }
        return list;
    }

    public T merge(T entity) {
        T exiting = get(entity.getId());
        if (exiting == null) {
            create(entity);
        } else {
            update(entity);
        }
        return entity;
    }
    /**
     * Counts all rows in table
     * @return row count for table {@link #getTableName()}
     */
    public int countAll() {
        String sql = String.format("select count(%s) from %s", getPKColumn(), getTableName());
        Cursor cursor = db.rawQuery(sql, null);
        int count = 0;
        cursor.moveToNext();
        count = cursor.getInt(0);
        closeCursor(cursor);
        return count;
    }
}
