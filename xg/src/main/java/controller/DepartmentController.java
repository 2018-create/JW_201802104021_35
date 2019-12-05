package controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;
import domain.Department;
import service.DepartmentService;
import util.JSONUtil;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.SQLException;
import java.util.Collection;

@WebServlet("/department.ctl")
public class DepartmentController extends HttpServlet {
    /**
     * 方法-功能
     * put 修改
     * post 添加
     * delete 删除
     * get 查找
     */
    //POST 49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //根据request对象，获得代表参数的JSON字串
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Department对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        System.out.println(departmentToAdd);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try {
            //增加加Department对象
            DepartmentService.getInstance().add(departmentToAdd);
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
    //DELETE  49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //响应
        //response.setContentType("html/text;charset=UTF8");
        //读取参数id
        String id_str = request.getParameter("id");
        int id = Integer.parseInt(id_str);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //到数据库表中删除
            DepartmentService.getInstance().delete(id);
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
    //PUT  49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String department_json = JSONUtil.getJSON(request);
        //将JSON字串解析为Degree对象
        Department departmentToAdd = JSON.parseObject(department_json, Department.class);
        //创建JSON对象
        JSONObject resp = new JSONObject();
        try{
            //增加加Department对象
            DepartmentService.getInstance().update(departmentToAdd);
            //加入数据信息
            resp.put("MSG", "修改成功");
        } catch (SQLException e){
            e.printStackTrace();
            resp.put("MSG", "数据库操作异常");
        }catch (Exception e) {
            e.printStackTrace();
            resp.put("MSG", "网络异常");
        }
        //响应
        response.getWriter().println(resp);
    }
    //GET  49.235.219.168:8080/bysj/department.ctl
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        //创建JSON对象message，以便往前端响应信息
        JSONObject message = new JSONObject();
        //读取参数id
        String id_str = request.getParameter("id");
        String paraType = request.getParameter("paraType");
        try {
            //如果id = null, 表示响应所有院系对象，否则响应id指定的院系对象
            if (id_str != null) {
                int id = Integer.parseInt(id_str);
                //如果paraType为空，响应对应id的院系对象；如果paraType为school时，响应对应school_id的所有院系对象
                if (paraType == null) {
                    responseDepartment(id,response);
                } else if (paraType.equals("school")){
                    responseDepartmentBySchool(id,response);
                }
            }else{
                responseDepartments(response);
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
    private void responseDepartment(int id, HttpServletResponse response)
            throws ServletException, IOException,SQLException {
            //根据id查找
            Department department = DepartmentService.getInstance().find(id);
            String department_json = JSON.toJSONString(department);
            //控制台打印结果
            System.out.println(department_json);
            //浏览器展示结果
            response.getWriter().println(department_json);
    }
    //响应所有对象
    private void responseDepartments(HttpServletResponse response)
            throws ServletException, IOException,SQLException {
        //获得所有
        Collection<Department> departments = DepartmentService.getInstance().findAll();
        String departments_json = JSON.toJSONString(departments);
        //控制台打印结果
        System.out.println(departments_json);
        //浏览器展示结果
        response.getWriter().println(departments_json);
    }

    //响应对应school_id所有的院系对象
    private void responseDepartmentBySchool(int id, HttpServletResponse response) throws SQLException, IOException {
        //获得对应school_id的所有院系
        Collection<Department>departments = DepartmentService.getInstance().findALLBySchool(id);
        String departments_json = JSON.toJSONString(departments);
        //响应Departments_json到前端
        response.getWriter().println(departments_json);
    }
}
