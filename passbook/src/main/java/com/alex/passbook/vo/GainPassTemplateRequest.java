package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>用户领取优惠券的请求对象</h1>
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class GainPassTemplateRequest {

    /** 用户id */
    private Long userId;

    /** PassTemplate对象 */
    private PassTemplateVo passTemplateVo;
}
