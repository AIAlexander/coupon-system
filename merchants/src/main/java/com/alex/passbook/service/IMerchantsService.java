package com.alex.passbook.service;

import com.alex.passbook.vo.CreateMerchantsRequest;
import com.alex.passbook.vo.PassTemplate;
import com.alex.passbook.vo.Response;

/**
 * @author wsh
 * @date 2020-06-02
 * <h1>商户服务接口定义</h1>
 */

public interface IMerchantsService {

    /**
     * <h2>创建商户服务</h2>
     * @param createMerchantsRequest {@link CreateMerchantsRequest} 创建商户请求
     * @return {@link Response}
     */
    Response createMerchants(CreateMerchantsRequest createMerchantsRequest);

    /**
     * 根据id构造商户信息
     * @param id 商户id
     * @return {@link Response}
     */
    Response buildMerchantsInfoById(Integer id);

    /**
     * 投放优惠券
     * @param passTemplate {@link PassTemplate} 优惠券对象
     * @return {@link Response}
     */
    Response dropPassTemplate(PassTemplate passTemplate);
}
