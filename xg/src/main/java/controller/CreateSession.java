package controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/createSession")
public class CreateSession extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //如果当前请求对应服务器内存中的一个session对象，返回该对象
        //如果当前服务器内存中的没有session对象与当前请求对应，则建立一个session对象并返回该对象
        HttpSession session = request.getSession();
        //如果session不活跃时间间隔大于5秒则session失效
        session.setMaxInactiveInterval(5);
        response.getWriter().println("session will last 5 seconds");
    }
}
