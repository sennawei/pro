package com.imooc.manager.respositories;

import com.imooc.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * 产品管理
 * Created by Administrator on 2018/7/19 0019.
 */
public interface ProductRespository extends JpaRepository<Product,String>,JpaSpecificationExecutor<Product> {

}
