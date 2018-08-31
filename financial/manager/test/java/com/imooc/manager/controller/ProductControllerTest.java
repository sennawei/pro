package com.imooc.manager.controller;

import com.imooc.entity.Product;
import com.imooc.enums.ProductStatus;
import com.imooc.manager.respositories.ProductRespository;
import com.imooc.util.RestUtil;
import org.junit.BeforeClass;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/7/26 0026.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)//每次启动的时候随机启动一个端口
@FixMethodOrder(MethodSorters.NAME_ASCENDING)//执行顺序
public class ProductControllerTest {

    private static RestTemplate rest= new RestTemplate();

    @Value("http://localhost:${local.server.port}/manager")
    private String baseUrl;

    //正常产品数据
    private static List<Product> normals= new ArrayList<>();
    //异常数据
    private static List<Product> exceptions = new ArrayList<>();

    @Autowired
    private ProductRespository respository;

    @BeforeClass
    public static void  init(){
        Product p1 = new Product("T001","灵活宝", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(10),BigDecimal.valueOf(1),BigDecimal.valueOf(3.42));
        Product p2 = new Product("T002","灵活宝", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(10),BigDecimal.valueOf(1),BigDecimal.valueOf(4));
        Product p3 = new Product("T003","灵活宝", ProductStatus.AUDITING.name(),
                BigDecimal.valueOf(10),BigDecimal.valueOf(1),BigDecimal.valueOf(5.42));
        normals.add(p1);
        normals.add(p2);
        normals.add(p3);
    }

    @Test
    public void create(){
        for (Product p : normals){
            Map<String,String> result = RestUtil.postJSON(rest, baseUrl + "/products", p, HashMap.class);
            Assert.isTrue(result.get("message").equals(p.getName()),"插入成功");
        }
    }
    public void zfindOne(){
        for (Product p : normals){
            Product result = rest.getForObject(baseUrl + "/products/" + p.getId(), Product.class);
            Assert.isTrue(result.getId().equals(p.getId()),"查询失败");
        }
    }




















}
