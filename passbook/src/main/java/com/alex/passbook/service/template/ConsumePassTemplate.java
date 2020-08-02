package com.alex.passbook.service.template;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.service.IHBasePassService;
import com.alex.passbook.vo.PassTemplateVo;
import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @author wsh
 * @date 2020-08-02
 * <h2>消费Kafka中的PassTemplate</h2>
 */
@Slf4j
@Component
public class ConsumePassTemplate {

    @Autowired
    private IHBasePassService passService;

    @KafkaListener(topics = {Constants.TEMPLATE_TOPIC})
    public void receive(@Payload String passTemplate,
                        @Header(KafkaHeaders.RECEIVED_MESSAGE_KEY) String key,
                        @Header(KafkaHeaders.RECEIVED_PARTITION_ID) int partition,
                        @Header(KafkaHeaders.RECEIVED_TOPIC) String topic){
        log.info("Consume Receive PassTemplate: {}", passTemplate);
        PassTemplateVo passTemplateVo;
        try{
            passTemplateVo = JSON.parseObject(passTemplate, PassTemplateVo.class);
        } catch (Exception ex){
            log.error("Parse PassTemplate Error: {}", ex.getMessage());
            return;
        }
        log.info("DropPassTemplateToHBase: {}", passService.dropPassTemplateToHBase(passTemplateVo));

    }
}
