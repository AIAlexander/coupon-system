package com.alex.passbook.service;

import com.alex.passbook.vo.Response;
import com.alex.passbook.vo.UserVo;
import com.alibaba.fastjson.JSON;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @author wsh
 * @date 2020-08-19
 * <h1>用户服务测试</h1>
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class UserServiceTest {

    @Autowired
    IUserService userService;


    //{"data":
    // {"basicInfo":{"age":22,"name":"alex","sex":"m"},
    // "id":170928,
    // "otherInfo":{"address":"浙江杭州","phone":"1390000000"}},"errorCode":0,"errorMsg":""}
    @Test
    public void testCreateUser() throws Exception{
        UserVo userVo = new UserVo();
        userVo.setBasicInfo(new UserVo.BasicInfo("sam", 22, "m"));
        userVo.setOtherInfo(new UserVo.OtherInfo("13911111231", "浙江杭州"));
        Response user = userService.createUser(userVo);
        System.out.println(JSON.toJSONString(user));
    }
}
