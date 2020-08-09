package com.alex.passbook.service;

import com.alex.passbook.vo.Response;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>库存服务（未领取的优惠券）</h1>
 * TODO 需要重构
 */
public interface IInventorySevice {

    /**
     * <h2>获取未领取的优惠券</h2>
     * @param userId 用户id
     * @return {@link Response}
     * @throws Exception
     */
    Response getInventoryInfo(Long userId) throws Exception;
}
