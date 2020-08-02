package com.alex.passbook.vo;

import com.alex.passbook.constant.FeedbackType;
import com.google.common.base.Enums;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>用户评论vo</h1>
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class FeedbackVo {

    /** 用户id **/
    private Long userId;

    /** 评论类型 **/
    private String type;

    /** PassTemplate RowKey， 如果是app类型的评论，则没有 **/
    private String templateId;

    /** 评论内容 **/
    private String comment;

    public boolean validate(){
        FeedbackType feedbackType =
                Enums.getIfPresent(FeedbackType.class, this.type.toUpperCase())
                        .orNull();
        return !(null == feedbackType || null == comment);
    }
}
