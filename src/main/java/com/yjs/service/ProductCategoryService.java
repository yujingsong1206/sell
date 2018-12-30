package com.yjs.service;

import com.yjs.dataobject.ProductCategory;

import java.util.List;

/**
 * Created by sjyjs on 2018/12/25.
 */
public interface ProductCategoryService {

    ProductCategory findById(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

}
