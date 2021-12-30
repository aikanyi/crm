package com.dandan.crm.workbench.service;

import com.dandan.crm.workbench.domain.ClueRemark;

import java.util.List;

public interface ClueRemarkService {
    List<ClueRemark> queryClueRemarkForDetailByClueId(String clueId);
    int saveCreateClueRemark(ClueRemark clueRemark);
}
