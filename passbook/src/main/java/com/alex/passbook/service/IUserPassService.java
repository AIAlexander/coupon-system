package com.alex.passbook.service;

import com.alex.passbook.vo.PassVo;
import com.alex.passbook.vo.Response;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>获取用户个人优惠券信息</h1>
 * TODO 需要重构
 */
public interface IUserPassService {

    /**
     * <h2>获取用户未使用的优惠券（我的优惠券）</h2>
     * @param userId 用户id
     * @return {@link Response}
     */
    Response getUserPassInfo(Long userId) throws Exception;

    /**
     * <h2>获取用户已经消费的优惠券</h2>
     * @param userId 用户id
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserUsedPassInfo(Long userId) throws Exception;

    /**
     * <h2>获取用户所有的优惠券</h2>
     * @param userId 用户id
     * @return {@link Response}
     * @throws Exception
     */
    Response getUserAllPassInfo(Long userId) throws Exception;

    /**
     * <h2>用户使用优惠券</h2>
     * @param passVo {@link PassVo}
     * @return {@link Response}
     */
    Response usePass(PassVo passVo);
}
