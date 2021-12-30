package com.dandan.crm.workbench.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class TransactionController {
    @RequestMapping(value = "/workbench/transaction/index.do")
    public String index(){
        return "workbench/transaction/index";
    }
}
