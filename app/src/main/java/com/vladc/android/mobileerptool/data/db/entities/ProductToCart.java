package com.vladc.android.mobileerptool.data.db.entities;

/**
 * Created by Vlad.
 */
public class ProductToCart extends DataObject<Long> {

    private Product product;
    private Long productId;
    private Long cartId;
    private Long quantity = 0L;

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Long getCartId() {
        return cartId;
    }

    public void setCartId(Long cartId) {
        this.cartId = cartId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public void setQuantity(Long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }
}
