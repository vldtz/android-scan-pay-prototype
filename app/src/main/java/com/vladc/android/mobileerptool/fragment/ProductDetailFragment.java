package com.vladc.android.mobileerptool.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.activity.ProductDetailActivity;
import com.vladc.android.mobileerptool.activity.ProductListActivity;
import com.vladc.android.mobileerptool.dao.entity.Product;
import com.vladc.android.mobileerptool.dao.impl.ProductDaoImpl;


/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a {@link ProductListActivity}
 * in two-pane mode (on tablets) or a {@link ProductDetailActivity}
 * on handsets.
 */
public class ProductDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String ARG_ITEM_ID = "item_id";
    public static final String ARG_ITEM_BARCODE = "item_barcode";

    /**
     * The dummy content this fragment is presenting.
     */
    private Product mItem;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            ProductDaoImpl productDao = new ProductDaoImpl(getContext());
            productDao.open();

            // Load the dummy content specified by the fragment
            // arguments. In a real-world scenario, use a Loader
            // to load content from a content provider.
            mItem = productDao.findById(getArguments().getLong(ARG_ITEM_ID));
            productDao.close();

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(mItem.getName());
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.product_detail, container, false);

        // Show the dummy content as text in a TextView.
        if (mItem != null) {
            ((TextView) rootView.findViewById(R.id.field_id)).setText(String.valueOf(mItem.getId()));
            ((TextView) rootView.findViewById(R.id.field_barcode)).setText(mItem.getBarcode());
            ((TextView) rootView.findViewById(R.id.field_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.field_quantity)).setText(String.valueOf(mItem.getQuantity()));
        }

        return rootView;
    }
}
