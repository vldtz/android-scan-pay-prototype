package com.vladc.android.mobileerptool.shared.service;

import java.io.Serializable;

/**
 * Created by Vlad.
 */
public abstract class BaseDto<PK extends Serializable> implements Serializable {

    private PK id;

    public BaseDto() {
        super();
    }

    public BaseDto(PK id) {
        this();
        this.id = id;
    }

    public PK getId() {
        return this.id;
    }

    public void setId(PK id) {
        this.id = id;
    }
}
