package controllers;
import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;
import static spark.Spark.put;

import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import beans.Amenity;
import beans.Apartment;
import beans.Guest;
import beans.Host;
import beans.Reservation;
import beans.ReservationStatus;
import beans.User;
import services.ApartmentService;
import spark.Session;

public class ApartmentController {
	private static Gson g = new Gson();

	public ApartmentController(final ApartmentService apartmentService) {
		
		post("/apartment/add", (req, res) -> {
			Session ss = req.session(true);
			Host user = ss.attribute("user");
			Apartment a = g.fromJson(req.body(), Apartment.class);
			//user.setAppartments(null)
			a.setHost(user);
			return apartmentService.Create(a);
			
		});
		
		get("/apartment/get/reservations", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int whatToGet = -1;
			if(user instanceof Guest)
				whatToGet = 0;
			else if(user instanceof Host)
				whatToGet = 1;
			else 
				whatToGet = 2;
			
			return apartmentService.getAllReservations(whatToGet, user.getUsername());
		});
		
		get("/reservation/search/parameters", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int whatToGet = -1;
			if(user instanceof Guest)
				whatToGet = 0;
			else if(user instanceof Host)
				whatToGet = 1;
			else 
				whatToGet = 2;
			
	
			
			return apartmentService.searchReservation(req.queryParams("guestUsername"), req.queryParams("sortValue"), req.queryParams("reservationStatus"),whatToGet, user.getUsername());
		
		});
		
		get("/apartmentsForUser", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int whatToGet = -1;
			if(user instanceof Guest || user==null)
				whatToGet = 0;
			else if(user instanceof Host)
				whatToGet = 1;
			else 
				whatToGet = 2;
			
			String username;
			if(user==null) 
				username="";
			else {
				username=user.getUsername();
			}
				
			
			
			return apartmentService.GetAllForUser(whatToGet,username);
		});
		
		get("/apartments/search/parameters", (req,res) -> {
			Session ss = req.session(true);
			User user = ss.attribute("user");
			int whatToGet = -1;
			if(user instanceof Guest)
				whatToGet = 0;
			else if(user instanceof Host)
				whatToGet = 1;
			else 
				whatToGet = 2;
			
			String username;
			if(user==null) 
				username="";
			else {
				username=user.getUsername();
			}
			

			return apartmentService.searchApartments(req.queryParams("location"), req.queryParams("dateFrom"), req.queryParams("dateTo"), req.queryParams("numberOfGuest"), req.queryParams("minRoom"), req.queryParams("maxRoom"), req.queryParams("minPrice"), req.queryParams("maxPrice"), req.queryParams("sortValue"), req.queryParams("type"), req.queryParams("apartmentStatus"),g.fromJson(req.queryParams("amenities"), new TypeToken<List<Amenity>>(){}.getType()),whatToGet,username);
		});
		

		get("/apartment/:id", (req,res) -> apartmentService.getApartment(req.params("id")));
		
		
		get("/apartment/occupiedDates/:id", (req,res) ->  apartmentService.getOccupiedDates(req.params("id")));
		
		get("/apartment/occupiedRanges/:id", (req,res) ->  apartmentService.getOccupiedRanges(req.params("id")));
		
		post("/apartment/createReserveForSelectedApartment", (req,res) -> {
			Reservation reservation = g.fromJson(req.body(), Reservation.class);
			
			Session sesion = req.session(true);
			Guest user = sesion .attribute("user");
			
			user.setRentedAppartments(null);
			user.setReservations(null);
			
			reservation.setGuest(user);
			
			return apartmentService.createReserveForSelectedApartment(reservation);
		});
		
		
		post("/apartment/updateCurrentApartment", (req, res) -> 
		apartmentService.updateCurrentApartment(g.fromJson(req.body(), Apartment.class)));
		
		
		
		put("/apartment/accept/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReservationStatus.accepted)));
		
		put("/apartment/reject/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReservationStatus.rejected)));
		
		put("/apartment/withdraw/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReservationStatus.withdraw)));
		
		put("/apartment/finished/:id", (req,res) -> (apartmentService.changeReservationStatus(req.params("id"),ReservationStatus.done)));
		
		
		delete("/apartment/deleteSelectedApartment/:id", (req,res) -> apartmentService.DeleteApartment(req.params("id")));
		
		post("/apartment/AddNewPeriod", (req, res) -> 
		apartmentService.addNewPeriod(g.fromJson(req.body(), Apartment.class)));
		
		
		
	}
}
