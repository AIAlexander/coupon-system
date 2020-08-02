package com.alex.passbook.service;

import com.alex.passbook.vo.PassTemplateVo;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>Pass Hbase服务</h1>
 */
public interface IHBasePassService {

    /**
     * <h2>将PassTemplate 写入 HBase</h2>
     * @param passTemplateVo {@link PassTemplateVo}
     * @return
     */
    boolean dropPassTemplateToHBase(PassTemplateVo passTemplateVo);
}
