package com.dandan.crm.workbench.mapper;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.workbench.domain.Contacts;
import com.dandan.crm.workbench.domain.Customer;

public interface ContactsMapper {
    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbggenerated Mon Dec 06 19:35:48 CST 2021
     */
    int deleteByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbggenerated Mon Dec 06 19:35:48 CST 2021
     */
    int insert(Contacts record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbggenerated Mon Dec 06 19:35:48 CST 2021
     */
    int insertSelective(Contacts record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbggenerated Mon Dec 06 19:35:48 CST 2021
     */
    Contacts selectByPrimaryKey(String id);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbggenerated Mon Dec 06 19:35:48 CST 2021
     */
    int updateByPrimaryKeySelective(Contacts record);

    /**
     * This method was generated by MyBatis Generator.
     * This method corresponds to the database table tbl_contacts
     *
     * @mbggenerated Mon Dec 06 19:35:48 CST 2021
     */
    int updateByPrimaryKey(Contacts record);

    int saveContactsInformationForClueInfromation(Contacts contacts);
}