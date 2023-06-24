package com.example.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.example.reggie.common.BaseContext;
import com.example.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 过滤器：用于对前端发送的请求进行过滤
 * 登录检查过滤器
 *用户只有成功登录之后才能访问网站内容
 * @author 李朋逊
 * @date 2023/06/17
 */
//指定过滤器名称和过滤的路径
@WebFilter(filterName = "LoginCheckFilter" , urlPatterns = "/*")
@Slf4j
public class LoginCheckFilter implements Filter {


    /*
    1.获取本次请求的URI
    2.判断本次请求是否需要处理
    3.如果不需要处理，直接放行
    4.判断登录状态，如果已登录，直接放行
    5.如果未登录，返回未登录结果
     */
    //路由匹配器，支持通配符匹配
    public static final AntPathMatcher PATH_MATCHER = new AntPathMatcher();


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        HttpServletResponse response = (HttpServletResponse) servletResponse;
        String URI = request.getRequestURI();
        log.info("拦截到请求：{}",URI);
        //定义不需要处理的请求路径
        String[] urls = new String[]{
          "/employee/login", "/employee/logout","/backend/**","/front/**"
        };
        //判断本次请求是否需要处理
        //如果不需要处理，直接放行
        if(check(urls,URI)){
            log.info("本次请求{}不需要处理",URI);
            filterChain.doFilter(request,response);
            return;
        }
        //判断登录状态，如果已登录，直接放行
        Object employee = request.getSession().getAttribute("employee");
        if(employee != null){
            log.info("用户已登录，用户id为{}",request.getSession().getAttribute("employee"));
            Long empId = (Long)  request.getSession().getAttribute("employee");
            BaseContext.setCurrentId(empId);
            filterChain.doFilter(request,response);
            return;
        }
        log.info("用户未登录");
        //如果未登录，返回未登录结果,通过输出流方式向客户端页面响应数据
        response.getWriter().write(JSON.toJSONString((R.error("NOTLOGIN"))));
        return ;
    }

    /*
    路径匹配，检查本次请求是否需要放行
     */
    public Boolean check(String[] urls,String url){
        for (String s : urls) {
            boolean match = PATH_MATCHER.match(s,url);
            if(match == true){
                return true;
            }
        }
        return false;
    }
}
