package com.dandan.crm.workbench.mapper;

import com.dandan.crm.workbench.domain.ContactsRemark;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

public interface ContactsRemarkMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbggenerated Mon Dec 06 19:36:22 CST 2021
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbggenerated Mon Dec 06 19:36:22 CST 2021
     */
    int insert(ContactsRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbggenerated Mon Dec 06 19:36:22 CST 2021
     */
    int insertSelective(ContactsRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbggenerated Mon Dec 06 19:36:22 CST 2021
     */
    ContactsRemark selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbggenerated Mon Dec 06 19:36:22 CST 2021
     */
    int updateByPrimaryKeySelective(ContactsRemark record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts_remark
     *
     * @mbggenerated Mon Dec 06 19:36:22 CST 2021
     */
    int updateByPrimaryKey(ContactsRemark record);

    int saveContactsRemarkByList(List<ContactsRemark> contactsRemarkList);
}