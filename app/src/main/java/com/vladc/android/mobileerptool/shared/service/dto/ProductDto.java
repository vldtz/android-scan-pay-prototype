package com.vladc.android.mobileerptool.shared.service.dto;

import com.vladc.android.mobileerptool.shared.service.BaseDto;

/**
 * Created by Vlad.
 */
public class ProductDto extends BaseDto<Long> {

    private String name;
    private String description;
    private String ingredients;
    private String promotions;
    private String producer;
    private String producerUrl;
    private String storingConditions;
    private String barcode;
    private String imageUrl;
    private Float price;

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

    public Float getPrice() {
        return price;
    }

    public void setPrice(Float price) {
        this.price = price;
    }

    public String getIngredients() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients = ingredients;
    }

    public String getPromotions() {
        return promotions;
    }

    public void setPromotions(String promotions) {
        this.promotions = promotions;
    }

    public String getProducer() {
        return producer;
    }

    public void setProducer(String producer) {
        this.producer = producer;
    }

    public String getProducerUrl() {
        return producerUrl;
    }

    public void setProducerUrl(String producerUrl) {
        this.producerUrl = producerUrl;
    }

    public String getStoringConditions() {
        return storingConditions;
    }

    public void setStoringConditions(String storingConditions) {
        this.storingConditions = storingConditions;
    }
}
