package com.alex.passbook.service.impl;

import com.alex.passbook.constant.Constants;
import com.alex.passbook.dao.MerchantsDao;
import com.alex.passbook.entity.Merchants;
import com.alex.passbook.mapper.PassTemplateMapper;
import com.alex.passbook.service.IInventorySevice;
import com.alex.passbook.service.IUserPassService;
import com.alex.passbook.utils.RowKeyGeneratorUtil;
import com.alex.passbook.vo.*;
import com.spring4all.spring.boot.starter.hbase.api.HbaseTemplate;
import lombok.extern.slf4j.Slf4j;
import org.apache.hadoop.hbase.client.Scan;
import org.apache.hadoop.hbase.filter.CompareFilter;
import org.apache.hadoop.hbase.filter.FilterList;
import org.apache.hadoop.hbase.filter.LongComparator;
import org.apache.hadoop.hbase.filter.SingleColumnValueFilter;
import org.apache.hadoop.hbase.util.Bytes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author wsh
 * @date 2020-08-09
 * <h1>操作用户未领取的优惠券</h1>
 */
@Slf4j
@Service
public class InventoryServiceImpl implements IInventorySevice {

    @Autowired
    private HbaseTemplate hbaseTemplate;

    @Autowired
    private MerchantsDao merchantsDao;

    private IUserPassService userPassService;

    @Override
    public Response getInventoryInfo(Long userId) throws Exception {
        Response allUserPass = userPassService.getUserAllPassInfo(userId);
        List<PassInfoVo> passInfoVoList = (List<PassInfoVo>) allUserPass.getData();
        List<PassTemplateVo> excludeObject = passInfoVoList.stream().map(
                PassInfoVo::getPassTemplateVo
        ).collect(Collectors.toList());
        List<String> excludeIds = new ArrayList<>();
        excludeObject.forEach(o -> excludeIds.add(RowKeyGeneratorUtil.getPassTemplateRowKey(o)));
        return new Response(new InventoryResponseVo(
                userId, buildPassTemplateInfo(getAvailablePassTemplate(excludeIds))));
    }

    /**
     * 获得系统中可以用的优惠券
     * @param excludeIds 需要排除的优惠券ids
     * @return {@link PassTemplateVo}
     */
    private List<PassTemplateVo> getAvailablePassTemplate(List<String> excludeIds) {
        FilterList filterList = new FilterList(FilterList.Operator.MUST_PASS_ONE);
        //LIMIT > 0 或者等于 -1 就是系统中可以用的优惠券
        filterList.addFilter(new SingleColumnValueFilter(
                Constants.PT_FAMILY_C, Constants.PT_LIMIT, CompareFilter.CompareOp.GREATER, new LongComparator(0L)
        ));
        filterList.addFilter(new SingleColumnValueFilter(
                Constants.PT_FAMILY_C, Constants.PT_LIMIT, CompareFilter.CompareOp.EQUAL, Bytes.toBytes("-1")
        ));
        Scan scan = new Scan();
        scan.setFilter(filterList);
        List<PassTemplateVo> validPassTemplates =
                hbaseTemplate.find(Constants.PassTemplateTable.TABLE_NAME, scan, new PassTemplateMapper());
        List<PassTemplateVo> availablePassTemplates = new ArrayList<>();
        Date currentDate = new Date();
        for (PassTemplateVo p : validPassTemplates) {
            if(excludeIds.contains(RowKeyGeneratorUtil.getPassTemplateRowKey(p))){
                continue;
            }
            if(p.getStart().getTime() <= currentDate.getTime() && p.getEnd().getTime() >= currentDate.getTime()){
                availablePassTemplates.add(p);
            }
        }
        return availablePassTemplates;
    }

    /**
     * <h2>构造优惠券的返回信息</h2>
     * @param passTemplateVos {@link List<PassTemplateVo>}
     * @return {@link PassTemplateInfoVo}
     */
    private List<PassTemplateInfoVo> buildPassTemplateInfo(List<PassTemplateVo> passTemplateVos){
        Map<Integer, Merchants> merchantsMap = new HashMap<>();
        List<Integer> merchantsIds = passTemplateVos.stream().map(
                PassTemplateVo::getId
        ).collect(Collectors.toList());
        List<Merchants> merchants = merchantsDao.findByIds(merchantsIds);
        merchants.forEach(m -> merchantsMap.put(m.getId(), m));
        List<PassTemplateInfoVo> result = new ArrayList<>(passTemplateVos.size());
        for (PassTemplateVo passTemplateVo : passTemplateVos) {
            Merchants mc = merchantsMap.getOrDefault(passTemplateVo.getId(), null);
            if(null == mc){
                log.error("Merchants is not Exist : {}", passTemplateVo.getId());
                continue;
            }
            result.add(new PassTemplateInfoVo(passTemplateVo, mc));
        }
        return result;
    }
}
