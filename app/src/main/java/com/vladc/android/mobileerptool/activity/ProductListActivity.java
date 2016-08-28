package com.vladc.android.mobileerptool.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.dao.entity.Product;
import com.vladc.android.mobileerptool.dao.impl.ProductDaoImpl;
import com.vladc.android.mobileerptool.fragment.ProductDetailFragment;

import java.util.List;

/**
 * An activity representing a list of Products. This activity
 * has different presentations for handset and tablet-size devices. On
 * handsets, the activity presents a list of items, which when touched,
 * lead to a {@link ProductDetailActivity} representing
 * item details. On tablets, the activity presents the list of items and
 * item details side-by-side using two vertical panes.
 */
public class ProductListActivity extends AppCompatActivity {

    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private SimpleCursorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final Intent addNewIntent = new Intent(this,AddEditProductActivity.class);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(addNewIntent);
            }
        });

        View recyclerView = findViewById(R.id.product_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.product_detail_container) != null) {
            // The detail container view will be present only in the
            // large-screen layouts (res/values-w900dp).
            // If this view is present, then the
            // activity should be in two-pane mode.
            mTwoPane = true;
        }
        getSupportActionBar().setDefaultDisplayHomeAsUpEnabled(true);
    }

    private void setupRecyclerView(@NonNull RecyclerView recyclerView) {
        ProductDaoImpl productDao = new ProductDaoImpl(this);
        productDao.open();
        List<Product> items = productDao.findAll();
        productDao.close();
        recyclerView.setAdapter(new SimpleItemRecyclerViewAdapter(items));
    }

    public class SimpleItemRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder> {

        private final List<Product> mValues;

        public SimpleItemRecyclerViewAdapter(List<Product> items) {
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
            holder.mIdView.setText(String.valueOf(mValues.get(position).getId()));
            holder.mNameView.setText(mValues.get(position).getName());
            holder.mBarcodeView.setText(mValues.get(position).getBarcode());
            holder.mQuantityView.setText(String.valueOf(mValues.get(position).getQuantity()));

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mTwoPane) {
                        Bundle arguments = new Bundle();
                        arguments.putLong(ProductDetailFragment.ARG_ITEM_ID, holder.mItem.getId());
                        ProductDetailFragment fragment = new ProductDetailFragment();
                        fragment.setArguments(arguments);
                        getSupportFragmentManager().beginTransaction()
                                .replace(R.id.product_detail_container, fragment)
                                .commit();
                    } else {
                        Context context = v.getContext();
                        Intent intent = new Intent(context, ProductDetailActivity.class);
                        intent.putExtra(ProductDetailFragment.ARG_ITEM_ID, holder.mItem.getId());

                        context.startActivity(intent);
                    }
                }
            });
        }

        @Override
        public int getItemCount() {
            return mValues.size();
        }

        public class ViewHolder extends RecyclerView.ViewHolder {
            public final View mView;
            public final TextView mIdView;
            public final TextView mNameView;
            public final TextView mBarcodeView;
            public final TextView mQuantityView;
            public Product mItem;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mIdView = (TextView) view.findViewById(R.id.id);
                mNameView = (TextView) view.findViewById(R.id.name);
                mBarcodeView = (TextView) view.findViewById(R.id.barcode);
                mQuantityView = (TextView) view.findViewById(R.id.quantity);
            }

            @Override
            public String toString() {
                return super.toString() + " '" + mNameView.getText() + "'";
            }
        }
    }
}
