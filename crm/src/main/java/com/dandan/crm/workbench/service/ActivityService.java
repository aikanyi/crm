package com.dandan.crm.workbench.service;

import com.dandan.crm.workbench.domain.Activity;

import java.util.List;
import java.util.Map;

public interface ActivityService {
    /**
     * 保存创建市场
     * @param activity
     * @return
     */
    int saveCreateActivity(Activity activity);
    /**
     * 根据条件查询市场活动
     * @param map
     * @return
     */
    List<Activity> queryActivityByConditionForPage(Map<String, Object> map);
    /**
     * 根据条件查询市场活动总条数
     * @param map
     * @return
     */
    int queryActivityCountByCondition(Map<String, Object> map);
    /**
     * 根据所选中活动信息的ID进行删除操作
     * @param ids
     * @return
     */
    int deleteActivityByIdForPage(String[] ids);
    /**
     * 根据id查询市场活动信息
     * @param id
     * @return
     */
    Activity selectActivityById(String id);
    /**
     * 根据id更新市场活动信息
     * @param activity
     * @return
     */
    int updateActivityById(Activity activity);
    /**
     * 查询所有的市场活动信息
     * @return
     */
    List<Activity> selectAllActivitys();
    /**
     * 根据所选中活动信息的ID进行查询操作
     * @param ids
     * @return
     */
    List<Activity> selectActivityByIds(String[] ids);
    /**
     * 批量保存市场活动信息
     * @param list
     * @return
     */
    int saveCreateActivityByList(List<Activity> list);
    /**
     * 根据id查询市场活动明细信息
     * @param id
     * @return
     */
    Activity queryActivityForDetailById(String id);

    /**
     * 根据线索id查询对应线索下所有的市场活动信息
     * @param clueId
     * @return
     */
    List<Activity> queryActivityForDetailByClueId(String clueId);

    List<Activity> queryActivityForDetailByActivityNameClueId(Map<String, Object> map);

    List<Activity> queryActivityForDetailByIds(String[] ids);
}
