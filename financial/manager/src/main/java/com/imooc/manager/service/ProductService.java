package com.imooc.manager.service;

import com.imooc.entity.Product;
import com.imooc.enums.ProductStatus;
import com.imooc.manager.respositories.ProductRespository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.persistence.criteria.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 产品服务类
 * Created by Administrator on 2018/7/19 0019.
 */
@Service
public class ProductService {
    private static Logger log = LoggerFactory.getLogger(ProductService.class);
    @Autowired
    private ProductRespository respository;

    public Product addProduct(Product product){
        log.debug("创建产品，参数{}",product);
        //校验数据
        checkProduct(product);

        //设置默认值
        setDefault(product);
        Product result = respository.save(product);
        log.debug("创建产品，结果{}",product);
        return result;
    }

    /**
     * 创建时间，更新时间
     * 投资步长，锁定期，状态
     * @param product
     */
    private void setDefault(Product product) {
        if (product.getCreateAt() ==null){
            product.setCreateAt(new Date());
        }
        if (product.getUpdateAt() ==null){
            product.setUpdateAt(new Date());
        }
        if (product.getStepAmount() ==null){
            product.setStepAmount(BigDecimal.ZERO);
        }
        if (product.getLockTerm() ==null){
            product.setLockTerm(0);
        }
        if (product.getStatus() ==null){
            product.setStatus(ProductStatus.AUDITING.name());
        }
    }

    /**
     * 产品数据校验
     * 1，非空数据
     * 2，收益率要0-30以内
     * 3，投资步长需为整数
     * @param product
     */
    private void checkProduct(Product product) {
        Assert.notNull(product.getId(),"编号不能为空");
        //其他非空校验
        Assert.isTrue(BigDecimal.ZERO.compareTo(product.getRewardRate())< 0 && BigDecimal.valueOf(30).compareTo(product.getRewardRate()) >= 0, "收益率范围错误");
        Assert.isTrue(BigDecimal.valueOf(product.getStepAmount().longValue()).compareTo(product.getStepAmount()) ==0,"投资步长需为整数");
    }

    /**
     * 查询单个产品
     * @param id
     * @return
     */
    public Product findOne(String id){
        Assert.notNull(id,"产品参数有误");
        log.debug("单个产品：",id);
        Product product = respository.findOne(id);
        log.debug("单个产品输出:",id);
        return product;
    }

    /**
     * 分页查询产品
     * @param idList
     * @param minRewardRate
     * @param maxRewardRate
     * @param statusList
     * @param pageable
     * @return
     */
    public Page<Product> query(final List<String> idList,
                               final BigDecimal minRewardRate, final BigDecimal maxRewardRate,
                               final List<String> statusList,
                               final Pageable pageable){
        log.debug("查询产品，idList={},minRewardRate{},maxRewardRate{},statusList{}",idList,minRewardRate,maxRewardRate,statusList);
        Specification<Product> specification = new Specification<Product>() {
            @Override
            public javax.persistence.criteria.Predicate toPredicate(Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Expression<String> idCol = root.get("id");
                Expression<BigDecimal> rewardRateCol = root.get("rewardRate");
                Expression<String> statusCol = root.get("status");
                List<Predicate> predicates = new ArrayList<>();
                if (idList !=null &&idList.size()>0){
                    predicates.add(idCol.in(idList));
                }
                if (BigDecimal.ZERO.compareTo(minRewardRate)<0){
                    predicates.add(cb.ge(rewardRateCol,maxRewardRate));
                }
                if (BigDecimal.ZERO.compareTo(maxRewardRate)<0){
                    predicates.add(cb.ge(rewardRateCol,maxRewardRate));
                }
                if (statusList !=null &&statusList.size()>0){
                    predicates.add(statusCol.in(statusList));
                }
                query.where(predicates.toArray(new Predicate[0]));
                return null;
            }
        };
        Page<Product> products = respository.findAll(specification, pageable);
        log.debug("查询结果",products);

        return products;
    }
}
