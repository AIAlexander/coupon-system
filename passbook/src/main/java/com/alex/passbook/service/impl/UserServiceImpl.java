package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.service.IUserService;
import com.alex.passbook.vo.Response;
import com.alex.passbook.vo.UserVo;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hadoop.hbase.client.Mutation;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>创建用户服务实现</h1>
 */
@Slf4j
@Service
public class UserServiceImpl implements IUserService {



    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public Response createUser(UserVo userVo) throws Exception {
        Long currentUserCount = redisTemplate.opsForValue().increment(Constants.USE_COUNT_REDIS_KEY, 1);
        Long userId = generateUserId(currentUserCount);
        List<Mutation> puts = new ArrayList<>();
        Put put = new Put(Bytes.toBytes(userId));
        put.addColumn(Constants.USER_FAMILY_B, Constants.USER_NAME, Bytes.toBytes(userVo.getBasicInfo().getName()));
        put.addColumn(Constants.USER_FAMILY_B, Constants.USER_AGE, Bytes.toBytes(userVo.getBasicInfo().getAge()));
        put.addColumn(Constants.USER_FAMILY_B, Constants.USER_SEX, Bytes.toBytes(userVo.getBasicInfo().getSex()));
        put.addColumn(Constants.USER_FAMILY_O, Constants.USER_PHONE, Bytes.toBytes(userVo.getOtherInfo().getPhone()));
        put.addColumn(Constants.USER_FAMILY_O, Constants.USER_ADDRESS, Bytes.toBytes(userVo.getOtherInfo().getAddress()));
        puts.add(put);
        hbaseTemplate.saveOrUpdates(Constants.UserTable.TABLE_NAME, puts);
        log.info("HBase中创建了用户：{}", userId);
        userVo.setId(userId);
        return new Response(userVo);
    }

    /**
     * 生成UserId
     * @param prefix 当前用户数
     * @return 用户Id
     */
    private Long generateUserId(Long prefix){
        String suffix = RandomStringUtils.randomNumeric(5);
        return Long.valueOf(prefix + suffix);
    }
}
