package com.alex.passbook.dao;

import com.alex.passbook.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>商户dao层</h1>
 */
public interface MerchantsDao extends JpaRepository<Merchants, Integer> {

    /**
     * <h2>根据商户名称查询商户<h2/>
     * @param name
     * @return {@link Merchants}
     */
    Merchants findByName(String name);

    /**
     * <h2>根据商户ids获取商户list<h2/>
     * @param ids
     * @return {@link List<Merchants>}
     */
    List<Merchants> findByIds(List<Integer> ids);


}
