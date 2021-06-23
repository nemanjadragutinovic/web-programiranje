package application;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

import java.io.File;
import java.io.IOException;

import beans.Administrator;
import beans.Gender;
import beans.User;
import controllers.AmenityController;
import controllers.ApartmentController;

import controllers.UserController;
import dao.AmenityDAO;
import dao.ApartmentDAO;

import dao.UserDAO;
import services.AmenityService;
import services.ApartmentService;

import services.UserService;

public class Aplication {

	public static void main(String[] args) throws IOException{
		
		port(80);
		
		//User a = new Administrator("admin", "admin", "Stefan", "Zec", Gender.male);
		  
		//UserDAO dao = new UserDAO(); dao.Create(a); 
				
		
		
		staticFiles.externalLocation(new File("./static").getCanonicalPath());
		
		UserDAO userDAO = new UserDAO();
		new UserController(new UserService(userDAO));
		
		
		ApartmentDAO apartmentDAO = new ApartmentDAO(userDAO);
		AmenityDAO amenityDAO = new AmenityDAO(apartmentDAO);
		
		
		new UserController(new UserService(userDAO));
		new AmenityController(new AmenityService(amenityDAO));
		
		new ApartmentController(new ApartmentService(apartmentDAO));
		
		
		
	}

}
