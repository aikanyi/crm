package com.dandan.crm.settings.web.controller;

import com.dandan.crm.commons.contants.Contants;
import com.dandan.crm.commons.domain.ReturnObject;
import com.dandan.crm.commons.utils.DateFormatUtil;
import com.dandan.crm.settings.domain.User;
import com.dandan.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @RequestMapping(value = "/settings/qx/user/toLogin.do")
    public String toLogin(){
        return "settings/qx/user/login";
    }
    @RequestMapping(value = "/settings/qx/user/login.do")
    @ResponseBody
    public Object login(String loginAct, String loginPwd, String isRemPwd,
                        HttpServletRequest request, HttpSession session, HttpServletResponse response){
        ReturnObject obj = new ReturnObject();
        HashMap<String, Object> map = new HashMap<>();
        map.put("loginAct",loginAct);
        map.put("loginPwd",loginPwd);
        User user = userService.queryUserByActAndPwd(map);
        if (user == null) {
            //登录失败，用户名或密码为空
            obj.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
            obj.setMessage("用户名或密码为空");
        } else {
            if (DateFormatUtil.getDateFormatTime(new Date()).compareTo(user.getExpireTime()) > 0) {
                //登录失败，账号已过期
                obj.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                obj.setMessage("账号已过期");

            } else if ("0".equals(user.getLockState())){
                //登录失败，账号已锁定
                obj.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                obj.setMessage("账号已锁定");
            } else if (!user.getAllowIps().contains(request.getRemoteAddr())) {
                //登录失败，ip受限
                obj.setCode(Contants.AJAX_RETURN_SUCCESS_FAIL);
                obj.setMessage("ip受限");
            } else {
                //登陆成功
                obj.setCode(Contants.AJAX_RETURN_SUCCESS_CODE);
                session.setAttribute(Contants.SESSION_USER,user);
                if ("true".equals(isRemPwd)) {
                    Cookie act = new Cookie("loginAct", user.getLoginAct());
                    act.setMaxAge(Integer.MAX_VALUE);
                    response.addCookie(act);
                    Cookie pwd = new Cookie("loginPwd", user.getLoginPwd());
                    pwd.setMaxAge(Integer.MAX_VALUE);
                    response.addCookie(pwd);
                } else {
                    Cookie act = new Cookie("loginAct", "0");
                    act.setMaxAge(0);
                    response.addCookie(act);
                    Cookie pwd = new Cookie("loginPwd", "0");
                    pwd.setMaxAge(0);
                    response.addCookie(pwd);
                }

            }
        }
        return obj;
    }

    /**
     * 处理安全退出 清空cookie，销毁session
     * @return
     */
    @RequestMapping(value = "/settings/qx/user/logout.do")
    public String logout(HttpSession session,HttpServletResponse response){
        Cookie act = new Cookie("loginAct", "0");
        act.setMaxAge(0);
        response.addCookie(act);
        Cookie pwd = new Cookie("loginPwd", "0");
        pwd.setMaxAge(0);
        response.addCookie(pwd);
        session.invalidate();
        return "redirect:/";
    }
}
