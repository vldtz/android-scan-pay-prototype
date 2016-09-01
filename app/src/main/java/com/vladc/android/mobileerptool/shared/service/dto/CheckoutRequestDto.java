package com.vladc.android.mobileerptool.shared.service.dto;

import com.vladc.android.mobileerptool.shared.service.BaseDto;

import java.util.Map;

/**
 * Created by Vlad.
 */
public class CheckoutRequestDto extends BaseDto<Long>{

    Map<String,Long> products;

    public Map<String, Long> getProducts() {
        return products;
    }

    public void setProducts(Map<String, Long> products) {
        this.products = products;
    }
}
