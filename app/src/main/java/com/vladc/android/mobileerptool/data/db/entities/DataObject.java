package com.vladc.android.mobileerptool.data.db.entities;

import java.io.Serializable;

/**
 * Created by Vlad.
 */
public abstract class DataObject<PK extends Serializable> implements Serializable {

    private PK id;

    public PK getId() {
        return id;
    }

    public void setId(PK key) {
        this.id = key;
    }


}
