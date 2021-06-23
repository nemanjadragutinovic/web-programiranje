package controllers;

import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import beans.LoginData;
import beans.Administrator;
import beans.Guest;
import beans.Host;
import beans.User;
import dao.adapter.RuntimeTypeAdapterFactory;
import services.UserService;
import spark.Session;

public class UserController {
	private static Gson g;
	private static Gson gs = new Gson();

	public UserController(final UserService userService) {
		
		RuntimeTypeAdapterFactory<User> userAdapterFactory = RuntimeTypeAdapterFactory.of(User.class)
		        .registerSubtype(Guest.class)
		        .registerSubtype(Administrator.class)
		        .registerSubtype(Host.class);
		g = new GsonBuilder()
		     .registerTypeAdapterFactory(userAdapterFactory)
	         .create();
		
		
		
		
		get("/users/log/test", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			User user = ss.attribute("user");
			return g.toJson(user);
		});
		
		get("/users", (req,res) -> {
			
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int whatToGet = -1;
			if(user instanceof Guest)
				whatToGet = 0;
			else if(user instanceof Host)
				whatToGet = 1;
			else 
				whatToGet = 2;
			
			
			return userService.GetAll(whatToGet, user.getUsername());
		});
		
		post("/users/login", (req, res) -> {
			res.type("application/json");
			User u = userService.Login(gs.fromJson(req.body(), LoginData.class));
			if(u != null) {
				if(!u.isBlocked()) {
					Session ss = req.session(true);
					User user = ss.attribute("user");
					if (user == null) {
						user = u;
						ss.attribute("user", user);
					}
					return g.toJson(user);
				}
			}
			return "";
		});
		
		get("/users/log/logout", (req, res) -> {
			res.type("application/json");
			Session ss = req.session(true);
			User user = ss.attribute("user");
			
			if (user != null) {
				ss.invalidate();
			}
			return true;
		});
		
		
		post("/users/addGuest", (req, res) ->{ 
			
			User u = g.fromJson(req.body(), Guest.class);
			
			return userService.Register(u);		
		});
		
		post("/users/addHost", (req, res) ->{ 		
			User u = g.fromJson(req.body(), Host.class);		
			return userService.Register(u);		
		});

		get("/users/Register/:username", (req,res) -> userService.getUser(req.params("username")));
		
		put("/users/update", (req,res)-> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			if(user instanceof Guest) {
				String a = userService.Update(g.fromJson(req.body(), Guest.class));
				ss.attribute("user", g.fromJson(a, Guest.class));
				return a;
			}
			else if(user instanceof Host) {
				String a =userService.Update(g.fromJson(req.body(), Host.class));
				ss.attribute("user", g.fromJson(a, Host.class));
				return a;
			}
			else {
				String a =userService.Update(g.fromJson(req.body(), Administrator.class));
				ss.attribute("user", g.fromJson(a, Administrator.class));
				return a;
			}
		});
		
		
		get("/users/search/", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int staVratiti = -1;
			if(user instanceof Guest)
				staVratiti = 0;
			else if(user instanceof Host)
				staVratiti = 1;
			else 
				staVratiti = 2;
			return userService.searchUsers(req.queryParams("username"), req.queryParams("name"), req.queryParams("surname"), req.queryParams("userType"), req.queryParams("gender"), staVratiti, user.getUsername());
		});
	
		
		put("/users/blockOperations/:username", (req,res)->(userService.toggleBlockUser(req.params("username"))));
		
		
	}
}
