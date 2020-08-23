package com.alex.passbook.service;

import com.alex.passbook.vo.PassVo;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wsh
 * @date 2020-08-22
 * <h1>用户优惠券服务测试</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserPassServiceTest extends AbstractServiceTest {
    @Autowired
    private IUserPassService userPassService;

    @Test
    public void testGetUserPassInfo() throws Exception{
        System.out.println(JSON.toJSONString(
                userPassService.getUserPassInfo(userId)));
    }

    @Test
    public void testGetUserUsedPassInfo() throws Exception{
        System.out.println(JSON.toJSONString(
                userPassService.getUserUsedPassInfo(userId)));
    }

    @Test
    public void testGetUserAllPassInfo() throws Exception{
        System.out.println(JSON.toJSONString(
                userPassService.getUserAllPassInfo(userId)));
    }
//    {
//        "data": [
//        {
//            "merchants": {
//            "address": "浙江杭州",
//                    "businessLicenseUrl": "www.kfc.com",
//                    "id": 20,
//                    "isAudit": true,
//                    "logUrl": "www.kfc.com",
//                    "name": "肯德基",
//                    "phone": "400823823"
//        },
//            "passTemplateVo": {
//            "background": 2,
//                    "desc": "详情: 肯德基优惠券",
//                    "end": 1598630400000,
//                    "hasToken": true,
//                    "id": 20,
//                    "limit": 9998,
//                    "start": 1596902400000,
//                    "summary": "简介: 肯德基优惠券",
//                    "title": "肯德基"
//        },
//            "passVo": {
//            "assignedDate": 1598025600000,
//                    "rowKey": "[B@66456506",
//                    "templateId": "4a7654f84800e33b8529932f2518a1d3",
//                    "token": "token-16",
//                    "userId": 170928
//        }
//        }
//  ],
//        "errorCode": 0,
//            "errorMsg": ""
//    }

    @Test
    public void testUserUsePass(){
        PassVo passVo = new PassVo();
        passVo.setTemplateId("4a7654f84800e33b8529932f2518a1d3");
        passVo.setUserId(userId);
        System.out.println(JSON.toJSONString(userPassService.usePass(passVo)));
    }
}
