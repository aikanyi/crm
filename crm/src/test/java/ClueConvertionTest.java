import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.commons.utils.UUIDUtils;
import com.dandan.crm.workbench.domain.Customer;
import com.dandan.crm.workbench.mapper.CustomerMapper;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ClueConvertionTest {
    @Resource
    CustomerMapper customerMapper;
    @Test
    public void clueConvertionTest(){
        Map<String, Object> map = new HashMap<>();
        map.put("id","5b5189c777b349c1811ac70454eb1bb9");
        map.put("fullname","王昭君");
        map.put("appellation","67165c27076e4c8599f42de57850e39c");
        map.put("owner","40f6cdea0bd34aceb77492a1656d9fb3");
        map.put("company","蛋蛋公司4");
        map.put("job","射手");
        map.put("email","dandan@163.com");
        map.put("phone","011-11111111");
        map.put("website","http://www.baidu.com/");
        map.put("mphone","13555556666");
        map.put("state","7c07db3146794c60bf975749952176df");
        map.put("source","db867ea866bc44678ac20c8a4a8bfefb");
        map.put("createBy","40f6cdea0bd34aceb77492a1656d9fb3");
        map.put("createTime","2021-12-03 16-02-56");
        map.put("editBy","40f6cdea0bd34aceb77492a1656d9fb3");
        map.put("editTime","2021-12-05 10-59-56");
        map.put("description","净化森林，净化污秽，净化心灵，净化自己。");
        map.put("contactSummary","潮水中，沉默着被遗忘的名字，他们隶属于自作多情的泡沫");
        map.put("nextContactTime","2021-12-01");
        map.put("address","无论何时何地，都会遵守约定");
        String clueId = (String) map.get("id");
        String fullname = (String) map.get("fullname");
        String appellation = (String) map.get("appellation");
        String owner = (String) map.get("owner");
        String company = (String) map.get("company");
        String job = (String) map.get("job");
        String email = (String) map.get("email");
        String phone = (String) map.get("phone");
        String website = (String) map.get("website");
        String mphone = (String) map.get("mphone");
        String state = (String) map.get("state");
        String source = (String) map.get("source");
        String createBy = (String) map.get("createBy");
        String createTime = (String) map.get("createTime");
        String editBy = (String) map.get("editBy");
        String editTime = (String) map.get("editTime");
        String description = (String) map.get("description");
        String contactSummary = (String) map.get("contactSummary");
        String nextContactTime = (String) map.get("nextContactTime");
        String address = (String) map.get("address");

        Customer customer = new Customer();
        customer.setId(UUIDUtils.getUUID());
        customer.setOwner(owner);
        customer.setName(company);
        customer.setPhone(phone);
        customer.setCreateBy(clueId);
        customer.setCreateTime(DateFormatUtil.getDateFormatTime(new Date()));
        customer.setContactSummary(contactSummary);
        customer.setNextContactTime(nextContactTime);
        customer.setDescription(description);
        customer.setAddress(address);

        int customerRet = customerMapper.saveCustomerInformationForClueCompanyInfromation(customer);
        System.out.println(customerRet);
    }
}
