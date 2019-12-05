package service;

import dao.UserDao;
import domain.User;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;

public final class UserService {
	private UserDao userDao = UserDao.getInstance();
	private static UserService userService = new UserService();
	
	public UserService() {
	}
	
	public static UserService getInstance(){
		return UserService.userService;
	}

    public User find(Integer id) throws SQLException {
        return userDao.find(id);
    }
    public boolean add(User user, Connection connection) throws SQLException {
        return userDao.add(user,connection);
    }
    public boolean changePassword(Integer id,String password) throws SQLException {
        return userDao.changePassword(id,password);
    }
    public User login(String username,String password) throws SQLException{
        return userDao.login(username,password);
    }
    public boolean delete(Integer id) throws SQLException {
        return userDao.delete(id);
    }
    public User findByUsername(String username) throws SQLException {
        return userDao.findByUsername(username);
    }
    public User findById(Integer id) throws SQLException {
        return userDao.find(id);
    }

}
