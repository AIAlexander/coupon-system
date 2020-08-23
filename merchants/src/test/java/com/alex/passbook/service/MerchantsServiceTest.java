package com.alex.passbook.service;

import com.alex.passbook.vo.CreateMerchantsRequest;
import com.alex.passbook.vo.PassTemplate;
import com.alibaba.fastjson.JSON;
import org.apache.commons.lang.time.DateUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

/**
 * @author wsh
 * @date 2020-06-02
 * <h1>商户服务测试类</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class MerchantsServiceTest {

    @Autowired
    private IMerchantsService merchantsService;

    @Test
    @Transactional
    public void testCreateMerchantService(){
        CreateMerchantsRequest request = new CreateMerchantsRequest();
        request.setName("麦当劳");
        request.setLogoUrl("https://www.mcdonalds.com.cn/");
        request.setBusinessLicenseUrl("https://www.mcdonalds.com.cn/");
        request.setPhone("123456789");
        request.setAddress("浙江杭州");
        System.out.println(JSON.toJSONString(merchantsService.createMerchants(request)));
    }

    /**
     * {"data":{"address":"浙江杭州","businessLicenseUrl":"https://www.mcdonalds.com.cn/","id":18,"isAudit":false,
     * "logoUrl":"https://www.mcdonalds.com.cn/","name":"麦当劳","phone":"123456789"},"errorCode":0,"errorMsg":""}
     */
    @Test
    public void testBuildMerchantsInfoById(){
        System.out.println(JSON.toJSONString(merchantsService.buildMerchantsInfoById(18)));
    }

    @Test
    public void testDropPassTemplate(){
        PassTemplate passTemplate = new PassTemplate();
        passTemplate.setId(20);
        passTemplate.setTitle("肯德基");
        passTemplate.setSummary("简介: 肯德基优惠券");
        passTemplate.setDesc("详情: 肯德基优惠券");
        passTemplate.setLimit(10000L);
        passTemplate.setHasToken(true);
        passTemplate.setBackground(2);
        passTemplate.setStart(DateUtils.addDays(new Date(), -10));
        passTemplate.setEnd(DateUtils.addDays(new Date(), 10));
        System.out.println(JSON.toJSONString(merchantsService.dropPassTemplate(passTemplate)));
    }
}
