package com.dandan.crm.workbench.service;


import com.dandan.crm.workbench.domain.Clue;

import javax.servlet.http.HttpSession;
import java.util.List;
import java.util.Map;

public interface ClueService {
    int saveCreateClue(Clue clue);

    int queryClueCountByConditionForPage(Map<String, Object> map);

    List<Clue> queryClueByConditionForPage(Map<String, Object> map);

    int deleteClueById(String[] id);

    Clue queryClueById(String id);

    Clue queryDefaultClueById(String id);

    int updateSaveClueMessage(Clue clue);

    void saveClueConversionForCustomerAndContacts(Map<String,Object> map , HttpSession session);
}
