package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.service.IHBasePassService;
import com.alex.passbook.utils.RowKeyGeneratorUtil;
import com.alex.passbook.vo.PassTemplateVo;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.Get;
import org.apache.hadoop.hbase.client.Put;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>PassTemplate HBase服务</h1>
 */
@Slf4j
@Service
public class HBasePassServiceImpl implements IHBasePassService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Override
    public boolean dropPassTemplateToHBase(PassTemplateVo passTemplateVo) {
        if(passTemplateVo == null){
            return false;
        }
        String rowKey = RowKeyGeneratorUtil.getPassTemplateRowKey(passTemplateVo);
        try{
            //通过HBase来查找是否有重复的rowKey
            if(hbaseTemplate.getConnection().getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME)).exists(new Get(Bytes.toBytes(rowKey)))){
                log.warn("RowKey {} is already exist!", rowKey);
                return false;
            }
        }catch (Exception ex){
            log.error("DropPassTemplateToHBase Error, {}", ex.getMessage());
        }
        /**
         * Put对象为写入HBase对象，通过rowKey，列族和属性名来写入
         */
        Put put = new Put(Bytes.toBytes(rowKey));
        put.addColumn(Constants.PT_FAMILY_B, Constants.PT_ID, Bytes.toBytes(passTemplateVo.getId()));
        put.addColumn(Constants.PT_FAMILY_B, Constants.PT_TITLE, Bytes.toBytes(passTemplateVo.getTitle()));
        put.addColumn(Constants.PT_FAMILY_B, Constants.PT_SUMMARY, Bytes.toBytes(passTemplateVo.getSummary()));
        put.addColumn(Constants.PT_FAMILY_B, Constants.PT_DESC, Bytes.toBytes(passTemplateVo.getDesc()));
        put.addColumn(Constants.PT_FAMILY_B, Constants.PT_HAS_TOKEN, Bytes.toBytes(passTemplateVo.getHasToken()));
        put.addColumn(Constants.PT_FAMILY_B, Constants.PT_BACKGROUND, Bytes.toBytes(passTemplateVo.getBackground()));
        put.addColumn(Constants.PT_FAMILY_C, Constants.PT_LIMIT, Bytes.toBytes(passTemplateVo.getLimit()));
        put.addColumn(Constants.PT_FAMILY_C, Constants.PT_START, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplateVo.getStart())));
        put.addColumn(Constants.PT_FAMILY_C, Constants.PT_END, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(passTemplateVo.getEnd())));
        hbaseTemplate.saveOrUpdate(Constants.PassTemplateTable.TABLE_NAME, put);
        return true;
    }
}
