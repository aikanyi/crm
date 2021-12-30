package com.dandan.crm.workbench.web.controller;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.commons.domain.ReturnObject;
import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.commons.utils.UUIDUtils;
import com.dandan.crm.settings.domain.DicValue;
import com.dandan.crm.settings.domain.User;
import com.dandan.crm.settings.mapper.DicValueMapper;
import com.dandan.crm.settings.service.DicValueService;
import com.dandan.crm.settings.service.UserService;
import com.dandan.crm.workbench.domain.Activity;
import com.dandan.crm.workbench.domain.Clue;
import com.dandan.crm.workbench.domain.ClueActivityRelation;
import com.dandan.crm.workbench.domain.ClueRemark;
import com.dandan.crm.workbench.service.ActivityService;
import com.dandan.crm.workbench.service.ClueActivityRelationService;
import com.dandan.crm.workbench.service.ClueRemarkService;
import com.dandan.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class ClueController {
    @Autowired
    private ClueService clueService;
    @Autowired
    private UserService userService;
    @Autowired
    private DicValueService dicValueService;
    @Autowired
    private ClueRemarkService clueRemarkService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ClueActivityRelationService clueActivityRelationService;
    /**
     * 显示线索主页面
     * @return
     */
    @RequestMapping(value = "/workbench/clue/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        List<DicValue> appellationList = dicValueService.queryDicValueByTypeCode("appellation");
        List<DicValue> clueStateList = dicValueService.queryDicValueByTypeCode("clueState");
        List<DicValue> sourceList = dicValueService.queryDicValueByTypeCode("source");
        request.setAttribute("users",users);
        request.setAttribute("appellationList",appellationList);
        request.setAttribute("clueStateList",clueStateList);
        request.setAttribute("sourceList",sourceList);
        return "workbench/clue/index";
    }

    /**
     * 保存创建的线索
     * @param clue
     * @return
     */
    @RequestMapping(value = "/workbench/clue/saveCreateClue.do")
    @ResponseBody
    public Object saveCreateClue(Clue clue, HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        clue.setCreateBy(user.getId());
        clue.setExist("1");
        clue.setId(UUIDUtils.getUUID());
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueService.saveCreateClue(clue);
            if (ret > 0) {
                //保存成功
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

    /**
     * 根据条件查询所有符合的线索记录
     * @param clue
     * @param pageNo
     * @param pageSize
     * @return
     */
    @RequestMapping(value = "/workbench/clue/queryClueByConditionForPage.do")
    @ResponseBody
    public Map<String,Object> queryClueByConditionForPage(Clue clue,int pageNo, int pageSize){
        //获取参数,封装参数
        Map<String, Object> map = new HashMap<>();
        map.put("fullname",clue.getFullname());
        map.put("company",clue.getCompany());
        map.put("phone",clue.getPhone());
        map.put("source",clue.getSource());
        map.put("owner",clue.getOwner());
        map.put("mphone",clue.getMphone());
        map.put("state",clue.getState());
        map.put("exist","1");
        map.put("pageSize",pageSize);
        int beginNo = (pageNo - 1) * pageSize;
        map.put("beginNo",beginNo);
        List<Clue> clueList = clueService.queryClueByConditionForPage(map);
        int totalRows = clueService.queryClueCountByConditionForPage(map);
        Map<String, Object> retMap = new HashMap<>();
        retMap.put("clueList", clueList);
        retMap.put("totalRows", totalRows);
        return retMap;
    }

    /**
     * 根据选中的信息id删除记录
     * @param id
     * @return
     */
    @RequestMapping(value = "/workbench/clue/deleteClueById.do")
    @ResponseBody
    public Object deleteClueById(String[] id) {
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueService.deleteClueById(id);
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

    /**
     * 显示修改模态窗口的线索信息
     * @param id
     * @return
     */
    @RequestMapping(value = "/workbench/clue/queryClueById.do")
    @ResponseBody
    public Object queryClueById(String id) {
        return clueService.queryClueById(id);
    }

    /**
     * 保存修改的线索信息
     * @param clue
     * @param session
     * @return
     */
    @RequestMapping(value = "/workbench/clue/updateSaveClueMessage.do")
    @ResponseBody
    public Object updateSaveClueMessage(Clue clue,HttpSession session) {
        ReturnObject returnObject = new ReturnObject();
        //获取参数 封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        clue.setEditBy(user.getId());
        clue.setEditTime(DateFormatUtil.getDateFormatTime(new Date()));
        try {
            int ret = clueService.updateSaveClueMessage(clue);
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

    /**
     * 根据线索id查询对应线索,活动,备注信息,显示细节页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/workbench/clue/queryClueForDetailById.do")
    public String queryClueForDetailById(String id,HttpServletRequest request) {
        Clue clue = clueService.queryClueById(id);
        List<ClueRemark> clueRemarkList = clueRemarkService.queryClueRemarkForDetailByClueId(id);
        List<Activity> activityList = activityService.queryActivityForDetailByClueId(id);
        request.setAttribute("clue",clue);
        request.setAttribute("clueRemarkList",clueRemarkList);
        request.setAttribute("activityList",activityList);
        return "workbench/clue/detail";
    }

    /**
     * 根据市场活动名字查询线索相关联的市场活动
     * @param activityName
     * @param clueId
     * @return
     */
    @RequestMapping(value="/workbench/clue/queryActivityForDetailByActivityNameClueId.do")
    @ResponseBody
    public Object queryActivityForDetailByActivityNameClueId(String activityName,String clueId){
        Map<String,Object> map = new HashMap<>();
        map.put("name", activityName);
        map.put("clueId", clueId);
        List<Activity> activityList = activityService.queryActivityForDetailByActivityNameClueId(map);
        return activityList;
    }

    /**
     * 保存关联的线索市场关系
     * @param activityIds
     * @param clueId
     * @return
     */
    @RequestMapping(value="/workbench/clue/saveClueActivityRelation.do")
    @ResponseBody
    public Object saveClueActivityRelation(String[] activityIds,String clueId) {
        //获取参数，封装参数
        ClueActivityRelation relation=null;
        List<ClueActivityRelation> relationList=new ArrayList<>();
        for(String ai:activityIds){
            relation=new ClueActivityRelation();
            relation.setActivityId(ai);
            relation.setClueId(clueId);
            relation.setId(UUIDUtils.getUUID());
            relationList.add(relation);
        }
        ReturnObject returnObject = new ReturnObject();
        try {
            int ret = clueActivityRelationService.insertClueActivityRelationByList(relationList);
            if (ret > 0) {
                List<Activity> activityList = activityService.selectActivityByIds(activityIds);
                //成功
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
                returnObject.setRetData(activityList);
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
     * 显示转换页面
     * @param id
     * @return
     */
    @RequestMapping(value = "/workbench/clue/toConvert.do")
    public String toConvert(String id,HttpServletRequest request){
        Clue clue = clueService.queryClueById(id);
        request.setAttribute("clue",clue);
        return "workbench/clue/convert";
    }



    @RequestMapping(value = "/workbench/clue/saveConvertClue.do")
    @ResponseBody
    public Object saveConvertClue(@RequestParam Map<String,Object> map, HttpSession session){
        ReturnObject returnObject = new ReturnObject();
        try {
            clueService.saveClueConversionForCustomerAndContacts(map, session);
                returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
        } catch (Exception e) {
            e.printStackTrace();
            returnObject.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            returnObject.setMessage("系统忙,请稍后重试...");
        }
        return returnObject;
    }
}
