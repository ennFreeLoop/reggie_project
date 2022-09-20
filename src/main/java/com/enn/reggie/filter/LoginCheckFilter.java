package com.enn.reggie.filter;

import com.alibaba.fastjson.JSON;
import com.enn.reggie.common.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@WebFilter(filterName = "LoginCheckFilter",urlPatterns = "/*")
public class LoginCheckFilter implements Filter {
    public static final AntPathMatcher antPathMatcher=new AntPathMatcher();
    @Override
    public void doFilter(ServletRequest var1, ServletResponse var2, FilterChain var3) throws ServletException, IOException {
        HttpServletRequest request=(HttpServletRequest) var1;
        HttpServletResponse response=(HttpServletResponse) var2;

        String reqUrl=request.getRequestURI();
        log.info("访问的地址是：{}",reqUrl);

        /**1.定义不需要处理的路径*/
        String[] urls=new String[]{
                "/employee/login",
                "employee/logout",
                "/backend/**",
                "/front/**"
        };

        /**2.判断本次请求是否需要处理*/
        boolean check=UrlCheck(urls,reqUrl);

        //3.如果可以匹配上
        if(check){
            log.info("请求{}不需要拦截",reqUrl);
            var3.doFilter(request,response);
            return;
        }

        //4.如果是登录状态
        if (request.getSession().getAttribute("employee")!=null) {
            log.info("用户已经登陆了，登录用户为{}",request.getSession().getAttribute("employye"));
            var3.doFilter(request,response);
            return;
        }

        //5.最后应该就是要被拦截的，因为前端有拦截方法，所以只需要将输出流写到响应中就Ok了
        log.info("最后需要被拦截的~~~~~~~{}",request.getRequestURI());
        response.getWriter().write(JSON.toJSONString(R.error("NOTLOGIN")));
        return;


    }
    public boolean UrlCheck(String[] urls,String reqUrl){
        for(String url:urls){
            boolean res=antPathMatcher.match(url,reqUrl);
            //只要该链接能和其中任意条匹配就放行
            if(res){
                return true;
            }
        }
        return false;
    }
}
