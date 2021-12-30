package com.dandan.crm.workbench.service.impl;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.commons.domain.ReturnObject;
import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.commons.utils.UUIDUtils;
import com.dandan.crm.settings.domain.User;
import com.dandan.crm.workbench.domain.*;
import com.dandan.crm.workbench.mapper.*;
import com.dandan.crm.workbench.service.ClueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(value = "clueService")
public class ClueServiceImpl implements ClueService {
    @Autowired
    private TranMapper tranMapper;
    @Autowired
    private ClueMapper clueMapper;
    @Autowired
    private CustomerMapper customerMapper;
    @Autowired
    private ContactsMapper contactsMapper;
    @Autowired
    private ClueRemarkMapper clueRemarkMapper;
    @Autowired
    private CustomerRemarkMapper customerRemarkMapper;
    @Autowired
    private ContactsRemarkMapper contactsRemarkMapper;
    @Autowired
    private ClueActivityRelationMapper clueActivityRelationMapper;
    @Autowired
    private ContactsActivityRelationMapper contactsActivityRelationMapper;
    @Autowired
    private TranRemarkMapper tranRemarkMapper;
    @Override
    public int saveCreateClue(Clue clue) {
        return clueMapper.insertCreateClue(clue);
    }

    @Override
    public int queryClueCountByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueCountByConditionForPage(map);
    }

    @Override
    public List<Clue> queryClueByConditionForPage(Map<String, Object> map) {
        return clueMapper.selectClueByConditionForPage(map);
    }

    @Override
    public int deleteClueById(String[] id) {
        return clueMapper.deleteClueById(id);
    }

    @Override
    public Clue queryClueById(String id) {
        return clueMapper.selectClueById(id);
    }

    @Override
    public Clue queryDefaultClueById(String id) {
        return clueMapper.selectDefaultClueById(id);
    }

    @Override
    public int updateSaveClueMessage(Clue clue) {
        return clueMapper.updateClueByMessageForPage(clue);
    }

    @Override
    @Transactional
    public void saveClueConversionForCustomerAndContacts(Map<String,Object> map , HttpSession session) {
        User user = (User) session.getAttribute(Contants.SESSION_USER);
        String id = (String) map.get("clueId");
        String isCreateTran = (String) map.get("isCreateTran");
        String amountOfMoney = (String) map.get("amountOfMoney");
        String tradeName = (String) map.get("tradeName");
        String expectedClosingDate = (String) map.get("expectedClosingDate");
        String stage = (String) map.get("stage");
        String activityId = (String) map.get("activityId");
        Clue clue = clueMapper.selectDefaultClueById(id);
        String fullname = clue.getFullname();
        String appellation = clue.getAppellation();
        String owner = user.getId();
        String company = clue.getCompany();
        String job = clue.getJob();
        String email = clue.getEmail();
        String phone = clue.getPhone();
        String website = clue.getWebsite();
        String mphone = clue.getMphone();
        String source = clue.getSource();
        String description = clue.getDescription();
        String contactSummary = clue.getContactSummary();
        String nextContactTime = clue.getNextContactTime();
        String address = clue.getAddress();
        //把线索中有关公司的信息转换到客户表中,封装一个客户对象，添加到客户表中
        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(owner);
        customer.setName(company);
        customer.setWebsite(website);
        customer.setPhone(phone);
        customer.setCreateBy(user.getId());
        customer.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);

        customerMapper.saveCustomerInformationForClueCompanyInfromation(customer);
        //把线索中有关个人的信息转换到联系人表中,封装一个联系人对象，添加到联系人表中
        Contacts contacts = new Contacts();
        contacts.setId(UUIDUtils.getUUID());
        contacts.setCustomerId(customer.getId());
        contacts.setOwner(owner);
        contacts.setSource(source);
        contacts.setFullname(fullname);
        contacts.setAppellation(appellation);
        contacts.setEmail(email);
        contacts.setMphone(mphone);
        contacts.setJob(job);
        contacts.setCreateBy(user.getId());
        contacts.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        contacts.setDescription(description);
        contacts.setContactSummary(contactSummary);
        contacts.setNextContactTime(nextContactTime);
        contacts.setAddress(address);

        contactsMapper.saveContactsInformationForClueInfromation(contacts);
        //把线索的备注信息转换到客户备注表中一份,查询线索的所有备注remarkList,
        List<ClueRemark> clueRemarkList = clueRemarkMapper.selectDefaultClueRemarkByClueId(id);

        List<CustomerRemark> customerRemarkList = new ArrayList<>();
        CustomerRemark customerRemark = null;
        //把线索的备注信息转换到联系人备注表中一份，
        //遍历remarkList，将每条备注的clueId改成联系人的id，
        List<ContactsRemark> contactsRemarkList = new ArrayList<>();
        ContactsRemark contactsRemark = null;
        //遍历remarkList，将每条备注的clueId改成客户的id，
        for (ClueRemark clueRemark : clueRemarkList) {
            customerRemark = new CustomerRemark();
            customerRemark.setId(UUIDUtils.getUUID());
            customerRemark.setNoteContent(clueRemark.getNoteContent());
            customerRemark.setCreateBy(clueRemark.getCreateBy());
            customerRemark.setCreateTime(clueRemark.getCreateTime());
            customerRemark.setEditBy(clueRemark.getEditBy());
            customerRemark.setEditTime(clueRemark.getEditTime());
            customerRemark.setEditFlag(clueRemark.getEditFlag());
            customerRemark.setCustomerId(customer.getId());
            customerRemarkList.add(customerRemark);

            contactsRemark = new ContactsRemark();
            contactsRemark.setId(UUIDUtils.getUUID());
            contactsRemark.setNoteContent(clueRemark.getNoteContent());
            contactsRemark.setCreateBy(clueRemark.getCreateBy());
            contactsRemark.setCreateTime(clueRemark.getCreateTime());
            contactsRemark.setEditBy(clueRemark.getEditBy());
            contactsRemark.setEditTime(clueRemark.getEditTime());
            contactsRemark.setEditFlag(clueRemark.getEditFlag());
            contactsRemark.setContactsId(contacts.getId());
            contactsRemarkList.add(contactsRemark);
        }
        // 封装成客户备注的List，然后添加到客户的备注表中
        customerRemarkMapper.insertCustomerRemarkByList(customerRemarkList);
        // 封装成联系人备注的List，然后添加到联系人的备注表中
        contactsRemarkMapper.saveContactsRemarkByList(contactsRemarkList);
        //把线索和市场活动的关联关系转换到联系人和市场活动的关联关系表中,
        //查询与该线索相关联的所有关联关系，得到relationList
        List<ClueActivityRelation> clueActivityRelationList = clueActivityRelationMapper.selectAllRelationByClueId(id);
        //遍历relationList，将每条关联关系的clueId改成联系人的id，
        List<ContactsActivityRelation> contactsActivityRelationList = new ArrayList<>();
        ContactsActivityRelation contactsActivityRelation = null;
        for (ClueActivityRelation clueActivityRelation : clueActivityRelationList) {
            contactsActivityRelation = new ContactsActivityRelation();
            contactsActivityRelation.setId(UUIDUtils.getUUID());
            contactsActivityRelation.setActivityId(clueActivityRelation.getActivityId());
            contactsActivityRelation.setContactsId(contacts.getId());
            contactsActivityRelationList.add(contactsActivityRelation);
        }
        // 封装成联系人和市场活动的关联关系的List，然后添加到联系人和市场活动的关联关系表中
        contactsActivityRelationMapper.saveCreateContactsActivityRelationByList(contactsActivityRelationList);
        //如果需要创建交易,还要往交易表中添加一条记录

        if ("true".equals(isCreateTran)) {
            //封装一个交易对象，添加到交易表中
            Tran tran = new Tran();
            tran.setId(UUIDUtils.getUUID());
            tran.setOwner(user.getId());
            tran.setMoney(amountOfMoney);
            tran.setName(tradeName);
            tran.setExpectedDate(expectedClosingDate);
            tran.setCustomerId(customer.getId());
            tran.setStage(stage);
            tran.setActivityId(activityId);
            tran.setContactsId(contacts.getId());
            tran.setCreateBy(user.getId());
            tran.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
            tranMapper.saveTran(tran);
            //如果需要创建交易,还要把线索的备注信息转换到交易备注表中一份
            List<TranRemark> tranRemarkList = new ArrayList<>();
            TranRemark tranRemark = null;

            //遍历remarkList，将每条备注的clueId改成交易的id，
            for (ClueRemark clueRemark : clueRemarkList) {
                tranRemark = new TranRemark();
                tranRemark.setId(UUIDUtils.getUUID());
                tranRemark.setNoteContent(clueRemark.getNoteContent());
                tranRemark.setCreateBy(clueRemark.getCreateBy());
                tranRemark.setCreateTime(clueRemark.getCreateTime());
                tranRemark.setTranId(tran.getId());
                tranRemarkList.add(tranRemark);
            }
            // 封装成交易备注的List，然后添加到交易的备注表中
            tranRemarkMapper.saveTranRemarkByList(tranRemarkList);
        }
        //删除线索的备注
         clueRemarkMapper.deleteClueRemarkByClueId(id);
        //删除线索和市场活动的关联关系
         clueActivityRelationMapper.deleteClueActivityRelationByClueId(id);
        //删除线索
         clueMapper.deleteClue(id);
    }
}
