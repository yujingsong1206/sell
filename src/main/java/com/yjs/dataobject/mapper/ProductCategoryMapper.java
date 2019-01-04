package com.yjs.dataobject.mapper;


import com.yjs.dataobject.ProductCategory;
import org.apache.ibatis.annotations.*;

import java.util.Map;

public interface ProductCategoryMapper {

    @Insert("insert into product_category(category_name, category_type) values (#{category_name, jdbcType=VARCHAR},#{category_type, jdbcType=INTEGER})")
    int insertByMap(Map<String, Object> map);

    @Insert("insert into product_category(category_name, category_type) values (#{categoryName},#{categoryType})")
    int insertByObject(ProductCategory productCategory);

    @Select("select * from product_category where category_type = #{categoryType}")
    @Results({
            @Result(column = "category_id", property = "categoryId"),
            @Result(column = "category_name", property = "categoryName"),
            @Result(column = "category_type", property = "categoryType"),
    })
    ProductCategory findByCategoryType(@Param("categoryType") Integer categoryType);//多个参数记得加@Param

    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByCategoryType(@Param("categoryName") String categoryName,
                             @Param("categoryType") Integer categoryType);

    @Update("update product_category set category_name = #{categoryName} where category_type = #{categoryType}")
    int updateByObject(ProductCategory productCategory);

}
