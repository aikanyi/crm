package com.dandan.crm.workbench.service.impl;

import com.dandan.crm.workbench.domain.ActivityRemark;
import com.dandan.crm.workbench.mapper.ActivityRemarkMapper;
import com.dandan.crm.workbench.service.ActivityRemarkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service(value = "activityRemarkService")
public class ActivityRemarkServiceImpl implements ActivityRemarkService {
    @Autowired
    private ActivityRemarkMapper activityRemarkMapper;
    @Override
    public List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId) {
        return activityRemarkMapper.queryActivityRemarkForDetailByActivityId(activityId);
    }

    @Override
    public int saveCreateActivityRemark(ActivityRemark remark) {
        return activityRemarkMapper.insertActivityRemark(remark);
    }

    @Override
    public int deleteActivityRemarkById(String id) {
        return activityRemarkMapper.deleteActivityRemarkById(id);
    }

    @Override
    public int saveEditActivityRemarkById(ActivityRemark remark) {
        return activityRemarkMapper.updateActivityRemark(remark);
    }

    @Override
    public int deleteActivityRemarkByactivityIds(String[] activityIds) {
        return activityRemarkMapper.deleteActivityRemarkByactivityIds(activityIds);
    }

}
