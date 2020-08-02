package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>投放的优惠券VO</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassTemplateVo {

    /** 所属商户id **/
    private Integer id;

    /** 优惠券标题 **/
    private String title;

    /** 优惠券摘要 **/
    private String summary;

    /** 优惠券详细详细 **/
    private String desc;

    /** 优惠券最大投放数量 **/
    private Long limit;

    /** 优惠券是否有token，用于核销 **/
    private Boolean hasToken;

    /** 优惠券背景颜色 **/
    private Integer background;

    /** 优惠券的开始时间 **/
    private Date start;

    /** 优惠券的结束时间 **/
    private Date end;
}
