package com.vladc.android.mobileerptool.data.rest.impl;

import android.util.Log;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.vladc.android.mobileerptool.shared.service.dto.CheckoutRequestDto;
import com.vladc.android.mobileerptool.shared.service.dto.CheckoutResponseDto;
import com.vladc.android.mobileerptool.shared.service.dto.ProductDto;

import org.springframework.http.ResponseEntity;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


public class CheckoutRestDaoImpl extends RestEntityDaoImpl<ProductDto, Long> {


    private static final String TAG = CheckoutRestDaoImpl.class.getSimpleName();

    @Override
    protected String getControllerPath() {
        return "/checkout";
    }
    
    public CheckoutResponseDto checkout(CheckoutRequestDto dto) {
        URI targetUrl = UriComponentsBuilder.fromUriString(getBaseUrl())
                .build()
                .toUri();
        ObjectMapper mapper = new ObjectMapper();
        String content = "";
        try {
            content = mapper.writeValueAsString(dto);
        } catch (JsonProcessingException e) {
            Log.e(TAG, e.getStackTrace().toString(),e);
        }
        ResponseEntity<CheckoutResponseDto> result = template.postForEntity(targetUrl, content, CheckoutResponseDto.class);
        return result.getBody();
    }

}
