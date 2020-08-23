package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.mapper.PassTemplateMapper;
import com.alex.passbook.service.IGainPassTemplateService;
import com.alex.passbook.utils.RowKeyGeneratorUtil;
import com.alex.passbook.vo.GainPassTemplateRequest;
import com.alex.passbook.vo.PassTemplateVo;
import com.alex.passbook.vo.Response;
import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional
    public Response gainPassTemplate(GainPassTemplateRequest gainPassTemplateRequest) throws Exception {
        PassTemplateVo passTemplateVo;
        String passTemplateId = RowKeyGeneratorUtil.getPassTemplateRowKey(gainPassTemplateRequest.getPassTemplateVo());
        try{
            passTemplateVo = hbaseTemplate.get(
                    Constants.PassTemplateTable.TABLE_NAME,
                    passTemplateId,
                    new PassTemplateMapper()
            );
        }catch (Exception ex){
            log.error("Gain PassTemplate Error : {}", JSON.toJSONString(gainPassTemplateRequest.getPassTemplateVo()));
            return Response.error("Gain PassTemplate Error");
        }
        //查看该优惠券是否能被领取
        if(passTemplateVo.getLimit() <= 1 && passTemplateVo.getLimit() != -1){
            log.error("PassTemplate Limit : {}", JSON.toJSONString(gainPassTemplateRequest.getPassTemplateVo()));
            return Response.error("PassTemplate Limit");
        }
        //日期是否合法
        Date current = new Date();
        if(!(current.getTime() >= passTemplateVo.getStart().getTime()
                && current.getTime() <= passTemplateVo.getEnd().getTime())){
            log.error("PassTemplate ValidTime Error: {}", JSON.toJSONString(gainPassTemplateRequest.getPassTemplateVo()));
            return Response.error("PassTemplate ValidTime Error");
        }
        // 减去优惠券的limit,limit == -1 表示可以无限领取
        // 如果limit不等于-1，需要减去领取优惠券的limit
//        if(passTemplateVo.getLimit() != -1){
//            List<Mutation> datas = new ArrayList<>();
//            Put put = new Put(Bytes.toBytes(passTemplateId));
//            put.addColumn(Constants.PT_FAMILY_C, Constants.PT_LIMIT,
//                    Bytes.toBytes(passTemplateVo.getLimit() - 1));
//            datas.add(put);
//            hbaseTemplate.saveOrUpdates(Constants.PassTemplateTable.TABLE_NAME, datas);
//        }
        //将优惠券保存到用户优惠券表
        if(!addPassForUser(gainPassTemplateRequest, passTemplateVo.getId(), passTemplateId)){
            return Response.error("Gain PassTemplate Error!");
        }
        return Response.success();
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
        put.addColumn(Constants.P_FAMILY_I, Constants.P_CON_DATE, Bytes.toBytes("-1"));
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
                        StandardOpenOption.CREATE, StandardOpenOption.APPEND
        );
    }
}
