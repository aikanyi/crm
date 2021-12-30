package com.dandan.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class ContactsController {
    @RequestMapping(value = "/workbench/contacts/index.do")
    public String index(){
        return "workbench/contacts/index";
    }
}
