package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.constant.ErrorCode;
import com.alex.passbook.dao.MerchantsDao;
import com.alex.passbook.entity.Merchants;
import com.alex.passbook.service.IMerchantsService;
import com.alex.passbook.vo.CreateMerchantsRequest;
import com.alex.passbook.vo.CreateMerchantsResponse;
import com.alex.passbook.vo.PassTemplate;
import com.alex.passbook.vo.Response;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author wsh
 * @date 2020-06-02
 * <h1>商户服务接口实现</h1>
 */
@Slf4j
@Service
public class MerchantsServiceImpl implements IMerchantsService {

    private final MerchantsDao merchantsDao;

    //kafka客户端
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @Autowired
    public MerchantsServiceImpl(MerchantsDao merchantsDao, KafkaTemplate<String, String> kafkaTemplate){
        this.merchantsDao = merchantsDao;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    @Transactional
    public Response createMerchants(CreateMerchantsRequest createMerchantsRequest) {
        Response response = new Response();
        CreateMerchantsResponse createMerchantsResponse = new CreateMerchantsResponse();
        ErrorCode errorCode = createMerchantsRequest.validate(merchantsDao);
        if(errorCode != ErrorCode.SUCCESS){
            createMerchantsResponse.setId(-1);
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        } else{
            createMerchantsResponse.setId(merchantsDao.save(createMerchantsRequest.toMerchants()).getId());
        }
        response.setData(createMerchantsResponse);
        return response;
    }

    @Override
    public Response buildMerchantsInfoById(Integer id) {
        Response response = new Response();
        Merchants merchants = merchantsDao.findById(id).get();
        if(null == merchants) {
            response.setErrorCode(ErrorCode.MERCHANTS_NOT_EXIST.getCode());
            response.setErrorMsg(ErrorCode.MERCHANTS_NOT_EXIST.getDesc());
        }
        response.setData(merchants);
        return response;
    }

    @Override
    public Response dropPassTemplate(PassTemplate passTemplate) {
        Response response = new Response();
        ErrorCode errorCode = passTemplate.validate(merchantsDao);
        if(errorCode != ErrorCode.SUCCESS){
            response.setErrorCode(errorCode.getCode());
            response.setErrorMsg(errorCode.getDesc());
        }else{
            //转换成JSON，投放到kafka客户端中
            String passTemplateString = JSON.toJSONString(passTemplate);
            kafkaTemplate.send(
                    Constants.TEMPLATE_TOPIC,
                    Constants.TEMPLATE_TOPIC,
                    passTemplateString
            );
            log.info("DropPassTemplates: {}", passTemplateString);
        }
        return response;
    }
}
