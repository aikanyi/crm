package com.dandan.crm.workbench.web.controller;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.commons.domain.ReturnObject;
import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.commons.utils.UUIDUtils;
import com.dandan.crm.settings.domain.User;
import com.dandan.crm.workbench.domain.ActivityRemark;
import com.dandan.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;

@Controller
public class ActivityRemarkController {
    @Autowired
    private ActivityRemarkService activityRemarkService;

    /**
     * 保存创建的市场活动备注信息
     * @param remark
     * @param session
     * @return
     */
    @RequestMapping(value = "/workbench/activity/saveCreateActivityRemark.do")
    @ResponseBody
    public Object saveCreateActivityRemark(ActivityRemark remark, HttpSession session) {
        //封装参数
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setId(UUIDUtils.getUUID());
        remark.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        remark.setCreateBy(user.getId());
        remark.setEditFlag("0");
        try {
            int ret = activityRemarkService.saveCreateActivityRemark(remark);
            if (ret > 0) {
                //保存成功
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
                returnObject.setRetData(remark);
            } else {
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;
    }

    /**
     * 根据id删除市场活动备注信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/workbench/activity/deleteActivityRemarkById.do")
    @ResponseBody
    public Object deleteActivityRemarkById(String id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = activityRemarkService.deleteActivityRemarkById(id);
            if (ret > 0) {
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
            } else {
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnObject;
    }
    @RequestMapping(value = "/workbench/activity/saveEditActivityRemark.do")
    @ResponseBody
    public Object saveEditActivityRemark(ActivityRemark remark,HttpSession session) {
        //获取参数,封装参数
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        remark.setEditFlag("1");
        remark.setEditTime(DateFormatUtil.getDateFormatTime(new Date()));
        remark.setEditBy(user.getId());
        try {
            int ret = activityRemarkService.saveEditActivityRemarkById(remark);
            if (ret > 0) {
                //保存成功
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
                returnObject.setRetData(remark);
            } else {
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                returnObject.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;
    }
}
