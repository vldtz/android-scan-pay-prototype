package com.vladc.android.mobileerptool.data;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Vlad.
 */
public interface EntityDao<T extends Serializable, PK extends Serializable> {

    T get(PK id);

    T create(T entity);

    void update(T entity);

    List<T> getAll();

    void delete(T entity);
}
