package com.vladc.android.mobileerptool.dao;

import com.vladc.android.mobileerptool.dao.entity.Product;

import java.util.List;

/**
 * Created by Vlad.
 */
public interface ProductDao {

    Product findByBarcode(String barcode);

    List<Product> findAll();
}
