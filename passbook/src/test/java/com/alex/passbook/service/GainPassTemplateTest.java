package com.alex.passbook.service;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.utils.RowKeyGeneratorUtil;
import com.alex.passbook.vo.GainPassTemplateRequest;
import com.alex.passbook.vo.PassTemplateVo;
import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author wsh
 * @date 2020-08-19
 * <h1>用户领取优惠券测试</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class GainPassTemplateTest extends AbstractServiceTest{

    @Autowired
    IGainPassTemplateService gainPassTemplateService;

    @Autowired
    HbaseTemplate hbaseTemplate;

    @Test
    public void testGainPassTemplateTest() throws Exception{
        PassTemplateVo target = new PassTemplateVo();
        target.setId(20);
        target.setTitle("肯德基");
        target.setHasToken(true);
        System.out.println(JSON.toJSONString(
                gainPassTemplateService.gainPassTemplate(
                        new GainPassTemplateRequest(userId, target)
                )
        ));
    }

}
