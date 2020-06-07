package com.alex.passbook.dao;

import com.alex.passbook.entity.Merchants;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author wsh
 * @date 2020-06-01
 * <h1>Merchants Dao接口</h1>
 */
public interface MerchantsDao extends JpaRepository<Merchants, Integer> {
    /**
     * 根据id获取商户对象
     * @param id 商户id
     * @return {@link Merchants}
     */
    Merchants findById(String id);

    /**
     * 根据商户名称获取商户对象
     * @param name 商户名称
     * @return {@link Merchants}
     */
    Merchants findByName(String name);

}
