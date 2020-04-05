package com.czw.gmall.passport.interceptor;

import com.alibaba.fastjson.JSON;
import com.czw.gmall.passport.annotation.LoginRequired;
import com.czw.gmall.passport.util.CookieUtil;
import com.czw.gmall.utils.HttpclientUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Component
public class AuthInterceptor extends HandlerInterceptorAdapter {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod)handler;
        LoginRequired loginRequired = handlerMethod.getMethodAnnotation(LoginRequired.class);//得到访问方法上的注解
        if(loginRequired == null){
            //该方法上没有LoginRequired注解 直接放行
            return true;
        }

        String token = "";
        String oldToken = CookieUtil.getCookieValue(request, "oldToken", true);//从cookie 中取出旧的token
        if(StringUtils.isNotBlank(oldToken)){
            token=oldToken;
        }

        String newToken = request.getParameter("token");
        if(StringUtils.isNotBlank(newToken)){
            token=newToken;
        }

        //调用认证中心(passport)验证token
        String success = "fail";
        Map<String,String> successMap = new HashMap<>();
        if(StringUtils.isNotBlank(token)){
            String ip = request.getHeader("x-forwarded-for");// 通过nginx转发的客户端ip
            if(StringUtils.isBlank(ip)){
                ip = request.getRemoteAddr();// 从request中获取ip
                if(StringUtils.isBlank(ip)){
                    ip = "127.0.0.1";
                }
            }

            String successJson = HttpclientUtil.doGet("http://localhost:8086/verify?token="+token+"&currentIp="+ip);
            successMap = JSON.parseObject(successJson, Map.class);

            success = successMap.get("status");
        }

        // 是否必须登录
        boolean loginSuccess = loginRequired.loginSuccess();// 获得该请求是否必登录成功
        if(loginSuccess){
            if(!success.equals("success")){//验证不成功
                //踢回认证中心
                StringBuffer requestURL = request.getRequestURL();
                String queryString ="?"+ request.getQueryString();
                if(StringUtils.isNotBlank(queryString)){
                    requestURL.append(queryString);
                }
                response.sendRedirect("http://localhost:8086/index?returnUrl="+requestURL);
                return false;
            }
            // 需要将token携带的用户信息写入
            request.setAttribute("memberId", successMap.get("memberId"));
            request.setAttribute("nickname", successMap.get("nickname"));
            //验证通过，覆盖cookie中的token
            if(StringUtils.isNotBlank(token)){
                CookieUtil.setCookie(request,response,"oldToken",token,60*60,true);
            }
        }else {
            //没有登录也能使用  但必须验证 否则会影响购物车的分支 购物车分登录和没有登录两种情况
            if (success.equals("success")) {
                // 需要将token携带的用户信息写入
                request.setAttribute("memberId", successMap.get("memberId"));
                request.setAttribute("nickname", successMap.get("nickname"));
                //验证通过，覆盖cookie中的token
                if(StringUtils.isNotBlank(token)){
                    CookieUtil.setCookie(request,response,"oldToken",token,60*60,true);
                }
            }
        }
        return true;
    }
}
