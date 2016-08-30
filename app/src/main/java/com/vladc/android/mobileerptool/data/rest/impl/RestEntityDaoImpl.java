package com.vladc.android.mobileerptool.data.rest.impl;

import com.vladc.android.mobileerptool.MobileERPApplication;
import com.vladc.android.mobileerptool.data.EntityDao;
import com.vladc.android.mobileerptool.shared.service.BaseDto;

import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.List;

public abstract class RestEntityDaoImpl<T extends BaseDto<PK>, PK extends Serializable> implements EntityDao<T, PK> {

    protected RestTemplate template;
    protected Class<T> entityClass;
    
    @SuppressWarnings("unchecked")
    public RestEntityDaoImpl() {
        super();
        this.entityClass = ((Class<T>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[0]);        
        this.template = new RestTemplate();
        this.template.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
    }
    
    protected final String getBaseUrl() {
        return MobileERPApplication.getRestUrl() + getControllerPath();
    }
    
    protected abstract String getControllerPath();
    
    protected String getEntityUrl() {
        return getBaseUrl() + "/{id}";
    }
    
    @Override
    public T get(PK id) {
        return template.getForObject(getEntityUrl(), this.entityClass, id);
    }

    @Override
    public void update(T entity) {
        template.put(getEntityUrl(), entity, entity.getId());        
    }
    
    @Override
    public T create(T entity) {
        return template.postForObject(getBaseUrl(), entity, entityClass);        
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public List<T> getAll() {
        Class clazz = Array.newInstance(entityClass, 0).getClass();
        T[] arr = (T[])template.getForObject(getBaseUrl(), clazz);
        return Arrays.asList(arr);
    }

    public void delete(Long id) {
        template.delete(getEntityUrl(), id);        
    }
    
    @Override
    public void delete(T entity) {
        delete((Long)entity.getId());        
    }

}
