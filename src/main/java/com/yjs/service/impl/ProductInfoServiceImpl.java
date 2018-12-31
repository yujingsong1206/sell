package com.yjs.service.impl;

import com.yjs.dataobject.ProductInfo;
import com.yjs.dto.CartDTO;
import com.yjs.enums.ProductStatusEnum;
import com.yjs.enums.ResultEnum;
import com.yjs.exception.SellException;
import com.yjs.repository.ProductInfoRepository;
import com.yjs.service.ProductInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Slf4j
public class ProductInfoServiceImpl implements ProductInfoService {

    @Autowired
    private ProductInfoRepository productInfoRepository;

    @Override
    public ProductInfo findById(String productId) {
        Optional optional = productInfoRepository.findById(productId);
        if(!optional.isPresent()){
            log.error("【查找商品】不存在，productId={}", productId);
            throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
        }
        return productInfoRepository.findById(productId).get();
    }

    @Override
    public List<ProductInfo> findUpAll() {
        return productInfoRepository.findByProductStatus(ProductStatusEnum.UP.getCode());
    }

    @Override
    public Page<ProductInfo> findAll(Pageable pageable) {
        return productInfoRepository.findAll(pageable);
    }

    @Override
    public ProductInfo save(ProductInfo productInfo) {
        return productInfoRepository.save(productInfo);
    }

    @Override
    @Transactional
    public void increaseStock(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList){
            Optional optional = productInfoRepository.findById(cartDTO.getProductId());
            if(!optional.isPresent()){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = (ProductInfo) optional.get();
            Integer result = productInfo.getProductStock() + cartDTO.getProductQuantity();
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }

    @Override
    @Transactional
    public void decreaseStcok(List<CartDTO> cartDTOList) {
        for (CartDTO cartDTO : cartDTOList){
            Optional optional = productInfoRepository.findById(cartDTO.getProductId());
            if(!optional.isPresent()){
                throw new SellException(ResultEnum.PRODUCT_NOT_EXIST);
            }
            ProductInfo productInfo = (ProductInfo) optional.get();
            Integer result = productInfo.getProductStock() - cartDTO.getProductQuantity();
            if(result < 0){
                throw new SellException(ResultEnum.PRODUCT_STOCK_ERROR);
            }
            productInfo.setProductStock(result);
            productInfoRepository.save(productInfo);
        }
    }
}
