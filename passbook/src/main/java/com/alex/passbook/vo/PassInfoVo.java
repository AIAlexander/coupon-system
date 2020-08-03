package com.alex.passbook.vo;

import com.alex.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>用户已经领取的优惠券的信息</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassInfoVo {
    /** 优惠券的信息 */
    private PassVo passVo;

    /** 优惠券模版 */
    private PassTemplateVo passTemplateVo;

    /** 优惠券对应的商户 */
    private Merchants merchants;
}
