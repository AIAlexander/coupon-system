package com.alex.passbook.service;

import com.alex.passbook.vo.Response;
import com.alex.passbook.vo.UserVo;

/**
 * @author wsh
 * @date 2020-08-02
 * <h1>用户服务的接口</h1>
 */
public interface IUserService {

    /**
     * <h2>创建用户</h2>
     * @param userVo {@link UserVo}
     * @return {@link Response}
     */
    Response createUser(UserVo userVo) throws Exception;
}
