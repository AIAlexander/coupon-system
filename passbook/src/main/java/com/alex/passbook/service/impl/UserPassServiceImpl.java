package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.constant.PassStatus;
import com.alex.passbook.dao.MerchantsDao;
import com.alex.passbook.entity.Merchants;
import com.alex.passbook.mapper.PassMapper;
import com.alex.passbook.service.IUserPassService;
import com.alex.passbook.vo.PassInfoVo;
import com.alex.passbook.vo.PassTemplateVo;
import com.alex.passbook.vo.PassVo;
import com.alex.passbook.vo.Response;
import com.alibaba.fastjson.JSON;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.time.DateFormatUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.hadoop.hbase.TableName;
import org.apache.hadoop.hbase.client.*;
import org.apache.hadoop.hbase.filter.*;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wsh
 * @date 2020-08-03
 */
@Slf4j
@Service
public class UserPassServiceImpl implements IUserPassService {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private MerchantsDao merchantsDao;

    @Override
    public Response getUserPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.UNUSED);
    }

    @Override
    public Response getUserUsedPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.USED);
    }

    @Override
    public Response getUserAllPassInfo(Long userId) throws Exception {
        return getPassInfoByStatus(userId, PassStatus.ALL);
    }

    @Override
    public Response usePass(PassVo passVo) {
        //根据用户Id生成HBase中的行键的前缀
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(passVo.getUserId())).reverse().toString());
        Scan scan = new Scan();
        List<Filter> filters = new ArrayList<>();
        filters.add(new PrefixFilter(rowPrefix));
        filters.add(new SingleColumnValueFilter(
                Constants.P_FAMILY_I, Constants.P_TEMPLATE_ID,
                CompareFilter.CompareOp.EQUAL, Bytes.toBytes(passVo.getTemplateId())));
        //未使用过的优惠券
        filters.add(new SingleColumnValueFilter(Constants.P_FAMILY_I, Constants.P_CON_DATE,
                CompareFilter.CompareOp.EQUAL, Bytes.toBytes("-1")));
        scan.setFilter(new FilterList(filters));
        List<PassVo> passVos = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassMapper());
        if(null == passVos || passVos.size() < 1){
            log.error("UserUsePass Error : {}", JSON.toJSONString(passVo));
            return Response.error("UserUsePass Error!");
        }
        //修改CON_DATE（消费日期）
        List<Mutation> datas = new ArrayList<>();
        Put put = new Put(passVos.get(0).getRowKey().getBytes());
        //将CON_DATE将-1改成消费时间
        put.addColumn(Constants.P_FAMILY_I, Constants.P_CON_DATE, Bytes.toBytes(DateFormatUtils.ISO_DATE_FORMAT.format(new Date())));
        datas.add(put);
        hbaseTemplate.saveOrUpdates(Constants.PassTable.TABLE_NAME, datas);
        return Response.success();
    }

    /**
     * <h2>根据优惠券状态获取优惠券信息</h2>
     * @param userId 用户Id
     * @param passStatus {@link PassStatus}
     * @return {@link Response}
     */
    private Response getPassInfoByStatus(Long userId, PassStatus passStatus) throws Exception{
        //根据用户Id生成HBase中的行键的前缀
        byte[] rowPrefix = Bytes.toBytes(new StringBuilder(String.valueOf(userId)).reverse().toString());
        //设置HBase的比较器
        CompareFilter.CompareOp compareOp = passStatus ==
                PassStatus.UNUSED ? CompareFilter.CompareOp.EQUAL : CompareFilter.CompareOp.NOT_EQUAL;
        //定义扫描器来筛选HBase的数据
        Scan scan = new Scan();
        //定义过滤器列表添加到Scan中进行过滤
        List<Filter> filters = new ArrayList<>();
        //行键前缀过滤器，找到特定用户的优惠券
        filters.add(new PrefixFilter(rowPrefix));
        //基于列单元值的过滤器，找到未使用或者使用的优惠券的过滤器
        if(passStatus != PassStatus.ALL){
            filters.add(new SingleColumnValueFilter(Constants.P_FAMILY_I,
                    Constants.P_CON_DATE, compareOp, Bytes.toBytes("-1")));
        }
        //将过滤器列表设置到扫描器中，表示 'AND'  的条件过滤（行键前缀过滤器 + 列族的过滤器）
        scan.setFilter(new FilterList(filters));
        //查询优惠券
        List<PassVo> passes = hbaseTemplate.find(Constants.PassTable.TABLE_NAME, scan, new PassMapper());
        //通过优惠券获取优惠券信息
        Map<String, PassTemplateVo> passTemplateVoMap = buildPassTemplateMap(passes);
        //通过优惠券信息获取商户信息
        Map<Integer, Merchants> merchantsMap = buildMerchantsMap(
                new ArrayList<>(passTemplateVoMap.values()));
        //填充返回值
        List<PassInfoVo> passInfoVoList = new ArrayList<>();
        for (PassVo pass: passes) {
            PassTemplateVo passTemplateVo = passTemplateVoMap.getOrDefault(pass.getTemplateId(), null);
            if(null == passTemplateVo){
                log.error("PassTemplate is not exist : {}", pass.getTemplateId());
                continue;
            }

            Merchants merchants = merchantsMap.getOrDefault(passTemplateVo.getId(), null);
            if(null == merchants) {
                log.error("Merchants is not exist : {}", passTemplateVo.getId());
            }
            passInfoVoList.add(new PassInfoVo(pass, passTemplateVo, merchants));
        }
        return new Response(passInfoVoList);
    }

    /**
     * <h2>通过获取的Pass构造Map</h2>
     * 返回给用户不仅需要优惠券的信息还要优惠券模版的信息
     * @param passVoList {@link PassVo}
     * @return Map {@link PassTemplateVo}
     */
    private Map<String, PassTemplateVo> buildPassTemplateMap(List<PassVo> passVoList) throws Exception{
        String[] patterns = new String[]{"yyyy-MM-dd"};
        //PassVo中的templateId是PassTemplate在HBase中的行键，通过行键来查找PassTemplate
        List<String> templateIds = passVoList.stream().map(
                PassVo::getTemplateId
        ).collect(Collectors.toList());
        List<Get> templateGets = new ArrayList<>(templateIds.size());
        templateIds.forEach(
                t -> templateGets.add(new Get(Bytes.toBytes(t)))
        );
        Result[] templateResults = hbaseTemplate.getConnection().
                getTable(TableName.valueOf(Constants.PassTemplateTable.TABLE_NAME)).get(templateGets);
        //构造 PassTemplateId -> PassTemplate Object的Map，用于构造PassInfo
        Map<String, PassTemplateVo> templateVoMap = new HashMap<>();
        for (Result result : templateResults) {
            PassTemplateVo passTemplateVo = new PassTemplateVo();
            passTemplateVo.setId(Bytes.toInt(result.getValue(Constants.PT_FAMILY_B, Constants.PT_ID)));
            passTemplateVo.setTitle(Bytes.toString(result.getValue(Constants.PT_FAMILY_B, Constants.PT_TITLE)));
            passTemplateVo.setSummary(Bytes.toString(result.getValue(Constants.PT_FAMILY_B, Constants.PT_SUMMARY)));
            passTemplateVo.setDesc(Bytes.toString(result.getValue(Constants.PT_FAMILY_B, Constants.PT_DESC)));
            passTemplateVo.setHasToken(Bytes.toBoolean(result.getValue(Constants.PT_FAMILY_B, Constants.PT_HAS_TOKEN)));
            passTemplateVo.setBackground(Bytes.toInt(result.getValue(Constants.PT_FAMILY_B, Constants.PT_BACKGROUND)));
            passTemplateVo.setLimit(Bytes.toLong(result.getValue(Constants.PT_FAMILY_C, Constants.PT_LIMIT)));
            passTemplateVo.setStart(DateUtils.parseDate(
                    Bytes.toString(result.getValue(Constants.PT_FAMILY_C, Constants.PT_START)), patterns));
            passTemplateVo.setEnd(DateUtils.parseDate(
                    Bytes.toString(result.getValue(Constants.PT_FAMILY_C, Constants.PT_END)), patterns));
            templateVoMap.put(Bytes.toString(result.getRow()), passTemplateVo);
        }
        return templateVoMap;
    }

    /**
     * 通过获取的PassTemplate对象构造Merchants Map
     * @param passTemplateVos {@link PassTemplateVo}
     * @return Map {@link Merchants}
     */
    private Map<Integer, Merchants> buildMerchantsMap(List<PassTemplateVo> passTemplateVos) {
        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        //PassTemplate中的id 表示所属商户的id
        List<Integer> merchantsIds = passTemplateVos.stream().map(
                PassTemplateVo::getId
        ).collect(Collectors.toList());
        //通过PassTemplate的id在数据库中查询相关的商户
        List<Merchants> merchants = merchantsDao.findByIds(merchantsIds);
        merchants.forEach(m -> merchantsMap.put(m.getId(), m));
        return merchantsMap;
    }
}
