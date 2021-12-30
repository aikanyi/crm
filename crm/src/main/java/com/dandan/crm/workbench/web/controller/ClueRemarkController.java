package com.dandan.crm.workbench.web.controller;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.commons.domain.ReturnObject;
import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.commons.utils.UUIDUtils;
import com.dandan.crm.settings.domain.User;
import com.dandan.crm.workbench.domain.ClueActivityRelation;
import com.dandan.crm.workbench.domain.ClueRemark;
import com.dandan.crm.workbench.service.ClueActivityRelationService;
import com.dandan.crm.workbench.service.ClueRemarkService;
import com.sun.tools.javac.jvm.ByteCodes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.Date;


@Controller
public class ClueRemarkController {
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    /**
     * 保存创建的线索备注
     * @param clueRemark
     * @param session
     * @return
     */
    @RequestMapping(value = "/workbench/clue/saveCreateClueRemark.do")
    @ResponseBody
    public Object saveCreateClueRemark(ClueRemark clueRemark, HttpSession session) {
        clueRemark.setId(UUIDUtils.getUUID());
        ReturnObject returnObject = new ReturnObject();
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clueRemark.setCreateBy(user.getId());
        clueRemark.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        try {
            int ret = clueRemarkService.saveCreateClueRemark(clueRemark);
            if (ByteCodes.ret > 0) {
                //成功
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
                returnObject.setRetData(clueRemark);
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
     * 解除市场活动和线索的关联
     * @param relation
     * @return
     */
    @RequestMapping(value = "/workbench/clue/saveUnbundActivity.do")
    @ResponseBody
    public Object saveUnbundActivity(ClueActivityRelation relation){
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.deleteClueActivityRelationByClueIdActivityId(relation);
            if (ret > 0) {
                //成功
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
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
