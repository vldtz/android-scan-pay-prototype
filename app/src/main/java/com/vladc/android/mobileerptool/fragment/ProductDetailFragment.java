package com.vladc.android.mobileerptool.fragment;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.vladc.android.mobileerptool.R;
import com.vladc.android.mobileerptool.activity.ProductDetailActivity;
import com.vladc.android.mobileerptool.activity.SettingsActivity;
import com.vladc.android.mobileerptool.activity.ShoppingCartActivity;
import com.vladc.android.mobileerptool.data.db.entities.Product;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;


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

    protected static Map<String,View> mAvailableFields = new HashMap<>();

    protected ImageLoader imageLoader = ImageLoader.getInstance();

    /**
     * The dummy content this fragment is presenting.
     */
    private Product mItem;

    private ImageView mImageView;
    private TextView mDescriptionView;
    private TextView mIngredientsView;
    private TextView mStoringConditionsView;
    private TextView mPromotionsView;
    private TextView mProducerView;
    private TextView mProducerUrlView;


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
            ((TextView) rootView.findViewById(R.id.field_name)).setText(mItem.getName());
            ((TextView) rootView.findViewById(R.id.field_price)).setText(mItem.getPrice() + " RON");

            mImageView = (ImageView) rootView.findViewById(R.id.image_large);
            mDescriptionView = (TextView) rootView.findViewById(R.id.field_description);
            mIngredientsView = (TextView) rootView.findViewById(R.id.field_ingredients);
            mStoringConditionsView = (TextView) rootView.findViewById(R.id.field_storing_conditions);
            mPromotionsView = (TextView) rootView.findViewById(R.id.field_promotions);
            mProducerView = (TextView) rootView.findViewById(R.id.field_producer);
            mProducerUrlView = (TextView) rootView.findViewById(R.id.field_producer_url);

            mDescriptionView.setText(mItem.getDescription());
            mIngredientsView.setText(mItem.getIngredients());
            mProducerView.setText(mItem.getProducer());
            mProducerUrlView.setText(mItem.getProducerUrl());
            mPromotionsView.setText(mItem.getPromotions());
            mStoringConditionsView.setText(mItem.getStoringConditions());

            mAvailableFields.put("description", mDescriptionView);
            mAvailableFields.put("ingredients", mIngredientsView);
            mAvailableFields.put("storing_conditions", mStoringConditionsView);
            mAvailableFields.put("promotions", mPromotionsView);
            mAvailableFields.put("producer", mProducerView);
            mAvailableFields.put("producer_url", mProducerUrlView);
            mAvailableFields.put("image", mImageView);

            filterDisplayedInfo(rootView, false);
            displayWarnings(rootView);

            if (mItem.getImageUrl() != null) {
                imageLoader.displayImage(mItem.getImageUrl(), mImageView);
            }

            CheckedTextView showMoreBtn = (CheckedTextView) rootView.findViewById(R.id.btn_toggle_show_more);
            showMoreBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckedTextView view = (CheckedTextView) v;
                    view.toggle();
                    filterDisplayedInfo(rootView, view.isChecked() );
                    view.setText(view.isChecked() ? R.string.text_show_less : R.string.text_show_more);
                }
            });
        }

        return rootView;
    }

    private void displayWarnings(View rootView) {
        if (StringUtils.isEmpty(mItem.getIngredients())) {
            return;
        }

        List<String> ingredientsList = Arrays.asList(StringUtils.split(mItem.getIngredients(),","));

        View badIngredientsContainer = rootView.findViewById(R.id.layout_bad_list_ingredients_warning);
        TextView badIngredientsText = (TextView) rootView.findViewById(R.id.field_bad_list_ingredients);

        View goodIngredientsContainer = rootView.findViewById(R.id.layout_good_list_ingredients_warning);
        TextView goodIngredientsText = (TextView) rootView.findViewById(R.id.field_good_list_ingredients);

        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getContext());

        Set<String> badList = mSharedPreference.getStringSet(SettingsActivity.PREF_INGREDIENTS_LIST_BAD_KEY, Collections.<String>emptySet());
        Set<String> goodList = mSharedPreference.getStringSet(SettingsActivity.PREF_INGREDIENTS_LIST_GOOD_KEY, Collections.<String>emptySet());

        List<String> matchingBad = new ArrayList<>();
        List<String> matchingGood = new ArrayList<>();

        for (String i : ingredientsList) {
            for (String bi : badList) {
                if (StringUtils.containsIgnoreCase(i,bi)){
                    matchingBad.add(i);
                }
            }

            for (String gi : goodList) {
                if (StringUtils.containsIgnoreCase(i,gi)){
                    matchingGood.add(i);
                }
            }
        }

        if (matchingBad.size() > 0){
            badIngredientsContainer.setVisibility(View.VISIBLE);
            badIngredientsText.setText(StringUtils.join(matchingBad, ", "));
        } else {
            badIngredientsContainer.setVisibility(View.GONE);
        }

        if (matchingGood.size() > 0){
            goodIngredientsContainer.setVisibility(View.VISIBLE);
            goodIngredientsText.setText(StringUtils.join(matchingGood, ", "));
        } else {
            goodIngredientsContainer.setVisibility(View.GONE);
        }


    }

    private void filterDisplayedInfo(View rootView, boolean showMore) {
        final SharedPreferences mSharedPreference= PreferenceManager.getDefaultSharedPreferences(getContext());
        boolean displayEmptyTextFields = mSharedPreference.getBoolean(SettingsActivity.PREF_INFO_SHOW_EMPTY_KEY, false);
        List<String> defaultFields = Arrays.asList(getResources().getStringArray(R.array.pref_info_categories_list_values));
        Set<String> defaultSet = new HashSet<>(defaultFields.size());
        defaultSet.addAll(defaultFields);
        Set<String> displayedFields = mSharedPreference.getStringSet(SettingsActivity.PREF_INFO_CATEGORIES_KEY, defaultSet);

        int hiddenFields = 0;

        for (Map.Entry<String, View> entry : mAvailableFields.entrySet()){
            boolean visible = showMore || displayedFields.contains(entry.getKey());
            int visibility = visible ? View.VISIBLE : View.GONE;
            if (!visible) {
                hiddenFields++;
            }

            if (entry.getValue() instanceof TextView){
                TextView view = (TextView) entry.getValue();
                visible = visible && (displayEmptyTextFields || StringUtils.isNotEmpty(view.getText()));
                visibility = visible ? View.VISIBLE : View.GONE;
            }

            entry.getValue().setVisibility(visibility);
            if (entry.getValue().getLabelFor() != View.NO_ID) {
                rootView.findViewById(entry.getValue().getLabelFor()).setVisibility(visibility);
            }
        }

        rootView.findViewById(R.id.btn_toggle_show_more).setVisibility(hiddenFields > 0 || showMore ? View.VISIBLE : View.GONE);
    }

}
