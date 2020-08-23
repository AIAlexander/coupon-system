package com.alex.passbook.controller;

import com.alex.passbook.log.LogConstants;
import com.alex.passbook.log.LogGenerator;
import com.alex.passbook.service.IFeedbackService;
import com.alex.passbook.service.IGainPassTemplateService;
import com.alex.passbook.service.IInventorySevice;
import com.alex.passbook.service.IUserPassService;
import com.alex.passbook.vo.FeedbackVo;
import com.alex.passbook.vo.GainPassTemplateRequest;
import com.alex.passbook.vo.PassVo;
import com.alex.passbook.vo.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * @author wsh
 * @date 2020-08-19
 * <h1>Passbook Rest Controller</h1>
 */
@Slf4j
@RestController
@RequestMapping("/passbook")
public class PassbookController {

    //用户优惠券服务
    @Autowired
    private IUserPassService userPassService;

    //优惠券库存服务
    @Autowired
    private IInventorySevice inventoryService;

    //领取优惠券服务
    @Autowired
    private IGainPassTemplateService gainPassTemplateService;

    //反馈服务
    @Autowired
    private IFeedbackService feedbackService;

    @Autowired
    private HttpServletRequest httpServletRequest;

    /**
     * <h2>获取用户未使用的优惠券信息</h2>
     * @param userId 用户Id
     * @return {@link Response}
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/unused-pass-info")
    Response userPassInfo(Long userId) throws Exception{
        //生成日志
        LogGenerator.generateLog(httpServletRequest, userId,
                LogConstants.ActionName.USER_PASS_INFO, null);
        return userPassService.getUserPassInfo(userId);
    }

    /**
     * <h2>获取用户使用的优惠券信息</h2>
     * @param userId 用户Id
     * @return {@link Response}
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/used-pass-info")
    Response userUsedPassInfo(Long userId) throws Exception{
        //生成日志
        LogGenerator.generateLog(httpServletRequest, userId,
                LogConstants.ActionName.USER_USED_PASS_INFO, null);
        return userPassService.getUserUsedPassInfo(userId);
    }

    /**
     * <h2>用户使用优惠券</h2>
     * @param passVo {@link PassVo}
     * @return {@link Response}
     */
    @ResponseBody
    @PostMapping("/use-pass")
    Response usePass(@RequestBody  PassVo passVo){
        LogGenerator.generateLog(httpServletRequest, passVo.getUserId(),
                LogConstants.ActionName.USER_USE_PASS, passVo);
        return userPassService.usePass(passVo);
    }

    /**
     * <h2>用户领取优惠券</h2>
     * @param request {@link GainPassTemplateRequest}
     * @return {@link Response}
     * @throws Exception
     */
    @ResponseBody
    @PostMapping("/gain-pass-template")
    Response gainPassTemplate(@RequestBody GainPassTemplateRequest request) throws Exception{
        LogGenerator.generateLog(httpServletRequest, request.getUserId(),
                LogConstants.ActionName.GAIN_PASS_TEMPLATE, request);
        return gainPassTemplateService.gainPassTemplate(request);
    }

    /**
     * <h2>用户查询优惠券库存信息</h2>
     * @param userId 用户id
     * @return {@link Response}
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/inventory-info")
    Response inventoryInfo(Long userId) throws Exception{
        LogGenerator.generateLog(httpServletRequest, userId,
                LogConstants.ActionName.INVENTORY_INFO, null);
        return inventoryService.getInventoryInfo(userId);
    }

    /**
     * <h2>用户评论</h2>
     * @param feedbackVo {@link FeedbackVo}
     * @return {@link Response}
     */
    @ResponseBody
    @PostMapping("/feedback")
    Response feedback(@RequestBody FeedbackVo feedbackVo){
        LogGenerator.generateLog(httpServletRequest, feedbackVo.getUserId(),
                LogConstants.ActionName.CREATE_FEEDBACK, feedbackVo);
        return feedbackService.createFeedback(feedbackVo);
    }

    /**
     * <h2>用户获取评论信息</h2>
     * @param userId 用户Id
     * @return {@link Response}
     */
    @ResponseBody
    @GetMapping("/feedback")
    Response feedback(Long userId){
        LogGenerator.generateLog(httpServletRequest, userId,
                LogConstants.ActionName.GET_FEEDBACK, null);
        return feedbackService.getFeedBack(userId);
    }

    /**
     * <h2>异常演示接口</h2>
     * @return
     * @throws Exception
     */
    @ResponseBody
    @GetMapping("/exception")
    Response exception() throws Exception {
        throw new Exception("Hello World！");
    }

}
