package com.vladc.android.mobileerptool.shared.service.dto;

import com.vladc.android.mobileerptool.shared.service.BaseDto;

import java.util.Date;

/**
 * Created by Vlad.
 */
public class CheckoutResponseDto extends BaseDto<String>{

    private Date date;
    private Float total;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Float getTotal() {
        return total;
    }

    public void setTotal(Float total) {
        this.total = total;
    }
}
