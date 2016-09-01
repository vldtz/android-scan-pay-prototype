package com.vladc.android.mobileerptool.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.activity.ProductDetailActivity;
import com.vladc.android.mobileerptool.activity.ShoppingCartActivity;
import com.vladc.android.mobileerptool.data.db.entities.Product;


/**
 * A fragment representing a single Product detail screen.
 * This fragment is either contained in a {@link ShoppingCartActivity}
 * in two-pane mode (on tablets) or a {@link ProductDetailActivity}
 * on handsets.
 */
public class ProductDetailFragment extends Fragment {
    /**
     * The fragment argument representing the item ID that this fragment
     * represents.
     */
    public static final String PRODUCT_OBJ_KEY = "productObj";
    public static final String ARG_PRODUCT_ID = "item_id";

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * The dummy content this fragment is presenting.
     */
    private Product mItem;
    private ImageView mImageView;

    public static ProductDetailFragment newInstance(Product serializable) {
        ProductDetailFragment fragment = new ProductDetailFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable(PRODUCT_OBJ_KEY, serializable);
        fragment.setArguments(bundle);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(PRODUCT_OBJ_KEY)) {
            mItem = (Product) getArguments().getSerializable(PRODUCT_OBJ_KEY);

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
        final View rootView = inflater.inflate(R.layout.product_detail, container, false);

        if (mItem != null) {
//            ((TextView) rootView.findViewById(R.id.field_id)).setText(String.valueOf(mItem.getId()));
//            ((TextView) rootView.findViewById(R.id.field_barcode)).setText(mItem.getBarcode());
            ((TextView) rootView.findViewById(R.id.field_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.field_price)).setText(mItem.getPrice() + " RON");
            ((TextView) rootView.findViewById(R.id.field_description)).setText(mItem.getDescription());
            if (mItem.getIngredients() != null){
                rootView.findViewById(R.id.label_ingredients).setVisibility(View.VISIBLE);
                TextView ingredientsView = ((TextView) rootView.findViewById(R.id.field_ingredients));
                ingredientsView.setVisibility(View.VISIBLE);
                ingredientsView.setText(mItem.getIngredients());

            }
//            ((TextView) rootView.findViewById(R.id.field_quantity)).setText(String.valueOf(mItem.getQuantity()));


            mImageView = (ImageView) rootView.findViewById(R.id.image_large);
            if (mItem.getImageUrl() != null) {
                imageLoader.displayImage(mItem.getImageUrl(), mImageView);
            }
        }

        return rootView;
    }

}
