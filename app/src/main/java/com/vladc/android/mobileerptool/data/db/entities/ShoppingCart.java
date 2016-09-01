package com.vladc.android.mobileerptool.data.db.entities;

import java.util.Collections;
import java.util.List;

/**
 * Created by Vlad.
 */
public class ShoppingCart extends Cart{

    List<ProductToCart> products;
    Float cartTotal;

    public ShoppingCart(){
        products = Collections.emptyList();
        cartTotal = 0.0f;
    }

    public ShoppingCart(Cart cart){
        this();
        this.setId(cart.getId());
        this.setExternalId(cart.getExternalId());
        this.setDateClosed(cart.getDateClosed());
        this.setDateCreated(cart.getDateCreated());
    }

    public List<ProductToCart> getProducts() {
        return products;
    }

    public void setProducts(List<ProductToCart> products) {
        this.products = products;
        cartTotal = 0.0f;
        if (products == null){
            return;
        }
        for (ProductToCart pc : products){
            cartTotal += pc.getQuantity() * pc.getProduct().getPrice();
        }
    }

    public Float getCartTotal() {
        return cartTotal;
    }

    public void setCartTotal(Float cartTotal) {
        this.cartTotal = cartTotal;
    }
}
