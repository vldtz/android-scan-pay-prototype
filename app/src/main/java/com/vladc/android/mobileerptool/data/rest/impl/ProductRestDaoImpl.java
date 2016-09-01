package com.vladc.android.mobileerptool.data.rest.impl;

import com.vladc.android.mobileerptool.shared.service.dto.ProductDto;

import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


public class ProductRestDaoImpl extends RestEntityDaoImpl<ProductDto, Long> {

    @Override
    protected String getControllerPath() {
        return "/products";
    }
    
    public ProductDto getByBarcode(String barcode) {
        URI targetUrl = UriComponentsBuilder.fromUriString(getBaseUrl())
                .pathSegment(barcode)
                .build()
                .toUri();
        ProductDto result = template.getForObject(targetUrl, ProductDto.class);
        return result;
    }

}
