package dao;
import domain.Teacher;
import domain.User;
import helper.JdbcHelper;
import service.UserService;

import java.sql.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.TreeSet;

public final class TeacherDao {
	//创建私有的静态的集合teachers
	private static Collection<Teacher> teachers = new HashSet<>();
	private static TeacherDao teacherDao=
			new TeacherDao();
	private TeacherDao(){}
	public static TeacherDao getInstance(){
		return teacherDao;
	}
	//返回结果集对象
	public Collection<Teacher> findAll(){
		Collection<Teacher> teachers = new TreeSet<Teacher>();
		try{
			//获得数据库连接对象
			Connection connection = JdbcHelper.getConn();
			//在该连接上创建语句盒子对象
			Statement stmt = connection.createStatement();
			//执行SQL查询语句并获得结果集对象
			ResultSet resultSet = stmt.executeQuery("SELECT * FROM Teacher");
			//若结果存在下一条，执行循环体
			while (resultSet.next()) {
				//打印结果集中记录的id字段
				System.out.print(resultSet.getInt("id"));
				System.out.print(",");
				//打印结果集中记录的no字段
				System.out.print(resultSet.getString("no"));
				System.out.print(",");
				//打印结果集中记录的name字段
				System.out.print(resultSet.getString("name"));
				System.out.print(",");
				//打印结果集中记录的profTitle字段
				System.out.print(resultSet.getString("profTitle_id"));
				System.out.print(",");
				//打印结果集中记录的degree字段
				System.out.print(resultSet.getString("degree_id"));
				System.out.print(",");
				//打印结果集中记录的department字段
				System.out.print(resultSet.getString("department_id"));
				//根据数据库中的数据,创建Teacher类型的对象
				Teacher teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("no"),resultSet.getString("name"), ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),DegreeDao.getInstance().find(resultSet.getInt("degree_id")),DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
				//添加到集合teachers中
				teachers.add(teacher);
			}
			connection.close();
		}catch (SQLException e){
			e.printStackTrace();
		}
		return teachers;
	}
	public Teacher find(Integer id) throws SQLException{
		//声明一个Teacher类型的变量
		Teacher teacher = null;
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String deleteTeacher_sql = "SELECT * FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句
		ResultSet resultSet = preparedStatement.executeQuery();
		if (resultSet.next()){
			teacher = new Teacher(resultSet.getInt("id"), resultSet.getString("no"),resultSet.getString("name"), ProfTitleDao.getInstance().find(resultSet.getInt("profTitle_id")),DegreeDao.getInstance().find(resultSet.getInt("degree_id")),DepartmentDao.getInstance().find(resultSet.getInt("department_id")));
		}
		//关闭资源
		JdbcHelper.close(resultSet,preparedStatement,connection);
		return teacher;
	}
	public boolean add(Teacher teacher) throws SQLException{
		Connection connection = null;
		PreparedStatement pstmt = null;
		int affectedRowNum = 0;
		try{
			connection = JdbcHelper.getConn();
			//将自动提交关闭
			connection.setAutoCommit(false);
			//创建sql语句
			String addTeacher_sql="Insert into Teacher (name,profTitle_id,degree_id,department_id,no) values (?,?,?,?,?)";
			//在该连接上创建预编译语句对象
			pstmt =connection.prepareStatement(addTeacher_sql,PreparedStatement.RETURN_GENERATED_KEYS);
			//为预编译参数赋值
			pstmt.setString(1, teacher.getName());
			pstmt.setInt(2, teacher.getTitle().getId());
			pstmt.setInt(3, teacher.getDegree().getId());
			pstmt.setInt(4,teacher.getDepartment().getId());
			pstmt.setString(5, teacher.getNo());
			teachers.add(teacher);
			//执行预编译对象的excuteUpdate方法，获取添加的记录行数
			affectedRowNum=pstmt.executeUpdate();
			//显示添加的记录的行数
			System.out.println("添加了"+affectedRowNum+"条记录");
			ResultSet resultSet = pstmt.getGeneratedKeys();
			resultSet.next();
			teacher.setId(resultSet.getInt(1));
			UserService.getInstance().add(new User(teacher.getNo(),teacher.getNo(),teacher),connection);
			connection.commit();
		}catch (SQLException e){
			System.out.println(e.getMessage());
			try{
				if (connection!=null){
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
		}finally {
			try {
				//恢复自动提交
				if (connection != null){
					connection.setAutoCommit(true);
				}
			}catch (SQLException e){
				e.printStackTrace();
			}
		}
		//关闭连接
		JdbcHelper.close(pstmt,connection);
		return affectedRowNum > 0;
	}
	//delete方法，根据teacher的id值，删除数据库中对应的teacher对象
	public boolean delete(int id) throws SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		String deleteTeacher_sql = "DELETE FROM teacher WHERE id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(deleteTeacher_sql);
		//为预编译参数赋值
		preparedStatement.setInt(1,id);
		//执行预编译语句，获取删除记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("删除了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
	public boolean update(Teacher teacher) throws SQLException{
		//获得数据库连接对象
		Connection connection = JdbcHelper.getConn();
		//写sql语句
		String updateDegree_sql = " update teacher set name=?,profTitle_id=?,degree_id=?,department_id=? where id=?";
		//在该连接上创建预编译语句对象
		PreparedStatement preparedStatement = connection.prepareStatement(updateDegree_sql);
		//为预编译参数赋值
		preparedStatement.setString(1,teacher.getName());
		preparedStatement.setInt(2,teacher.getTitle().getId());
		preparedStatement.setInt(3,teacher.getDegree().getId());
		preparedStatement.setInt(4,teacher.getDepartment().getId());
		preparedStatement.setInt(5,teacher.getId());
		//执行预编译语句，获取改变记录行数并赋值给affectedRowNum
		int affectedRows = preparedStatement.executeUpdate();
		System.out.println("修改了"+affectedRows+"行记录");
		//关闭资源
		JdbcHelper.close(preparedStatement,connection);
		return affectedRows>0;
	}
}