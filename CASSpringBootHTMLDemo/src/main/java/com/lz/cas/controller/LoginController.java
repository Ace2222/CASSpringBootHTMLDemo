package com.lz.cas.controller;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.lz.cas.util.CookiesUtil;

import org.jasig.cas.client.authentication.AttributePrincipal;
import org.jasig.cas.client.util.AbstractCasFilter;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;

@RestController
@CrossOrigin
public class LoginController {

    @RequestMapping("cas")
    public void cas(HttpServletRequest request,HttpServletResponse response, @RequestParam String url)
            throws IOException {

        // 获取自定义返回值的数据
		AttributePrincipal principal = (AttributePrincipal) request.getUserPrincipal();
        Map<String, Object> map = principal.getAttributes();
        JSONObject jsob = new JSONObject();
        jsob.put("cn", map.get("cn"));//用户名
        jsob.put("displayName", map.get("displayName"));//姓名
        jsob.put("mobile", map.get("mobile"));//手机号
        jsob.put("email", map.get("email"));//电子邮箱
        jsob.put("company", map.get("company"));//公司
        
        CookiesUtil.setCookie(response, "data", URLEncoder.encode(jsob.toString(), "utf-8"), -1);

        response.sendRedirect(url);

    }

    /**
     * 测试不拦截的请求
     * 
     * @param request
     * @param modelMap
     * @return
     */
    @RequestMapping("/main")
    public String main(HttpServletRequest request, ModelMap modelMap) {

        return "main";
    }

    @RequestMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response, HttpSession session)
            throws IOException {
        session.removeAttribute(AbstractCasFilter.CONST_CAS_ASSERTION);
        // service=后面跟的是退出登录后要跳转的页面
        response.sendRedirect("https://test.nomalis.com:8443/cas/logout?service=http://localhost:8080/index.html");
        
    }

}