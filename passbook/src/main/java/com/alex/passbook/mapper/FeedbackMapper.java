package com.alex.passbook.mapper;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.vo.FeedbackVo;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>feedback与HBase中的feedback的映射</h1>
 */
public class FeedbackMapper implements RowMapper<FeedbackVo> {
    @Override
    public FeedbackVo mapRow(Result result, int i) throws Exception {

        FeedbackVo feedbackVo = new FeedbackVo();
        feedbackVo.setUserId(Bytes.toLong(result.getValue(Constants.F_FAMILY_I, Constants.F_USER_ID)));
        feedbackVo.setType(Bytes.toString(result.getValue(Constants.F_FAMILY_I, Constants.F_TYPE)));
        feedbackVo.setTemplateId(Bytes.toString(result.getValue(Constants.F_FAMILY_I, Constants.F_TEMPLATE_ID)));
        feedbackVo.setComment(Bytes.toString(result.getValue(Constants.F_FAMILY_I, Constants.F_COMMENT)));
        return feedbackVo;
    }
}
