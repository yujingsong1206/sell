package com.yjs.controller;

import com.yjs.dataobject.ProductCategory;
import com.yjs.form.CategoryForm;
import com.yjs.service.ProductCategoryService;
import com.yjs.utils.DateTimeUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.validation.Valid;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/seller/category")
public class SellerCategoryController {

    @Autowired
    private ProductCategoryService productCategoryService;

    @GetMapping("/list")
    public ModelAndView list(Map<String, Object> map){
        List<ProductCategory> categoryList = productCategoryService.findAll();
        map.put("categoryList", categoryList);
        return new ModelAndView("category/list", map);
    }

    @GetMapping("/index")
    public ModelAndView index(@RequestParam(value = "categoryId", required = false) Integer categoryId,
                              Map<String, Object> map){
        if(categoryId != null){
            ProductCategory productCategory = productCategoryService.findById(categoryId);
            map.put("productCategory", productCategory);
        }

        return new ModelAndView("category/index", map);
    }

    @PostMapping("/save")
    public ModelAndView save(@Valid CategoryForm form,
                             BindingResult bindingResult,
                             Map<String, Object> map){
        if(bindingResult.hasErrors()){
            map.put("msg", bindingResult.getFieldError().getDefaultMessage());
            map.put("url","/sell/seller/category/index");
            return new ModelAndView("common/error", map);
        }
        ProductCategory productCategory = new ProductCategory();
        BeanUtils.copyProperties(form, productCategory);
        if(form.getCategoryId() != null){
            productCategory.setCreateTime(DateTimeUtil.strToDate(form.getCreateTimeStr()));
        }
        productCategoryService.save(productCategory);
        map.put("url","/sell/seller/category/list");
        return new ModelAndView("common/success", map);
    }

}
