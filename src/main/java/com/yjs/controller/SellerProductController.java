package com.yjs.controller;

import com.yjs.dataobject.ProductCategory;
import com.yjs.dataobject.ProductInfo;
import com.yjs.enums.ProductStatusEnum;
import com.yjs.enums.ResultEnum;
import com.yjs.exception.SellException;
import com.yjs.form.ProductForm;
import com.yjs.service.ProductCategoryService;
import com.yjs.service.ProductInfoService;
import com.yjs.utils.DateTimeUtil;
import com.yjs.utils.KeyUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/product")
public class SellerProductController {

    @Autowired
    private ProductInfoService productInfoService;
    @Autowired
    private ProductCategoryService productCategoryService;

    /**
     * 商品列表
     */
    @GetMapping("/list")
    public ModelAndView list(@RequestParam(value = "page", defaultValue = "1") Integer page,
                             @RequestParam(value = "size", defaultValue = "10") Integer size,
                             Map<String, Object> map){
        Page<ProductInfo> productInfoPage = productInfoService.findAll(PageRequest.of(page - 1, size, new Sort(Sort.Direction.DESC, "createTime")));
        map.put("productInfoPage", productInfoPage);
        map.put("currentPage", page);
        map.put("size", size);
        return new ModelAndView("product/list", map);
    }

    /**
     * 商品上架
     */
    @RequestMapping("/on_sale")
    public ModelAndView onSale(@RequestParam("productId") String productId,
                               Map<String, Object> map){
        try {
            productInfoService.onSale(productId);
        } catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }
//        map.put("msg", ResultEnum.ORDER_CANCEL_ERROR.getMsg());
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    /**
     * 商品下架
     */
    @RequestMapping("/off_sale")
    public ModelAndView offSale(@RequestParam("productId") String productId,
                               Map<String, Object> map){
        try {
            productInfoService.offSale(productId);
        } catch (SellException e){
            map.put("msg", e.getMessage());
            map.put("url","/sell/seller/product/list");
            return new ModelAndView("common/error", map);
        }
//        map.put("msg", ResultEnum.ORDER_CANCEL_ERROR.getMsg());
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "productId", required = false) String productId,
                      Map<String, Object> map){
        if(!StringUtils.isEmpty(productId)){
            ProductInfo productInfo = productInfoService.findById(productId);
            map.put("productInfo",productInfo);
        }

        List<ProductCategory> productCategoryList = productCategoryService.findAll();
        map.put("productCategoryList", productCategoryList);

        return new ModelAndView("product/index", map);
    }

    @PostMapping("/save")
//    @CachePut(cacheNames = "products", key = "1")
    @CacheEvict(cacheNames = "products", key = "1")
    public ModelAndView save(@Valid ProductForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/product/index");
            return new ModelAndView("common/error", map);
        }

        ProductInfo productInfo = new ProductInfo();
        BeanUtils.copyProperties(form, productInfo);
        if(StringUtils.isEmpty(form.getProductId())){
            productInfo.setProductStatus(ProductStatusEnum.UP.getCode());
            productInfo.setProductId(KeyUtil.genUniqueKey());
        }else{
            productInfo.setCreateTime(DateTimeUtil.strToDate(form.getCreateTimeStr()));
        }
        productInfoService.save(productInfo);
        map.put("url","/sell/seller/product/list");
        return new ModelAndView("common/success", map);
    }

}
