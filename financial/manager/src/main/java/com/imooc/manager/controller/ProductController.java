package com.imooc.manager.controller;

import com.imooc.entity.Product;
import com.imooc.manager.service.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

/**
 * 产品
 * Created by Administrator on 2018/7/19 0019.
 */
@Controller
@RequestMapping("/products")
public class ProductController {
    private static Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Autowired
    private ProductService service;

    @RequestMapping(value = "",method = RequestMethod.POST)
    public Product addProduct(@RequestBody Product product){
        logger.info("创建产品，参数{}",product);
        Product result = service.addProduct(product);
        logger.info("创建产品，结果{}",product);
        return result;
    }
    @RequestMapping(value = "/{id}",method = RequestMethod.GET)
    public Product findOne(@PathVariable String id){
        logger.info("传入参数"+id);
        Product product = service.findOne(id);
        logger.info("输出参数"+id);
        return product;
    }
    @RequestMapping(value = "/{}",method = RequestMethod.GET)
    public Page<Product> query(String ids,
                               BigDecimal minRewardRate,
                               BigDecimal maxRewardRate,
                               String status,
                              @RequestParam(defaultValue = "0") int pageNum,
                              @RequestParam(defaultValue = "10") int pageSize){
        logger.info("查询产品");
        List<String> idList =null, statusList =null;
        if (!StringUtils.isEmpty(ids)){
            idList = Arrays.asList(ids.split(","));
        }
        if (!StringUtils.isEmpty(status)){
            statusList = Arrays.asList(status.split(","));
        }
        Pageable page = new PageRequest(pageNum,pageSize);
        Page<Product> query = service.query(idList, minRewardRate, maxRewardRate, statusList, page);
        logger.info("查询产品结果:",query);
        return query;
    }

}
