package com.vladc.android.mobileerptool.dao.entity;

import android.graphics.Bitmap;

/**
 * Created by Vlad.
 */
public class ProductImage {

    private Long id;
    private Long productId;
    private Product product;
    private Bitmap image;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
