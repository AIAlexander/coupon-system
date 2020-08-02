package com.alex.passbook.mapper;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.vo.PassTemplateVo;
import com.spring4all.spring.boot.starter.hbase.api.RowMapper;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.client.Result;
import org.apache.hadoop.hbase.util.Bytes;

/**
 * @author wsh
 * @date 2020-08-01
 * <h1>PassTemplate与HBase中的PassTemplate的映射</h1>
 */
public class PassTemplateMapper implements RowMapper<PassTemplateVo> {
    @Override
    public PassTemplateVo mapRow(Result result, int i) throws Exception {
        String[] patterns = new String[]{"yyyy-MM-dd"};
        PassTemplateVo passTemplateVo = new PassTemplateVo();
        passTemplateVo.setId(Bytes.toInt(result.getValue(Constants.PT_FAMILY_B, Constants.PT_ID)));
        passTemplateVo.setTitle(Bytes.toString(result.getValue(Constants.PT_FAMILY_B, Constants.PT_TITLE)));
        passTemplateVo.setSummary(Bytes.toString(result.getValue(Constants.PT_FAMILY_B, Constants.PT_SUMMARY)));
        passTemplateVo.setDesc(Bytes.toString(result.getValue(Constants.PT_FAMILY_B, Constants.PT_DESC)));
        passTemplateVo.setHasToken(Bytes.toBoolean(result.getValue(Constants.PT_FAMILY_B, Constants.PT_HAS_TOKEN)));
        passTemplateVo.setBackground(Bytes.toInt(result.getValue(Constants.PT_FAMILY_B, Constants.PT_BACKGROUND)));
        passTemplateVo.setLimit(Bytes.toLong(result.getValue(Constants.PT_FAMILY_C, Constants.PT_LIMIT)));
        passTemplateVo.setStart(DateUtils.parseDate(Bytes.toString(result.getValue(Constants.PT_FAMILY_C, Constants.PT_START)), patterns));
        passTemplateVo.setEnd(DateUtils.parseDate(Bytes.toString(result.getValue(Constants.PT_FAMILY_C, Constants.PT_END)), patterns));
        return passTemplateVo;
    }
}
