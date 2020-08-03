package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.mapper.FeedbackMapper;
import com.alex.passbook.service.IFeedbackService;
import com.alex.passbook.utils.RowKeyGeneratorUtil;
import com.alex.passbook.vo.FeedbackVo;
import com.alex.passbook.vo.Response;
import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.PrefixFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author wsh
 * @date 2020-08-03
 * <h1>评论功能实现</h1>
 */
@Slf4j
@Service
public class FeedbackServiceImpl implements IFeedbackService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public Response createFeedback(FeedbackVo feedbackVo) {
        if(!feedbackVo.validate()){
            log.error("Feedback Error: {}", JSON.toJSONString(feedbackVo));
            return Response.error("Feedback Error");
        }
        Put put = new Put(Bytes.toBytes(RowKeyGeneratorUtil.getFeedbackRowKey(feedbackVo)));
        put.addColumn(Constants.F_FAMILY_I, Constants.F_USER_ID, Bytes.toBytes(feedbackVo.getUserId()));
        put.addColumn(Constants.F_FAMILY_I, Constants.F_TYPE, Bytes.toBytes(feedbackVo.getType()));
        put.addColumn(Constants.F_FAMILY_I, Constants.F_TEMPLATE_ID, Bytes.toBytes(feedbackVo.getTemplateId()));
        put.addColumn(Constants.F_FAMILY_I, Constants.F_COMMENT, Bytes.toBytes(feedbackVo.getComment()));
        hbaseTemplate.saveOrUpdate(Constants.FeedbackTable.TABLE_NAME, put);
        return Response.success();
    }

    @Override
    public Response getFeedBack(Long userId) {
        //从HBase中获取FeedBack
        //根据feedback的RowKey产生的逻辑，来查询feedback
        //Feedback的RowKey生成逻辑： userId的倒叙 + （最大数 - 插入时间）
        byte[] reverseUserId = new StringBuilder(String.valueOf(userId)).reverse().toString().getBytes();
        //Hbase中的扫描器
        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(reverseUserId));
        List<FeedbackVo> feedbackVos =
                hbaseTemplate.find(Constants.FeedbackTable.TABLE_NAME, scan, new FeedbackMapper());
        return new Response(feedbackVos);
    }
}
