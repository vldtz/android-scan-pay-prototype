package com.vladc.android.mobileerptool.fragment;

import com.vladc.android.mobileerptool.dao.entity.Product;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Helper class for providing sample content for user interfaces created by
 * Android template wizards.
 * <p/>
 * TODO: Replace all uses of this class before publishing your app.
 */
public class ProductContent {

    /**
     * An array of sample (dummy) items.
     */
    public static final List<Product> ITEMS = new ArrayList<Product>();

    /**
     * A map of sample (dummy) items, by ID.
     */
    public static final Map<Long, Product> ITEM_MAP = new HashMap<>();

    private static final int COUNT = 25;

    private static void addItem(Product item) {
        ITEMS.add(item);
        ITEM_MAP.put(item.getId(), item);
    }


}
