package com.vladc.android.mobileerptool.shared.service;

import android.content.Context;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.data.db.entities.Cart;
import com.vladc.android.mobileerptool.data.db.entities.ProductToCart;
import com.vladc.android.mobileerptool.data.db.entities.ShoppingCart;
import com.vladc.android.mobileerptool.data.db.impl.CartDbDaoImpl;
import com.vladc.android.mobileerptool.data.db.impl.ProductDbDaoImpl;
import com.vladc.android.mobileerptool.data.db.impl.ProductToCartDbDaoImpl;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vlad.
 */
public class CartServiceImpl {

    private final static String TAG = CartServiceImpl.class.getSimpleName();

    private Context context;
    private ProductDbDaoImpl productDbDao;
    private CartDbDaoImpl cartDbDao;
    private ProductToCartDbDaoImpl productToCartDbDao;

    public CartServiceImpl() {
        this(MobileERPApplication.getContext());
    }

    public CartServiceImpl(Context context) {
        super();
        this.context = context;
        productDbDao = new ProductDbDaoImpl();
        productToCartDbDao = new ProductToCartDbDaoImpl();
        cartDbDao = new CartDbDaoImpl();
    }

    public ProductToCart addProductToCurrentCart(Long productId){
        Cart current = getCurrentOrCreate();

        ProductToCart productToCart = productToCartDbDao.getForProductAndCart(productId, current.getId());
        if (productToCart == null){
            productToCart = new ProductToCart();
            productToCart.setProductId(productId);
            productToCart.setCartId(current.getId());

            productToCart = productToCartDbDao.create(productToCart);
        }

        productToCart.setQuantity(productToCart.getQuantity() + 1 );
        productToCartDbDao.update(productToCart);

        return productToCart;
    }

    public void removeProductFromCurrentCart(Long productId){
        Cart current = getCurrentOrCreate();

        ProductToCart productToCart = productToCartDbDao.getForProductAndCart(productId, current.getId());
        if (productToCart == null){
           return;
        }

        productToCartDbDao.delete(productToCart);
    }

    public boolean updateProductQuantityInCurrentCart(Long productId, Long quantity){
        Cart current = getCurrentOrCreate();
        if (quantity < 1) {
            return false;
        }
        ProductToCart productToCart = productToCartDbDao.getForProductAndCart(productId, current.getId());
        if (productToCart == null){
            productToCart = addProductToCurrentCart(productId);
        }

        productToCart.setQuantity(quantity);
        productToCartDbDao.update(productToCart);
        return true;
    }

    private Cart getCurrentOrCreate(){
        Cart current = cartDbDao.getCurrent();
        if (current == null){
            current = cartDbDao.create(new Cart());
        }
        return current;
    }

    public ShoppingCart getCurrentShoppingCart(){
        Cart current = getCurrentOrCreate();
        ShoppingCart result = new ShoppingCart(current);

        loadCartProducts(result);
        return result;
    }

    private void loadCartProducts(ShoppingCart cart){
        List<ProductToCart> productsToCart = productToCartDbDao.getByCartId(cart.getId());
        for (ProductToCart pc : productsToCart){
            pc.setProduct(productDbDao.get(pc.getProductId()));
        }

        cart.setProducts(productsToCart);
    }

    public List<ShoppingCart> getCartHistory() {
        List<Cart> carts = cartDbDao.getAllClosed();
        List<ShoppingCart> result = new ArrayList<>(carts.size());

        for (Cart c : carts){
            ShoppingCart sc = new ShoppingCart(c);
            loadCartProducts(sc);
            result.add(sc);
        }
        return result;
    }
}
