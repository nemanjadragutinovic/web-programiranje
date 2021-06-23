package dao;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import beans.Administrator;
import beans.Apartment;
import beans.Gender;
import beans.Guest;
import beans.Host;
import beans.Reservation;
import beans.ReservationStatus;
import beans.User;
import beans.UserType;
import dao.adapter.RuntimeTypeAdapterFactory;

public class UserDAO {

	private final String path = "./files/users.json";
	private static Gson g;

	public UserDAO() {
		RuntimeTypeAdapterFactory<User> userAdapterFactory = RuntimeTypeAdapterFactory.of(User.class)
				        .registerSubtype(Guest.class)
				        .registerSubtype(Administrator.class)
				        .registerSubtype(Host.class);
		g = new GsonBuilder()
				     .registerTypeAdapterFactory(userAdapterFactory)
			         .create();
	}
	
	public List<User> GetAll() throws JsonSyntaxException, IOException{		
		return g.fromJson((Files.readAllLines(Paths.get(path),Charset.defaultCharset()).size() == 0) ? "" : Files.readAllLines(Paths.get(path),Charset.defaultCharset()).get(0), new TypeToken<List<User>>(){}.getType());
	}
	
	
	
	public User Create(User user) throws JsonSyntaxException, IOException {
		ArrayList<User> users = (ArrayList<User>) GetAll();
		if(users == null) {
			users = new ArrayList<User>();
		}
		users.add(user);
		SaveAll(users);
		return user;
	}
	
	public void SaveAll(Collection<User> users) throws JsonIOException, IOException{
		PrintWriter out = new PrintWriter(path);
		String str = g.toJson(users, new TypeToken<List<User>>(){}.getType());
		out.println(str);
		out.close();
	}
	
	
	
	
	
	public User get(String username) throws JsonSyntaxException, IOException {
		ArrayList<User> users = (ArrayList<User>) GetAll();
		if(users != null) {
			for(User u : users) {
				if(u.getUsername().equals(username)) {
					return u;
				}
			}
		}
		
		return null;
	}
	
	public User Login(String username,String password) throws JsonSyntaxException, IOException {
		for(User user : GetAll()) {
			if(user.getUsername().equals(username) && user.getPassword().equals(password)) {
				return user;
			}
		}
		return null;
	}
	
	
	public List<User> GetAllByUserType(int staVratiti, String username) throws JsonSyntaxException, IOException{		
		ArrayList<User> users = (ArrayList<User>) GetAll();
		List<User> retVal = new ArrayList<User>();
		for(User u : users) {
			if(staVratiti == 1) {
				if(u instanceof Guest) {
					for(Reservation r : ((Guest)u).getReservations()) {
						if(r.getAppartment().getHost().getUsername().equals(username)) {
							if(!u.isBlocked())
								retVal.add(u);
							break;
						}
					}
				}
			}else {
				retVal.add(u);
			}
		}
	
		return retVal;
	}
	
	public User Update(User user) throws JsonSyntaxException, IOException {
		ArrayList<User> users = (ArrayList<User>) GetAll();
		for(User u : users) {
			if(u.getUsername().equals(user.getUsername())) {
				users.set(users.indexOf(u),user);
				break;
			}
		}
		SaveAll(users);
		return user;
	}
	
	public List<User> searchUsers(String username, String name, String surname, String userType, String pol,int staVratiti, String ulogovaniUser) throws JsonSyntaxException, IOException{
		Gender gender = Gender.male;
		if(pol.equals("female")) 
			gender = Gender.female;
		
		UserType tip = UserType.Guest;
		if(userType.equals("Guest"))
			tip = UserType.Guest;
		else if(userType.equals("Host"))
			tip = UserType.Host;
		else if(userType.equals("Administrator"))
			tip = UserType.Administrator;
		
		ArrayList<User> list = (ArrayList<User>) GetAllByUserType(staVratiti, ulogovaniUser);
		List<User> retVal = new ArrayList<User>();

		for(User user : list) {
			if(((!username.isEmpty()) ? user.getUsername().equals(username) : true) && ((!name.isEmpty()) ? user.getName().equals(name) : true) 
				&& ((!surname.isEmpty()) ? user.getSurname().equals(surname) : true) && ((!userType.isEmpty()) ? tip == user.getUserType() : true) && ((!pol.isEmpty()) ? gender == user.getGender() : true)) {
					retVal.add(user);
			}
		}		
		return retVal;

	}
	
	public boolean toggleBlockUser(String username) throws JsonIOException, IOException {
		ArrayList<User> users = (ArrayList<User>) GetAll();
		for(User u : users) {
			if(u.getUsername().equals(username)){
				if(u.isBlocked())
					u.setBlocked(false);
				else
					u.setBlocked(true);
				break;
			}
		}
		SaveAll(users);
		
		return true;
	}
	
	public void changeReservationStatus(String id, ReservationStatus status) throws JsonIOException, IOException {
		ArrayList<User> users = (ArrayList<User>) GetAll();
		boolean changed = false;

		for(User u : users) {
			if(u.getUserType() == UserType.Guest) {
				for(Reservation r : ((Guest)u).getReservations()) {
					if(r.getId() == Integer.parseInt(id)) {
						r.setStatus(status);
						changed = true;
						break;
					}
				}
			}
			if(changed)
				break;
		}
		SaveAll(users);
	}

}
