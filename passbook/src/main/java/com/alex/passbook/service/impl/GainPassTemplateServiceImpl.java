package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.service.IGainPassTemplateService;
import com.alex.passbook.utils.RowKeyGeneratorUtil;
import com.alex.passbook.vo.GainPassTemplateRequest;
import com.alex.passbook.vo.Response;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wsh
 * @date 2020-08-11
 * <h1>用户领取优惠券功能实现</h1>
 */
@Service
@Slf4j
public class GainPassTemplateServiceImpl implements IGainPassTemplateService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Response gainPassTemplate(GainPassTemplateRequest gainPassTemplateRequest) throws Exception {
        return null;
    }

    /**
     * 给用户添加优惠券
     * @param request {@link GainPassTemplateRequest}
     * @param merchantsId 商户id
     * @param passTemplateId 优惠券id
     * @return
     * @throws Exception
     */
    private boolean addPassForUser(GainPassTemplateRequest request,
                                   Integer merchantsId,
                                   String passTemplateId) throws Exception {
        List<Mutation> datas = new ArrayList<>();
        //生成行键
        Put put = new Put(Bytes.toBytes(RowKeyGeneratorUtil.getPassRowKey(request)));
        put.addColumn(Constants.P_FAMILY_I, Constants.P_USER_ID, Bytes.toBytes(request.getUserId()));
        put.addColumn(Constants.P_FAMILY_I, Constants.P_TEMPLATE_ID, Bytes.toBytes(passTemplateId));
        //如果领取的优惠券中有token
        if(request.getPassTemplateVo().getHasToken()){
            //token从redis中获取，并减少一个token
            String token = redisTemplate.opsForSet().pop(passTemplateId);
            if(token == null){
                log.error("Token is not exist: {}", passTemplateId);
                return false;
            }
            //将使用的token记录到文件中
            recordTokenToFile(merchantsId, passTemplateId, token);
            put.addColumn(Constants.P_FAMILY_I, Constants.P_TOKEN, Bytes.toBytes(token));
        }else{
            put.addColumn(Constants.P_FAMILY_I, Constants.P_TOKEN, Bytes.toBytes("-1"));
        }
        put.addColumn(Constants.P_FAMILY_I, Constants.P_ASSIGNED_DATE, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        //消费日期为-1表示未使用
        put.addColumn(Constants.P_CON_DATE, Constants.P_CON_DATE, Bytes.toBytes("-1"));
        datas.add(put);
        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME, datas);
        return true;
    }

    /**
     * 将已使用的token记录到文件中
     * 文件名： /tmp/token/merchantsId/passTemplateId+'_'
     * @param merchantsId
     * @param passTemplateId
     * @param token
     * @throws Exception
     */
    private void recordTokenToFile(Integer merchantsId, String passTemplateId, String token) throws Exception {
        Files.write(
                Paths.get(Constants.TOKEN_DIR, String.valueOf(merchantsId), passTemplateId + Constants.USED_TOKEN_SUFFIX),
                        (token + "\n").getBytes(),
                        StandardOpenOption.APPEND
        );
    }
}
