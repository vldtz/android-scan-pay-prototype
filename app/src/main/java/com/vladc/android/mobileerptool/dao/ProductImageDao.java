package com.vladc.android.mobileerptool.dao;

import com.vladc.android.mobileerptool.dao.entity.ProductImage;

/**
 * Created by Vlad.
 */
public interface ProductImageDao {
    ProductImage findByProductId(Long productId);
}
