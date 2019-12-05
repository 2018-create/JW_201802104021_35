package FilterTest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter(filterName = "Filter10",urlPatterns = {"/*"})
public  class Filter10 implements javax.servlet.Filter {
    public void destroy(){
    }
    public void  doFilter(ServletRequest req , ServletResponse resp, FilterChain chain)throws ServletException, IOException{
        System.out.println("Filter 10 begins");
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse)resp;
        //若路径符合条件，则首先设置响应对象字符编码格式为utf8
        response.setContentType("text/html;charset=UTF-8");
        System.out.println("设置响应对象字符编码格式为utf8");
        //对调用的方法进行判断，若为post或put则设置请求对象字符编码格式为utf8
        if (request.getMethod().equals("POST")||request.getMethod().equals("PUT")){
            request.setCharacterEncoding("UTF-8");
            System.out.println("设置请求对象字符编码格式为utf8");
            }
        chain.doFilter(req,resp);//执行其他过滤器，如过滤器已执行完毕，则执行原请求
        System.out.println("Filter 10 ends");
    }
    public void init(FilterConfig config)throws ServletException{
    }
}
