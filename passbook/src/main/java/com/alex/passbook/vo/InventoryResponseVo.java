package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>库存请求响应(商户投放的未领取的优惠券)</h1>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InventoryResponseVo {

    /** 用户id */
    private Long userId;

    /** 未领取且未过期的优惠券 */
    private List<PassTemplateInfoVo> passTemplateInfoVos;
}
