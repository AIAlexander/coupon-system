package com.alex.passbook.mapper;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.vo.PassVo;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>Pass与HBase中的Pass的映射</h1>
 */
public class PassMapper implements RowMapper<PassVo> {



    @Override
    public PassVo mapRow(Result result, int i) throws Exception {
        String[] patterns = new String[]{"yyyy-MM-dd"};
        PassVo passVo = new PassVo();
        passVo.setUserId(Bytes.toLong(result.getValue(Constants.P_FAMILY_I, Constants.P_USER_ID)));
        passVo.setTemplateId(Bytes.toString(result.getValue(Constants.P_FAMILY_I, Constants.P_TEMPLATE_ID)));
        passVo.setToken(Bytes.toString(result.getValue(Constants.P_FAMILY_I, Constants.P_TOKEN)));
        passVo.setAssignedDate(DateUtils.parseDate(Bytes.toString(result.getValue(Constants.P_FAMILY_I, Constants.P_ASSIGNED_DATE)), patterns));
        String conDateStr = Bytes.toString(result.getValue(Constants.P_FAMILY_I, Constants.P_CON_DATE));
        if("-1".equals(conDateStr)){
            passVo.setConDate(null);
        }else{
            passVo.setConDate(DateUtils.parseDate(conDateStr, patterns));
        }

        passVo.setRowKey(Bytes.toString(result.getRow()));
        return passVo;
    }
}
