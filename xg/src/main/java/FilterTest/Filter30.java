
package FilterTest;

import com.alibaba.fastjson.JSONObject;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebFilter(filterName = "Filter30",urlPatterns = {"/*"})
public  class Filter30 implements Filter {
    public void destroy(){
    }
    public void  doFilter(ServletRequest req , ServletResponse resp, FilterChain chain)throws ServletException, IOException {
        System.out.println("Filter 30 begins");
        HttpServletRequest request = (HttpServletRequest)req;
        HttpServletResponse response=(HttpServletResponse)resp;
        //获得请求路径
        String path= request.getRequestURI();
        //创建JSON对象
        JSONObject message = new JSONObject();
        HttpSession session = request.getSession(false);
       if (path.contains("Session") ||path.contains("login") ||path.contains("show")) {
            System.out.println("正常执行");
        }else{
           if (session == null || session.getAttribute("currentUser") ==null){
               message.put("message","请登录或重新登录");
               //响应message到前端
               response.getWriter().println(message);
                return;
            }
        }
        chain.doFilter(req,resp);//执行其他过滤器，如过滤器已执行完毕，则执行原请求
        System.out.println("Filter 30 ends");
    }
    public void init(FilterConfig config)throws ServletException{
    }
}


