package com.vladc.android.mobileerptool.shared.service;

import android.content.Context;
import android.util.Log;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.data.db.entities.Product;
import com.vladc.android.mobileerptool.data.db.impl.ProductDbDaoImpl;
import com.vladc.android.mobileerptool.data.rest.impl.ProductRestDaoImpl;
import com.vladc.android.mobileerptool.data.transform.ProductTransformer;
import com.vladc.android.mobileerptool.data.transform.Transformer;
import com.vladc.android.mobileerptool.shared.service.dto.ProductDto;
import com.vladc.android.mobileerptool.util.NetworkUtil;

import java.util.List;

/**
 * Created by Vlad.
 */
public class ProductServiceImpl {

    private final static String TAG = ProductServiceImpl.class.getSimpleName();

    private Context context;
    private ProductDbDaoImpl productDbDao;
    private ProductRestDaoImpl productRestDao;

    public ProductServiceImpl() {
        this(MobileERPApplication.getContext());
    }

    public ProductServiceImpl(Context context) {
        super();
        this.context = context;
        productDbDao = new ProductDbDaoImpl();
        productRestDao = new ProductRestDaoImpl();
    }

    public Product getLocalById(Long id){
        return productDbDao.get(id);
    }

    public Product getLocalByBarcode(String barcode){
        return productDbDao.findByBarcode(barcode);
    }

    public Product getProductCachedByBarcode(String barcode) {
        if (NetworkUtil.ifUp(context)) {
            Log.d(TAG, "Network is up, try to fetch students from server");
            try {
                ProductDto product = productRestDao.getByBarcode(barcode);
                Transformer<ProductDto, Product> transformer = new ProductTransformer();
                Product local = productDbDao.findByBarcode(barcode);
                if (local != null) {
                    long localId = local.getId();
                    local = transformer.transform(product);
                    local.setId(localId);
                    Log.d(TAG, "Updating product " + product.getName());
                    productDbDao.update(local);
                } else {
                    Log.d(TAG, "Creating product " + product.getName());
                    local = productDbDao.create(transformer.transform(product));
                }
            } catch (Exception e) {
                Log.e(TAG, "Error getting products from server", e);
            }
        } else {
            Log.d(TAG, "Network is down, fetching product from db");
        }
        return productDbDao.findByBarcode(barcode);
    }

    public List<Product> getAll() {
        return productDbDao.getAll();
    }
}
