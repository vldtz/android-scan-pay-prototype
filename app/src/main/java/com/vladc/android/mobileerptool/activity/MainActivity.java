package com.vladc.android.mobileerptool.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.data.db.entities.Product;
import com.vladc.android.mobileerptool.fragment.ProductDetailFragment;
import com.vladc.android.mobileerptool.shared.service.ProductServiceImpl;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private static final String TAG = MainActivity.class.getSimpleName();

    static final int REQUEST_BARCODE_CAPTURE = IntentIntegrator.REQUEST_CODE;
    /**
     * mLoader to display while data is fetched from server/db
     */
    private ProgressDialog mLoader = null;

    private ImageView mImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Create global configuration and initialize ImageLoader with this config
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(this)
                .build();
        ImageLoader.getInstance().init(config);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mImageView = (ImageView) findViewById(R.id.scannedImageView);
        mImageView.setVisibility(View.GONE);
        ImageButton barcodeMainBtn = (ImageButton) findViewById(R.id.barcodeScanBtn);
        barcodeMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBarcodeScan();
            }
        });

        ImageButton gotoListActivityBtn = (ImageButton) findViewById(R.id.gotoListBtn);
        gotoListActivityBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchListActivity();
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.GONE);
        /*fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_scan_barcode) {
            initBarcodeScan();
        } else if (id == R.id.nav_product_list) {
            launchListActivity();
        }/* TODO else if (id == R.id.nav_slideshow) {

        }*/

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void launchListActivity() {
        Intent intent = new Intent(this, ProductListActivity.class);
        startActivity(intent);
    }

    private void initBarcodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
//            integrator.setDesiredBarcodeFormats(IntentIntegrator.ONE_D_CODE_TYPES);
        integrator.setPrompt("Scanati codul de bare");
//            integrator.setCameraId(0);  // Use a specific camera of the device
        integrator.setBeepEnabled(false);
//            integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_BARCODE_CAPTURE && scanResult != null) {
            if (scanResult.getContents() == null) {
                Toast.makeText(this, "Cancelled", Toast.LENGTH_LONG).show();
            } else {
                /*
                * Shows a loader, fetch product hide loader when done and goto product details
                */
                new AsyncTask<String, Void, Product>() {
                    @Override
                    protected void onPreExecute() {
                        displayDialog(); /* watch out, this is on main thread! */
                    }

                    @Override
                    protected Product doInBackground(String... params) {
                        final Context context = MobileERPApplication.getContext();
                        ProductServiceImpl productService = new ProductServiceImpl(context);
                        return productService.getProductCachedByBarcode(params[0]);
                    }

                    @Override
                    protected void onPostExecute(Product result) {
                        closeDialog();
                        final Context context = MobileERPApplication.getContext();
                        Intent productDetails = new Intent(context, ProductDetailActivity.class);
                        productDetails.putExtra(ProductDetailFragment.PRODUCT_OBJ_KEY, result);
                        Toast.makeText(context, "Scanned: " + scanResult.getContents(), Toast.LENGTH_LONG).show();

                        startActivity(productDetails);
                    }
                }.execute(scanResult.getContents());
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    /**
     * Useful function to init the mLoader
     */
    private void displayDialog() {
        if (mLoader == null) {
            final String title = getResources().getString(R.string.generic_load_title);
            final String message = getResources().getString(R.string.generic_load_message);
            mLoader = ProgressDialog.show(MainActivity.this, title, message, true, false);
        } else {
            if (!mLoader.isShowing()) {
                mLoader.show();
            }
        }
        Log.d(TAG, "Loader opened, have fun");
    }

    /**
     * Useful function to cancel the mLoader
     */
    private void closeDialog() {
        if (mLoader != null) {
            mLoader.dismiss();
        }
        Log.d(TAG, "Loader closed, have fun");
    }

    @Override
    protected void onStop() {
        closeDialog();
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        closeDialog();
        super.onDestroy();
    }
}
