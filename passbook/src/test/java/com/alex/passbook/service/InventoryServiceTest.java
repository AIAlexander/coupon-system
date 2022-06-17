package com.alex.passbook.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wsh
 * @date 2020-08-19
 * <h1>库存服务测试</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class InventoryServiceTest extends AbstractServiceTest {

    @Autowired
    private IInventorySevice inventoryService;

    @Test
    public void testGetInventory() throws Exception{
        System.out.println(JSON.toJSONString(
                inventoryService.getInventoryInfo(userId),
                SerializerFeature.DisableCircularReferenceDetect
                )
        );
    }
}