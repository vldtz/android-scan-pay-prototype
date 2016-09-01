package com.vladc.android.mobileerptool.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.data.db.entities.Product;
import com.vladc.android.mobileerptool.fragment.ProductDetailFragment;
import com.vladc.android.mobileerptool.shared.service.CartServiceImpl;

/**
 * An activity representing a single Product detail screen. This
 * activity is only used narrow width devices. On tablet-size devices,
 * item details are presented side-by-side with a list of items
 * in a {@link ShoppingCartActivity}.
 */
public class ProductDetailActivity extends AppCompatActivity {

    public static final String ARG_ITEM_BARCODE = "item_barcode";
    public static final String SHOW_REMOVE_BUTTON = "has_remove_button";

    Product mProduct;

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
        boolean showRemoveButton = false;
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            mProduct = (Product) getIntent().getSerializableExtra(ProductDetailFragment.PRODUCT_OBJ_KEY);
            ProductDetailFragment fragment = ProductDetailFragment.newInstance(mProduct);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.product_detail_container, fragment)
                    .commit();
            showRemoveButton = getIntent().getBooleanExtra(SHOW_REMOVE_BUTTON, false);
        }

        FloatingActionButton fabAddToCart = (FloatingActionButton) findViewById(R.id.fab_shopping_cart);
        fabAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartServiceImpl cartService = new CartServiceImpl(MobileERPApplication.getContext());
                Product product = (Product) getIntent().getSerializableExtra(ProductDetailFragment.PRODUCT_OBJ_KEY);
                cartService.addProductToCurrentCart(product.getId());

                Intent shoppingCartIntent = new Intent(getApplicationContext(), ShoppingCartActivity.class);

                startActivity(shoppingCartIntent);
            }
        });
        FloatingActionButton fabRemoveFromCart = (FloatingActionButton) findViewById(R.id.fab_shopping_cart_remove);
        if (showRemoveButton) {
            fabRemoveFromCart.setVisibility(View.VISIBLE);
        }
        fabRemoveFromCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CartServiceImpl cartService = new CartServiceImpl(MobileERPApplication.getContext());
                Product product = (Product) getIntent().getSerializableExtra(ProductDetailFragment.PRODUCT_OBJ_KEY);
                cartService.removeProductFromCurrentCart(product.getId());

                Intent shoppingCartIntent = new Intent(getApplicationContext(), ShoppingCartActivity.class);

                startActivity(shoppingCartIntent);
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
