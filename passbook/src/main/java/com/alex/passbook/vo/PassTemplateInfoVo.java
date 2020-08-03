package com.alex.passbook.vo;

import com.alex.passbook.entity.Merchants;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>未领取的优惠券模版</h1>
 */
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplateInfoVo extends PassTemplateVo{
    /** 优惠券模版 */
    private PassTemplateVo passTemplateVo;

    /** 优惠券属于的商户 */
    private Merchants merchants;
}
