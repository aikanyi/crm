package com.dandan.crm.workbench.service;

import com.dandan.crm.workbench.domain.ClueActivityRelation;

import java.util.List;

public interface ClueActivityRelationService {

    int insertClueActivityRelationByList(List<ClueActivityRelation> clueActivityRelationList);

    int deleteClueActivityRelationByClueIdActivityId(ClueActivityRelation relation);
}
