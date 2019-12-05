package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import dao.DegreeDao;
import domain.Degree;
import service.DegreeService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/degree.ctl")
public class DegreeController extends HttpServlet {

    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //POST 49.235.219.168:8080/bysj/degree.ctl
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        System.out.println(degreeToAdd);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加加Degree对象
            DegreeService.getInstance().add(degreeToAdd);
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
    //DELETE 49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //到数据库表中删除对应的学院
            DegreeService.getInstance().delete(id);
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
    //PUT 49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //设置请求字符编码为UTF-8
        //request.setCharacterEncoding("UTF-8");
        String degree_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Degree degreeToAdd = JSON.parseObject(degree_json, Degree.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加加Degree对象
            DegreeService.getInstance().update(degreeToAdd);
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
    //GET 49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //设置响应字符编码为UTF-8
        //response.setContentType("text/html;charset=UTF-8");
        //读取参数id
        String id_str = request.getParameter("id");
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        try {
            //如果id = null, 表示响应所有学位对象，否则响应id指定的学位对象
            if (id_str == null) {
                responseDegrees(response);
            } else {
                int id = Integer.parseInt(id_str);
                responseDegree(id, response);
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

    //响应一个学位对象
    private void responseDegree(int id, HttpServletResponse response)
            throws ServletException, IOException,SQLException {
            //根据id查找学院
            Degree degree = DegreeService.getInstance().find(id);
            String degree_json = JSON.toJSONString(degree);
            //控制台打印结果
            System.out.println(degree_json);
            //浏览器展示结果
            response.getWriter().println(degree_json);
    }
    //响应所有学位对象
    private void responseDegrees(HttpServletResponse response)
            throws ServletException, IOException ,SQLException{
        //获得所有学院
        Collection<Degree> degrees = DegreeService.getInstance().findAll();
        String degrees_json = JSON.toJSONString(degrees);
        //控制台打印结果
        System.out.println(degrees_json);
        //浏览器展示结果
        response.getWriter().println(degrees_json);
    }
}
