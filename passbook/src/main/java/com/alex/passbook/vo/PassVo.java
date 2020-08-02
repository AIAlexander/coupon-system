package com.alex.passbook.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>用户已领取的优惠券</h1>
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PassVo {

    /** 用户id **/
    private Long userId;

    /** pass在Hbase的rowKey **/
    private String rowKey;

    /** passTemplate在Hbase的rowKey **/
    private String templateId;

    /** 优惠券token，如果是null，则填充-1 **/
    private String token;

    /** 领取日期 **/
    private Date assignedDate;

    /** 消费日期，不为空代表已经被消费了 **/
    private Date conDate;
}
