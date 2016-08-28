package com.vladc.android.mobileerptool.dao;

import com.vladc.android.mobileerptool.dao.entity.ProductImage;

import java.util.List;

/**
 * Created by Vlad.
 */
public interface ProductImageDao {
    List<ProductImage> findByProductId(Long productId);
}
