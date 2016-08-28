package com.vladc.android.mobileerptool.dao;

import com.vladc.android.mobileerptool.dao.entity.Product;

/**
 * Created by Vlad.
 */
public interface ProductDao {

    Product findByBarcode(String barcode);

}
