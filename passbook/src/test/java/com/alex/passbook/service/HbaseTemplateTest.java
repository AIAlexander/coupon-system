package com.alex.passbook.service;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.mapper.PassMapper;
import com.alex.passbook.mapper.PassTemplateMapper;
import com.alex.passbook.vo.PassTemplateVo;
import com.alex.passbook.vo.PassVo;
import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

/**
 * @author wsh
 * @date 2020-08-22
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class HbaseTemplateTest extends AbstractServiceTest{

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Test
    public void test(){
//        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
//        //LIMIT > 0 或者等于 -1 就是系统中可以用的优惠券
//        filterList.addFilter(new SingleColumnValueFilter(
//                Constants.PT_FAMILY_C, Constants.PT_LIMIT, CompareFilter.CompareOp.GREATER, new LongComparator(0L)
//        ));
//        filterList.addFilter(new SingleColumnValueFilter(
//                Constants.PT_FAMILY_C, Constants.PT_LIMIT, CompareFilter.CompareOp.EQUAL, Bytes.toBytes("-1")
//        ));
        byte[] rowKeyPrefix = Bytes.toBytes(new StringBuilder(String.valueOf("8")).reverse().toString());
        Scan scan = new Scan();
        scan.setFilter(new PrefixFilter(rowKeyPrefix));
        List<PassVo> validPassTemplates =
                hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassMapper());
        System.out.println(JSON.toJSONString(validPassTemplates));
    }
}
