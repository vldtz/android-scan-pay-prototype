package com.vladc.android.mobileerptool.shared.service.dto;

import com.vladc.android.mobileerptool.shared.service.BaseDto;

/**
 * Created by Vlad.
 */
public class ProductDto extends BaseDto<Long> {

    private String name;
    private String description;
    private String barcode;
    private String imageUrl;
    private Double price;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }
}
