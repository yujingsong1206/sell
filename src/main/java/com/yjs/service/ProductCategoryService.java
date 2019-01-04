package com.yjs.service;

import com.yjs.dataobject.ProductCategory;

import java.util.List;


public interface ProductCategoryService {

    ProductCategory findById(Integer categoryId);

    List<ProductCategory> findAll();

    List<ProductCategory> findByCategoryTypeIn(List<Integer> categoryTypeList);

    ProductCategory save(ProductCategory productCategory);

}
