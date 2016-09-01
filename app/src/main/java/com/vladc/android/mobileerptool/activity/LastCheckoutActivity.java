package com.vladc.android.mobileerptool.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.data.db.entities.ShoppingCart;
import com.vladc.android.mobileerptool.shared.service.CartServiceImpl;

public class LastCheckoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_last_checkout);

        CartServiceImpl cartService = new CartServiceImpl(MobileERPApplication.getContext());
        ShoppingCart cart = cartService.getLastCheckout();

        if (cart == null){
            findViewById(R.id.lastCheckoutLayout).setVisibility(View.GONE);
            findViewById(R.id.noLastCheckoutText).setVisibility(View.VISIBLE);
        } else {
            findViewById(R.id.lastCheckoutLayout).setVisibility(View.VISIBLE);
            findViewById(R.id.noLastCheckoutText).setVisibility(View.GONE);

            ((TextView) findViewById(R.id.last_checkout_external_id)).setText(cart.getExternalId());
            ((TextView) findViewById(R.id.shopping_list_total_price)).setText(cart.getCartTotal() + " RON");

        }

        setupActionBar();
    }

    /**
     * Set up the {@link android.app.ActionBar}, if the API is available.
     */
    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
