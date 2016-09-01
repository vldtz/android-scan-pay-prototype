package com.vladc.android.mobileerptool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.data.db.entities.Product;
import com.vladc.android.mobileerptool.fragment.ProductDetailFragment;

/**
 * An activity representing a single Product detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ShoppingCartActivity}.
 */
public class ProductDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_BARCODE = "item_barcode";

    Product mProduct;
    Long mCurrentProductId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        // Show the Up button in the action bar.
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mProduct = (Product) getIntent().getSerializableExtra(ProductDetailFragment.PRODUCT_OBJ_KEY);
            ProductDetailFragment fragment = ProductDetailFragment.newInstance(mProduct);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();

            mCurrentProductId = getIntent().getLongExtra(ProductDetailFragment.ARG_PRODUCT_ID, 0L);
        }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent editItemIntent = new Intent(getApplicationContext(), AddEditProductActivity.class);
                editItemIntent.putExtra(ProductDetailFragment.ARG_PRODUCT_ID, getIntent().getLongExtra(ProductDetailFragment.ARG_PRODUCT_ID, 0L));

                startActivity(editItemIntent);
            }
        });*/
        fab.setVisibility(View.GONE);

        FloatingActionButton fabAddToCart = (FloatingActionButton) findViewById(R.id.fab_shopping_cart);
        fabAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent editItemIntent = new Intent(getApplicationContext(), AddEditProductActivity.class);
//                editItemIntent.putExtra(ProductDetailFragment.ARG_PRODUCT_ID, getIntent().getLongExtra(ProductDetailFragment.ARG_PRODUCT_ID, 0L));
//
//                startActivity(editItemIntent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            // This ID represents the Home or Up button. In the case of this
            // activity, the Up button is shown. For
            // more details, see the Navigation pattern on Android Design:
            //
            // http://developer.android.com/design/patterns/navigation.html#up-vs-back
            //
            navigateUpTo(new Intent(this, ShoppingCartActivity.class));
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
