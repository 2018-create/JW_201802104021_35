package controller;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import domain.School;
import service.SchoolService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;
//http://49.235.219.168:8080/bysj-25/myschool/school
@WebServlet("/school.ctl")
public class SchoolController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //POST 49.235.219.168:8080/bysj/school.ctl
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String school_json = JSONUtil.getJSON(request);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        System.out.println(schoolToAdd);

        try {
            //增加加School对象
            SchoolService.getInstance().add(schoolToAdd);
            //加入数据信息
            resp.put("MSG", "添加成功");
        }catch (SQLException e){
            e.printStackTrace();
            resp.put("MSG", "数据库操作异常");
        }catch (Exception e){
            e.printStackTrace();
            resp.put("MSG", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }
    //DELETE 49.235.219.168:8080/bysj/school.ctl
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //到数据库表中删除
            SchoolService.getInstance().delete(id);
            //加入数据信息
            resp.put("MSG", "删除成功");
        }catch (SQLException e){
            e.printStackTrace();
            resp.put("MSG", "数据库操作异常");
        }catch (Exception e){
            e.printStackTrace();
            resp.put("MSG", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }
    //PUT 49.235.219.168:8080/bysj/school.ctl
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String school_json = JSONUtil.getJSON(request);
        //将JSON字串解析为School对象
        School schoolToAdd = JSON.parseObject(school_json, School.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加加School对象
            SchoolService.getInstance().update(schoolToAdd);
            //加入数据信息
            resp.put("MSG", "修改成功");
        }catch (SQLException e){
            e.printStackTrace();
            resp.put("MSG", "数据库操作异常");
        }catch (Exception e){
            e.printStackTrace();
            resp.put("MSG", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }
    //GET 49.235.219.168:8080/bysj/school.ctl
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有对象，否则响应id指定的对象
            if (id_str == null) {
                responseSchools(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseSchool(id, response);
            }
        }catch (SQLException e){
            message.put("message", "数据库操作异常");
            //响应message到前端
            response.getWriter().println(message);
        }catch(Exception e){
            message.put("message", "网络异常");
            //响应message到前端
            response.getWriter().println(message);
        }
    }
    //响应一个对象
    private void responseSchool(int id, HttpServletResponse response)
            throws ServletException, IOException, SQLException, ClassNotFoundException {
            //根据id查找
            School school = SchoolService.getInstance().find(id);
            String school_json = JSON.toJSONString(school);
            //控制台打印结果
            System.out.println(school_json);
            //浏览器展示结果
            response.getWriter().println(school_json);
    }
    //响应所有对象
    private void responseSchools(HttpServletResponse response)
            throws ServletException, IOException,SQLException {
        //获得所有
        Collection<School> schools = SchoolService.getInstance().findAll();
        String schools_json = JSON.toJSONString(schools);
        //控制台打印结果
        System.out.println(schools_json);
        //浏览器展示结果
        response.getWriter().println(schools_json);
    }
}
