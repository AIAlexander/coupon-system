package com.alex.passbook.controller;

import com.alex.passbook.log.LogConstants;
import com.alex.passbook.log.LogGenerator;
import com.alex.passbook.service.IUserService;
import com.alex.passbook.vo.Response;
import com.alex.passbook.vo.UserVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wsh
 * @date 2020-08-19
 * <h1>创建用户Controller</h1>
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class CreateUserController {

    @Autowired
    private IUserService userService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * <h2>创建用户</h2>
     * @param userVo {@link UserVo}
     * @return {@link Response}
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/user")
    Response user(@RequestBody UserVo userVo) throws Exception{
        LogGenerator.generateLog(httpServletRequest, -1L,
                LogConstants.ActionName.CREATE_USER, userVo);
        return userService.createUser(userVo);
    }
}
