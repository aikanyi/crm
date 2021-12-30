package com.dandan.crm.workbench.web.controller;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.commons.domain.ReturnObject;
import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.commons.utils.HSSFUtils;
import com.dandan.crm.commons.utils.UUIDUtils;
import com.dandan.crm.settings.domain.User;
import com.dandan.crm.settings.service.UserService;
import com.dandan.crm.workbench.domain.Activity;
import com.dandan.crm.workbench.domain.ActivityRemark;
import com.dandan.crm.workbench.service.ActivityRemarkService;
import com.dandan.crm.workbench.service.ActivityService;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.*;

@Controller
public class ActivityController {
    @Autowired
    private UserService userService;
    @Autowired
    private ActivityService activityService;
    @Autowired
    private ActivityRemarkService activityRemarkService;
    //显示市场活动首页
    @RequestMapping(value = "/workbench/activity/index.do")
    public String index(HttpServletRequest request){
        List<User> users = userService.queryAllUsers();
        request.setAttribute("users",users);
        return "workbench/activity/index";
    }
    //处理保存市场活动
    @RequestMapping(value = "/workbench/activity/saveCreateActivity.do")
    @ResponseBody
    public Object saveCreateActivity(Activity activity,HttpSession session){
        ReturnObject object = new ReturnObject();
        User s = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setId(UUIDUtils.getUUID());
        activity.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        activity.setCreateBy(s.getId());
        try {
            int i = activityService.saveCreateActivity(activity);
            if (i > 0) {
                object.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
            } else {
                object.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                object.setMessage("后台正忙...请稍后再试");
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            object.setMessage("后台正忙...请稍后再试");
        }
        return object;
    }
    //处理查询市场活动
    @RequestMapping(value = "/workbench/activity/queryActivityByConditionForPage.do")
    @ResponseBody
    public Object queryActivityByConditionForPage(String name, String owner, String startDate, String endDate,
                                                              int pageNo, int pageSize){
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", name);
        paramMap.put("owner", owner);
        paramMap.put("startDate", startDate);
        paramMap.put("endDate", endDate);
        int bigenNo = (pageNo - 1) * pageSize;
        paramMap.put("biginNo", bigenNo);
        paramMap.put("pageSize", pageSize);
        List<Activity> activityList = activityService.queryActivityByConditionForPage(paramMap);
        int totalRows = activityService.queryActivityCountByCondition(paramMap);
        HashMap<String, Object> map = new HashMap<>();
        map.put("activityList", activityList);
        map.put("totalRows", totalRows);
        return map;
    }
    //根据所选中活动信息的ID进行删除操作
    @RequestMapping(value = "/workbench/activity/deleteActivityByIdForPage.do")
    @ResponseBody
    public Object deleteActivityByIdForPage(String[] id){
        ReturnObject object = new ReturnObject();
        try {
            int i = activityService.deleteActivityByIdForPage(id);
            if (i == 0) {
                object.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                object.setMessage("系统正忙...请稍后");
            } else {
                object.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
            }
        } catch (Exception e) {
            e.printStackTrace();
            object.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            object.setMessage("系统正忙...请稍后");
        }
        return object;
    }
    //显示修改模态窗口信息
    @RequestMapping(value = "/workbench/activity/queryActivityById.do")
    @ResponseBody
    public Object queryActivityById(String id){
        return activityService.selectActivityById(id);
    }
    //根据id更新市场活动信息
    @RequestMapping(value = "/workbench/activity/updateActivityById.do")
    @ResponseBody
    public Object updateActivityById(Activity activity,HttpSession session){
        //获取参数，封装参数
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        activity.setEditTime(DateFormatUtil.getDateFormatTime(new Date()));
        activity.setEditBy(user.getId());
        ReturnObject obj = new ReturnObject();
        try {
            int i = activityService.updateActivityById(activity);
            if (i > 0) {
                obj.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
            } else {
                obj.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                obj.setMessage("系统忙,请稍后重试...");
            }
        } catch (Exception e) {
            e.printStackTrace();
            obj.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            obj.setMessage("系统忙,请稍后重试...");
        }
       return obj;
    }
    //批量导出市场活动列表
    @RequestMapping(value = "/workbench/activity/queryAllActivitys.do")
    public void queryAllActivitys(HttpServletResponse response)throws Exception{
        List<Activity> activities = activityService.selectAllActivitys();
        //创建一个HSSFworkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个页对象
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //创建一个行对象
        HSSFRow row = sheet.createRow(0);
        //创建一个列对象
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("金额");
        cell=row.createCell(6);
        cell.setCellValue("备注");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        cell=row.createCell(11);
        cell.setCellValue("exist");
        //生成数据行
        Activity activity = null;
        for (int i = 0; i < activities.size(); i++) {
            activity = activities.get(i);
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell=row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell=row.createCell(2);
            cell.setCellValue(activity.getName());
            cell=row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell=row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell=row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell=row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell=row.createCell(7);
            cell.setCellValue(activity.getCreateBy());
            cell=row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell=row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell=row.createCell(10);
            cell.setCellValue(activity.getEditBy());
            cell=row.createCell(11);
            cell.setCellValue(activity.getExist());
        }
        //在指定服务器生成Excel文件
            /*OutputStream os = new FileOutputStream("E:\\BaiduNetdiskDownload\\java完整学习\\serviceDownload\\activityList.xls");
            wb.write(os);
            os.close();
            wb.close();*/
        //把生成的文件返回
        //设置响应信息
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置响应相应头信息，使浏览器接受到响应信息后，直接激活下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        //获取输出流
        OutputStream out = response.getOutputStream();
            /*InputStream is = new FileInputStream("E:\\BaiduNetdiskDownload\\java完整学习\\serviceDownload\\activityList.xls");
            byte[] buff = new byte[256];
            int len = 0;
            while ((len = is.read(buff)) != -1) {
                out.write(buff,0,len);
            }
            is.close();*/
        wb.write(out);
        out.flush();
        wb.close();
    }
    //选择导出市场活动列表
    @RequestMapping(value = "/workbench/activity/queryActivityByIds.do")
    public void queryActivityById(HttpServletResponse response,String[] id)throws Exception{
        List<Activity> activities = activityService.selectActivityByIds(id);
        //创建一个HSSFworkbook对象
        HSSFWorkbook wb = new HSSFWorkbook();
        //创建一个页对象
        HSSFSheet sheet = wb.createSheet("市场活动列表");
        //创建一个行对象
        HSSFRow row = sheet.createRow(0);
        //创建一个列对象
        HSSFCell cell = row.createCell(0);
        cell.setCellValue("ID");
        cell=row.createCell(1);
        cell.setCellValue("所有者");
        cell=row.createCell(2);
        cell.setCellValue("名称");
        cell=row.createCell(3);
        cell.setCellValue("开始日期");
        cell=row.createCell(4);
        cell.setCellValue("结束日期");
        cell=row.createCell(5);
        cell.setCellValue("金额");
        cell=row.createCell(6);
        cell.setCellValue("备注");
        cell=row.createCell(7);
        cell.setCellValue("创建时间");
        cell=row.createCell(8);
        cell.setCellValue("创建者");
        cell=row.createCell(9);
        cell.setCellValue("修改时间");
        cell=row.createCell(10);
        cell.setCellValue("修改者");
        cell=row.createCell(11);
        cell.setCellValue("exist");
        //生成数据行
        Activity activity = null;
        for (int i = 0; i < activities.size(); i++) {
            activity = activities.get(i);
            row = sheet.createRow(i+1);
            cell = row.createCell(0);
            cell.setCellValue(activity.getId());
            cell=row.createCell(1);
            cell.setCellValue(activity.getOwner());
            cell=row.createCell(2);
            cell.setCellValue(activity.getName());
            cell=row.createCell(3);
            cell.setCellValue(activity.getStartDate());
            cell=row.createCell(4);
            cell.setCellValue(activity.getEndDate());
            cell=row.createCell(5);
            cell.setCellValue(activity.getCost());
            cell=row.createCell(6);
            cell.setCellValue(activity.getDescription());
            cell=row.createCell(7);
            cell.setCellValue(activity.getCreateBy());
            cell=row.createCell(8);
            cell.setCellValue(activity.getCreateBy());
            cell=row.createCell(9);
            cell.setCellValue(activity.getEditTime());
            cell=row.createCell(10);
            cell.setCellValue(activity.getEditBy());
            cell=row.createCell(11);
            cell.setCellValue(activity.getExist());
        }
        //设置响应信息
        response.setContentType("application/octet-stream;charset=utf-8");
        //设置响应相应头信息，使浏览器接受到响应信息后，直接激活下载窗口，即使能打开也不打开
        response.addHeader("Content-Disposition","attachment;filename=activityList.xls");
        //获取输出流
        OutputStream out = response.getOutputStream();
        wb.write(out);
        out.flush();
        wb.close();
    }
    //文件上传 springMVC接受到请求之后会调用文件上传解析器类，从请求体中获取文件的所有信息，都封装到MultipartFile对象中
    @RequestMapping(value = "/workbench/activity/fileUpload.do")
    @ResponseBody
    public Object fileUpload(MultipartFile activityFile , HttpSession session){
        ReturnObject object = new ReturnObject();
        try {
            //String filename = activityFile.getOriginalFilename();
            //File file1 = new File("E:\\BaiduNetdiskDownload\\java完整学习\\serviceDownload",filename);
            //接受文件，在服务器指定目录生成一个同样的文件
            //activityFile.transferTo(file1);
            //根据生成的文件生成HSSFWorkbook对象，封装这个excel文件所有信息
            //InputStream is = new FileInputStream("E:\\BaiduNetdiskDownload\\java完整学习\\serviceDownload\\"+filename);
            InputStream is = activityFile.getInputStream();
            HSSFWorkbook wb = new HSSFWorkbook(is);
            //根据wb获取页面对象
            HSSFSheet sheet = wb.getSheetAt(0);
            if (sheet.getLastRowNum() == 0) {
                object.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
                object.setRetData(0);
                return object;
            }
            HSSFRow row = null;
            HSSFCell cell = null;
            Activity activity = null;
            List<Activity> activityList = new ArrayList<>();
            User user = (User) session.getAttribute(Contants.SESSION_USER);
            //遍历页面的行数
            for (int i = 1; i <=sheet.getLastRowNum(); i++) {//getLastRowNum();最后一行的编号
                row = sheet.getRow(i);//i 行的编号 0 表示第一行
                activity = new Activity();
                activity.setId(UUIDUtils.getUUID());
                activity.setOwner(user.getId());
                activity.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
                activity.setCreateBy(user.getId());
                for (int j = 0; j < row.getLastCellNum(); j++) {//getLastCellNum();最后一列的编号+1
                    cell = row.getCell(j);
                    String value = HSSFUtils.getCellValueForString(cell);
                    switch (j) {
                        case 0:activity.setName(value);break;
                        case 1:activity.setStartDate(value);break;
                        case 2:activity.setEndDate(value);break;
                        case 3:activity.setCost(value);break;
                        case 4:activity.setDescription(value);break;
                        default:activity.setExist(value);
                    }
                }
                activityList.add(activity);
            }
            int result = activityService.saveCreateActivityByList(activityList);
            if (result > 0) {
                object.setRetData(result);
                object.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
            } else {
                object.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                object.setMessage("系统忙，请稍后重试");
            }
        } catch (IOException e) {
            e.printStackTrace();
            object.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            object.setMessage("系统忙，请稍后重试");
        }
        return object;
    }
    //显示市场活动细节信息模态窗口
    @RequestMapping(value = "/workbench/activity/activityDetail.do")
    public String activityDetail(String id , HttpServletRequest request){
        Activity activity = activityService.queryActivityForDetailById(id);
        List<ActivityRemark> remarkList = activityRemarkService.queryActivityRemarkForDetailByActivityId(id);
        request.setAttribute("activity",activity);
        request.setAttribute("remarkList",remarkList);
        return "workbench/activity/detail";
    }
}
