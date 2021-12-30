package com.dandan.crm.workbench.service.impl;

import com.dandan.crm.workbench.domain.Activity;
import com.dandan.crm.workbench.mapper.ActivityMapper;
import com.dandan.crm.workbench.mapper.ActivityRemarkMapper;
import com.dandan.crm.workbench.service.ActivityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service("activityService")
public class ActivityServiceImpl implements ActivityService {
    @Autowired
    private ActivityMapper activityMapper;
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    //保存创建市场
    @Override
    public int saveCreateActivity(Activity ac) {
        return activityMapper.insertActivity(ac);
    }
    //根据条件查询市场活动
    @Override
    public List<Activity> queryActivityByConditionForPage(Map<String, Object> map) {
        return activityMapper.selectActivityByConditionForPage(map);
    }
    //根据条件查询市场活动总条数
    @Override
    public int queryActivityCountByCondition(Map<String, Object> map) {
        return activityMapper.selectActivityCountByCondition(map);
    }
    //根据所选中活动信息的ID进行删除操作
    @Override
    public int deleteActivityByIdForPage(String[] ids) {
        //删除市场活动之前 先删除所有备注信息
        activityRemarkMapper.deleteActivityRemarkByactivityIds(ids);
        //再删市场活动信息
        return activityMapper.deleteActivityByIdForPage(ids);
    }
    //根据id查询市场活动信息
    @Override
    public Activity selectActivityById(String id) {
        return activityMapper.selectActivityById(id);
    }
    //根据id更新市场活动信息
    @Override
    public int updateActivityById(Activity activity) {
        return activityMapper.updateActivityById(activity);
    }
    //查询所有的市场活动信息
    @Override
    public List<Activity> selectAllActivitys() {
        return activityMapper.selectAllActivitys();
    }
    //根据所选中活动信息的ID进行查询操作
    @Override
    public List<Activity> selectActivityByIds(String[] ids){
        return activityMapper.selectActivityByIds(ids);
    }
    //批量保存市场活动信息
    @Override
    public int saveCreateActivityByList(List<Activity> list) {
        return activityMapper.insertActivityByList(list);
    }
    //根据id查询市场活动明细信息
    @Override
    public Activity queryActivityForDetailById(String id) {
        return activityMapper.selectActivityForDetailById(id);
    }
    //根据线索id查询对应线索下所有的市场活动信息
    @Override
    public List<Activity> queryActivityForDetailByClueId(String clueId) {
        return activityMapper.selectActivityForDetailByClueId(clueId);
    }

    @Override
    public List<Activity> queryActivityForDetailByActivityNameClueId(Map<String, Object> map) {
        return activityMapper.selectActivityForDetailByActivityNameClueId(map);
    }

    @Override
    public List<Activity> queryActivityForDetailByIds(String[] ids) {
        return activityMapper.selectActivityForDetailByIds(ids);
    }
}
