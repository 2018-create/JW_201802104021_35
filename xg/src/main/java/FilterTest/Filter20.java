package FilterTest;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Calendar;

@WebFilter(filterName = "Filter20",urlPatterns = "/*")
public class Filter20 implements javax.servlet.Filter {
    public void destroy(){
    }
    public void  doFilter(ServletRequest req , ServletResponse resp, FilterChain chain)throws ServletException, IOException{
        System.out.println("Filter-date 20 begins");
        HttpServletRequest request = (HttpServletRequest)req;
        chain.doFilter(req,resp);//执行其他过滤器，如过滤器已执行完毕，则执行原请求
        //获得请求路径
        String path=request.getRequestURI();
        //创建Calendar类的对象
        Calendar cal = Calendar.getInstance();
        //根据Calendar对象，创建时间信息的字串
        String time= cal.get(Calendar.YEAR)+"年"+(cal.get(Calendar.MONTH) + 1)+"月"+cal.get(Calendar.DATE)+"日"+cal.get(Calendar.HOUR_OF_DAY)+": "+cal.get(Calendar.MINUTE);
        //打印路径与时间
        System.out.println(path+" @ "+time);
        System.out.println("Filter-date 20 ends");
    }
    public void init(FilterConfig config)throws ServletException{
    }
}
