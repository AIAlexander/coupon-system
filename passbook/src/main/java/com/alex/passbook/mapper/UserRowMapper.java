package com.alex.passbook.mapper;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.vo.UserVo;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>User与HBase的User映射</h1>
 */
public class UserRowMapper implements RowMapper<UserVo> {
    @Override
    public UserVo mapRow(Result result, int i) throws Exception {

        UserVo.BasicInfo basicInfo = new UserVo.BasicInfo(
                Bytes.toString(result.getValue(Constants.USER_FAMILY_B, Constants.USER_NAME)),
                Bytes.toInt(result.getValue(Constants.USER_FAMILY_B, Constants.USER_AGE)),
                Bytes.toString(result.getValue(Constants.USER_FAMILY_B, Constants.USER_SEX))
        );
        UserVo.OtherInfo otherInfo = new UserVo.OtherInfo(
                Bytes.toString(result.getValue(Constants.USER_FAMILY_O, Constants.USER_PHONE)),
                Bytes.toString(result.getValue(Constants.USER_FAMILY_O, Constants.USER_ADDRESS))
        );
        return new UserVo(Bytes.toLong(result.getRow()), basicInfo, otherInfo);
    }
}
