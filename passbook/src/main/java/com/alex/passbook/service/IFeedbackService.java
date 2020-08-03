package com.alex.passbook.service;

import com.alex.passbook.vo.FeedbackVo;
import com.alex.passbook.vo.Response;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>评论服务</h1>
 */
public interface IFeedbackService {

    /**
     * <h2>创建评论</h2>
     * @param feedbackVo {@link FeedbackVo}
     * @return {@link Response}
     */
    Response createFeedback(FeedbackVo feedbackVo);

    /**
     * <h2>获取用户评论</h2>
     * @param userId 用户id
     * @return {@link Response}
     */
    Response getFeedBack(Long userId);
}
