package com.vladc.android.mobileerptool.activity;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.dao.entity.Product;
import com.vladc.android.mobileerptool.dao.impl.ProductDaoImpl;
import com.vladc.android.mobileerptool.fragment.ProductDetailFragment;

public class AddEditProductActivity extends AppCompatActivity {

    ProductDaoImpl productDao;
    EditText mIdField;
    EditText mNameField;
    EditText mBarcodeField;
    EditText mQuantityField;

    Product mProduct;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        productDao = new ProductDaoImpl(this);
        setContentView(R.layout.activity_add_edit_product);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mIdField = (EditText) findViewById(R.id.field_id);
        mNameField = (EditText) findViewById(R.id.field_name);
        mBarcodeField = (EditText) findViewById(R.id.field_barcode);
        mQuantityField = (EditText) findViewById(R.id.field_quantity);
//        mQuantityField.getText().

        mIdField.setEnabled(false);
        mBarcodeField.setEnabled(false);

        if (getIntent().hasExtra(ProductDetailFragment.ARG_ITEM_ID)){
            mProduct = loadProduct(getIntent().getLongExtra(ProductDetailFragment.ARG_ITEM_ID,0L));
        } else if (getIntent().hasExtra(ProductDetailFragment.ARG_ITEM_BARCODE)){
            mProduct = findOrCreateProduct(getIntent().getStringExtra(ProductDetailFragment.ARG_ITEM_BARCODE));
        }

        if (mProduct != null){
            mIdField.setText(String.valueOf(mProduct.getId()));
            mBarcodeField.setText(mProduct.getBarcode());
            mNameField.setText(mProduct.getName());
            mQuantityField.setText(String.valueOf(mProduct.getQuantity()));
        } else {
            mIdField.setText("");
            mBarcodeField.setText("");
            mNameField.setText("");
            mQuantityField.setText("0");
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    saveProduct();
                    CharSequence text = "Produs salvat";
                    Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
                    onBackPressed();
                } catch (Exception e){
                    Snackbar.make(view, "Eroare", Snackbar.LENGTH_LONG).setAction("Action",null).show();
                }
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    protected void saveProduct(){
        productDao.open();
        if (mProduct != null){
            mProduct.setName(mNameField.getText().toString());
            mProduct.setQuantity(Long.parseLong(mQuantityField.getText().toString()));
            productDao.update(mProduct);
        } else {
            Product p = new Product();
            p.setBarcode(mBarcodeField.getText().toString());
            p.setName(mNameField.getText().toString());
            p.setQuantity(Long.parseLong(mQuantityField.getText().toString()));
            long newId = productDao.insert(p);

            mProduct = loadProduct(newId);
        }

        productDao.close();
    }

    protected Product loadProduct(Long id){
        productDao.open();

        return productDao.findById(id);
    }

    protected Product findOrCreateProduct(String barcode){
        productDao.open();

        Product p = productDao.findByBarcode(barcode);
        if (p == null){
            p = new Product();
            p.setBarcode(barcode);
            long id = productDao.insert(p);
            return loadProduct(id);
        }

        return p;
    }

}
