package com.yjs.dataobject.mapper;

import com.yjs.dataobject.ProductCategory;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sjyjs on 2019/1/3.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class ProductCategoryMapperTest {

    @Autowired
    private ProductCategoryMapper productCategoryMapper;

    @Test
    public void insertByMap() throws Exception {
        Map<String, Object> map = new HashMap<>();
        map.put("category_name","哈哈");
        map.put("category_type","100");
        productCategoryMapper.insertByMap(map);
    }

    @Test
    public void insertByObject(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("发动机上课了");
        productCategory.setCategoryType(500);
        productCategoryMapper.insertByObject(productCategory);
    }

    @Test
    public void findByCategoryType(){
        ProductCategory productCategory = productCategoryMapper.findByCategoryType(99);
        Assert.assertNotNull(productCategory);
    }

    @Test
    public void updateByCategoryType(){
        productCategoryMapper.updateByCategoryType("师妹", 99);
    }

    @Test
    public void updateByObject(){
        ProductCategory productCategory = new ProductCategory();
        productCategory.setCategoryName("师姐");
        productCategory.setCategoryType(99);
        productCategoryMapper.updateByObject(productCategory);
    }

}