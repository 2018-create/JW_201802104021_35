package controller;

import com.alibaba.fastjson.JSONObject;
import domain.User;
import service.UserService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.sql.SQLException;

@WebServlet("/loginController")
public class LoginController extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        request.setCharacterEncoding("UTF-8");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        //创建json对象message，以便于向前端响应信息
        JSONObject message = new JSONObject();
        try {
            User loggedUser = UserService.getInstance().login(username,password);
            if (loggedUser != null){
                message.put("message","登陆成功");
                HttpSession session =request.getSession();
                //10分钟未操作则session失效
                session.setMaxInactiveInterval(10 * 60);
                session.setAttribute("currentUser",loggedUser);
                response.getWriter().println(message);
                //此处应重定向到索引页（菜单页）
                return;
            }else {
                message.put("message","用户名或密码错误");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message.put("message","数据库操作异常");
        }catch (Exception e) {
            e.printStackTrace();
            message.put("message","网络异常");
        }
        //响应message到前端
        response.getWriter().println(message);
    }
}
