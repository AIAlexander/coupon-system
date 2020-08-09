package com.alex.passbook.utils;

import com.alex.passbook.vo.FeedbackVo;
import com.alex.passbook.vo.GainPassTemplateRequest;
import com.alex.passbook.vo.PassTemplateVo;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>HBase RowKey生成器工具类</h1>
 */
@Slf4j
public class RowKeyGeneratorUtil {

    /**
     * 根据提供的PassTemplate对象生成RowKey
     * @param passTemplateVo {@link PassTemplateVo}
     * @return String rowKey
     */
    public static String getPassTemplateRowKey(PassTemplateVo passTemplateVo){
        //生成唯一的优惠券信息
        String passInfo = String.valueOf(passTemplateVo.getId()) + "_" + passTemplateVo.getTitle();
        /**
         * 通过唯一的优惠券信息生成rowKey
         * 使用md5生成rowKey是因为HBase是一个集群，hbase会根据rowKey来进行存储，rowkey相近会存在一起
         * 如果不对rowKey进行处理，则会存储在HBase的一个节点中，这样的话不利于负载均衡，
         * 将rowKey进行分散处理，能提高HBase的查询效率
         */
        String rowKey = DigestUtils.md5Hex(passInfo);
        log.info("getPassTemplateRpwKey: {}, {}", passInfo, rowKey);
        return rowKey;
    }

    /**
     * 根据提供的Feedback对象生成RowKey
     * @param feedbackVo {@link FeedbackVo}
     * @return String rowKey
     */
    public static String getFeedbackRowKey(FeedbackVo feedbackVo) {
        /**
         * 第一部分（String.valueOf(feedbackVo.getUserId())).reverse()）
         *      任何一个反馈信息都有特定的用户，将某个用户的反馈信息的rowKey相近一点，便于做scan操作来获取所有的反馈信息
         *      reverse的原因是用户的id是根据用户数量产生的，用户数量不断增大后，前缀不会发生变化，后缀是随机数，这样反转之后，就能让rowKey分散，从而提高HBase的查询效率
         * 第二部分（Long.MAX_VALUE - System.currentTimeMillis()）
         *      在存储feedback的时候才会生成rowKey，这样的好处是，越晚生成的反馈信息的rowKey会越在前面，这对于获取最近的评论较为容易，便于分页
         */
        String rowKey = new StringBuilder(String.valueOf(feedbackVo.getUserId())).reverse().toString() +
                (Long.MAX_VALUE - System.currentTimeMillis());
        log.info("getFeedbackRowKey: {}, {}", feedbackVo, rowKey);
        return rowKey;
    }

    /**
     * 根据提供的领取优惠券请求生成RowKey，在用户领取优惠券时候使用
     * Pass RowKey = reverse(userId) + (Long.Max - timestamp) + PassTemplate RowKey
     * 前两个部分的设计与FeedBack相同，后面加上PassTemplate的rowKey是便于查询哪些用户领取过相同的优惠券，可以通过后缀的Scan来进行扫描
     * @param request {@link GainPassTemplateRequest}
     * @return String rowKey
     */
    public static String getPassRowKey(GainPassTemplateRequest request){
        String rowKey = new StringBuilder(String.valueOf(request.getUserId())).reverse().toString() +
                (Long.MAX_VALUE - System.currentTimeMillis()) +
                getPassTemplateRowKey(request.getPassTemplateVo());
        log.info("getPassRowKey when getting the pass : {}, {}", request, rowKey);
        return rowKey;
    }
}
