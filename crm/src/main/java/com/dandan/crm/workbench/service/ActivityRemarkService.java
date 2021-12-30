package com.dandan.crm.workbench.service;

import com.dandan.crm.workbench.domain.ActivityRemark;

import java.util.List;

public interface ActivityRemarkService {
    /**
     * 根据市场活动id查询活动备注信息
     * @param activityId
     * @return
     */
    List<ActivityRemark> queryActivityRemarkForDetailByActivityId(String activityId);

    /**
     * 保存市场活动备注信息
     * @param remark
     * @return
     */
    int saveCreateActivityRemark(ActivityRemark remark);

    /**
     * 根据id删除市场活动备注信息
     * @param id
     * @return
     */
    int deleteActivityRemarkById(String id);

    /**
     *
     * @param remark
     * @return
     */
    int saveEditActivityRemarkById(ActivityRemark remark);

    int deleteActivityRemarkByactivityIds(String[] activityIds);
}
