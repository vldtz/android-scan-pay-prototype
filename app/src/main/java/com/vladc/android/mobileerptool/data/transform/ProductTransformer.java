package com.vladc.android.mobileerptool.data.transform;

import com.vladc.android.mobileerptool.data.db.entities.Product;
import com.vladc.android.mobileerptool.shared.service.dto.ProductDto;

public class ProductTransformer extends Transformer<ProductDto, Product> {

    @Override
    public Product transform(ProductDto input) {
        if (input == null) {
            return null;
        }
        Product output = new Product();
        return doTransform(input, output);
    }

    @Override
    protected Product doTransform(ProductDto input, Product output) {
        output.setId(input.getId());
        output.setBarcode(input.getBarcode());
        output.setName(input.getName());
        output.setDescription(input.getDescription());
        output.setImageUrl(input.getImageUrl());
        output.setPrice(input.getPrice());
        return output;
    }

}
