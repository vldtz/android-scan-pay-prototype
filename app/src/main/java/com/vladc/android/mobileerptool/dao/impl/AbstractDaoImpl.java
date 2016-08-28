package com.vladc.android.mobileerptool.dao.impl;

import android.database.Cursor;

import com.vladc.android.mobileerptool.dao.AbstractDao;

import java.util.List;

/**
 * Created by Vlad.
 */
public abstract class AbstractDaoImpl<PK, T> implements AbstractDao<T> {

    public abstract T findById(PK id);

    public abstract PK insert(T product);

    public abstract PK update(T product);

    public abstract List<T> findAll();

    protected abstract T transform (Cursor c);
}
