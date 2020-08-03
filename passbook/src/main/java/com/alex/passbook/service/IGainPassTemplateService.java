package com.alex.passbook.service;

import com.alex.passbook.vo.GainPassTemplateRequest;
import com.alex.passbook.vo.Response;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>用户领取的优惠券功能实现</h1>
 * TODO 需要重构
 */
public interface IGainPassTemplateService {

    /**
     * <h2>用户领取优惠券</h2>
     * @param gainPassTemplateRequest {@link GainPassTemplateRequest}
     * @return {@link Response}
     * @throws Exception
     */
    Response gainPassTemplatee(GainPassTemplateRequest gainPassTemplateRequest) throws Exception;
}
