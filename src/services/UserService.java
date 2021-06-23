package services;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;

import beans.LoginData;
import beans.Administrator;
import beans.Guest;
import beans.Host;
import beans.User;
import dao.UserDAO;
import dao.adapter.RuntimeTypeAdapterFactory;

public class UserService {

	private static Gson gSon;
	private UserDAO userDao;
	
	public UserService(UserDAO userDao) {
		this.userDao = userDao;
		RuntimeTypeAdapterFactory<User> userAdapterFactory = RuntimeTypeAdapterFactory.of(User.class)
		        .registerSubtype(Guest.class)
		        .registerSubtype(Administrator.class)
		        .registerSubtype(Host.class);
		gSon = new GsonBuilder()
		     .registerTypeAdapterFactory(userAdapterFactory)
	         .create();
	}

	public String Register(User user) throws JsonSyntaxException, IOException {
		try {
			userDao.Create(user);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return gSon.toJson(user);		
	}
	
	
	
	public String getUser(String username) {
		try {
			return gSon.toJson(userDao.get(username));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return gSon.toJson(null);
	}
	
	public String GetAll(int whatToGet, String username) {
		try {
			return gSon.toJson(userDao.GetAllByUserType(whatToGet, username));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
					
	
	public User Login(LoginData data) {		
		try {
			return userDao.Login(data.getUsername(),data.getPassword());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;

	}
	
	public String Update(User user) {
		try {
			return gSon.toJson(userDao.Update(user));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
	public String searchUsers(String username, String name, String surname, String userType, String pol,int staVratiti, String ulogovaniUser) {
		try {
			return gSon.toJson(userDao.searchUsers(username, name, surname, userType,pol, staVratiti, ulogovaniUser));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	
	public boolean toggleBlockUser(String username) {
		try {
			return userDao.toggleBlockUser(username);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
