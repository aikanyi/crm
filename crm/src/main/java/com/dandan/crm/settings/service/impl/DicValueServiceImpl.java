package com.dandan.crm.settings.service.impl;

import com.dandan.crm.settings.domain.DicValue;
import com.dandan.crm.settings.mapper.DicValueMapper;
import com.dandan.crm.settings.service.DicValueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Service(value = "dicValueService")
public class DicValueServiceImpl implements DicValueService {
    @Autowired
    private DicValueMapper dicValueMapper;
    @Override
    public List<DicValue> queryDicValueByTypeCode(String typeCode) {
        return dicValueMapper.selectAllDicValueByTypeCode(typeCode);
    }
}
