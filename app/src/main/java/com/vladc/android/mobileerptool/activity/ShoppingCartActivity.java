package com.vladc.android.mobileerptool.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.data.db.entities.Product;
import com.vladc.android.mobileerptool.data.db.entities.ProductToCart;
import com.vladc.android.mobileerptool.data.db.entities.ShoppingCart;
import com.vladc.android.mobileerptool.fragment.ProductDetailFragment;
import com.vladc.android.mobileerptool.shared.service.CartServiceImpl;
import com.vladc.android.mobileerptool.shared.service.ProductServiceImpl;
import com.vladc.android.mobileerptool.util.NetworkUtil;

import java.util.List;

/**
 * An activity representing a list of Products. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProductDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ShoppingCartActivity extends AppCompatActivity {

    private static final String TAG = ShoppingCartActivity.class.getSimpleName();
    private SimpleCursorAdapter adapter;

    private ShoppingCart mCart;
    private CartServiceImpl mCartService;

    private ProgressDialog mLoader = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initBarcodeScan();
            }
        });

        mCartService = new CartServiceImpl(this);
        reloadShoppingCart();

        FloatingActionButton checkout = (FloatingActionButton) findViewById(R.id.fab_checkout);
        checkout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AsyncTask<ShoppingCart, Void, Boolean>() {
                    @Override
                    protected void onPreExecute() {
                        displayDialog();
                    }

                    @Override
                    protected Boolean doInBackground(ShoppingCart... shoppingCarts) {
                        return mCartService.checkoutCurrentCart();
                    }

                    @Override
                    protected void onPostExecute(Boolean result) {
                        closeDialog();

                        if (!result) {
                            AlertDialog.Builder builder = new AlertDialog.Builder(ShoppingCartActivity.this);
                            builder.setTitle("A aparut o eroare.");
                            if (NetworkUtil.ifDown(MobileERPApplication.getContext())){
                                builder.setMessage("Verificati conexiunea la internet si incercati din nou");
                            } else if (mCart.getProducts().size() == 0){
                                builder.setMessage("Nu aveti niciun produs in cos");
                            } else {
                                builder.setMessage("Incercati din nou.");
                            }

                            builder.setNeutralButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            });
                            builder.create().show();
                            return;
                        }

                        gotoLastCheckoutActivity();
                    }
                }.execute();
            }
        });

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void gotoLastCheckoutActivity() {
        Intent intent = new Intent(this, LastCheckoutActivity.class);
        startActivity(intent);
    }

    private void initBarcodeScan() {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(true);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.PRODUCT_CODE_TYPES);
        integrator.setPrompt("Scanati codul de bare");
//            integrator.setCameraId(0);  // Use a specific camera of the device
//            integrator.setBarcodeImageEnabled(true);
        integrator.initiateScan();
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(mCart.getProducts()));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<ProductToCart> mValues;

        public SimpleItemRecyclerViewAdapter(List<ProductToCart> items) {
            mValues = items;
        }

        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.product_list_content, parent, false);
            return new ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final ViewHolder holder, int position) {
            holder.mItem = mValues.get(position);
//            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mNameView.setText(holder.mItem.getProduct().getName());
            holder.mPriceView.setText(holder.mItem.getProduct().getPrice() + "");
            holder.mQuantityView.setText(String.valueOf(holder.mItem.getQuantity()));
            Float totalPrice = holder.mItem.getQuantity() * holder.mItem.getProduct().getPrice();
            totalPrice = Math.round(totalPrice*100)/100f;
            holder.mTotalProdPrice.setText(String.valueOf(totalPrice));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showQuantityDialog(holder.mItem);
                }
            });

            holder.mView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, ProductDetailActivity.class);
                    intent.putExtra(ProductDetailActivity.SHOW_REMOVE_BUTTON, true);
                    intent.putExtra(ProductDetailFragment.PRODUCT_OBJ_KEY, holder.mItem.getProduct());
                    context.startActivity(intent);
                    return true;
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mNameView;
            public final TextView mPriceView;
            public final TextView mQuantityView;
            public final TextView mTotalProdPrice;
            public ProductToCart mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mNameView = (TextView) view.findViewById(R.id.name);
                mPriceView = (TextView) view.findViewById(R.id.unit_price);
                mQuantityView = (TextView) view.findViewById(R.id.quantity);
                mTotalProdPrice = (TextView) view.findViewById(R.id.total_prod_price);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }

    // Get the results:
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        final IntentResult scanResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (requestCode == MainActivity.REQUEST_BARCODE_CAPTURE && scanResult != null) {
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

    private void showQuantityDialog(final ProductToCart productToCart){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Cantitate");

        // Set up the input
        final NumberPicker input = new NumberPicker(this);
        // Specify the type of input expected; this, for example, sets the input as a password, and will mask the text
        input.setMinValue(1);
        input.setMaxValue(1000);
        input.setWrapSelectorWheel(false);
        input.setValue(productToCart.getQuantity().intValue());
        builder.setView(input);

        // Set up the buttons
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener(){
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mCartService.updateProductQuantityInCurrentCart(productToCart.getProductId(), (long) input.getValue());
                reloadShoppingCart();
                dialog.dismiss();
            }
        });
        builder.setNeutralButton("Sterge din cos", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                mCartService.removeProductFromCurrentCart(productToCart.getProductId());
                reloadShoppingCart();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Anuleaza", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }

    /**
     * Useful function to init the mLoader
     */
    private void displayDialog() {
        if (mLoader == null) {
            final String title = getResources().getString(R.string.generic_load_title);
            final String message = getResources().getString(R.string.generic_load_message);
            mLoader = ProgressDialog.show(ShoppingCartActivity.this, title, message, true, false);
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

    private void reloadShoppingCart(){
        mCart = mCartService.getCurrentShoppingCart();
        View reciclerView = findViewById(R.id.product_list);
        assert reciclerView != null;
        setupRecyclerView((RecyclerView) reciclerView);

        TextView listTotalView = (TextView) findViewById(R.id.shopping_list_total_price);
        listTotalView.setText(mCart.getCartTotal() + " RON");

        FloatingActionButton gotoCheckoutBtn = (FloatingActionButton) findViewById(R.id.fab_checkout);
        gotoCheckoutBtn.setVisibility(mCart.getProducts().size() > 0 ? View.VISIBLE : View.GONE);
    }
}
