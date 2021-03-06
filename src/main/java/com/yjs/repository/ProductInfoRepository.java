package com.yjs.repository;

import com.yjs.dataobject.ProductInfo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ProductInfoRepository extends JpaRepository<ProductInfo, String> {

    List<ProductInfo> findByProductStatus(Integer productStatus);

    ProductInfo findByProductStatusAndProductId(Integer productStatus, String productId);

}
