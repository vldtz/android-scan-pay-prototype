package com.vladc.android.mobileerptool.data.db.entities;

import java.util.Date;

/**
 * Created by Vlad.
 */
public class Cart extends DataObject<Long> {

    private Date dateCreated;
    private Date dateClosed;
    private String externalId;

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public Date getDateClosed() {
        return dateClosed;
    }

    public void setDateClosed(Date dateClosed) {
        this.dateClosed = dateClosed;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }
}
