package controllers;

import static spark.Spark.delete;
import static spark.Spark.get;
import static spark.Spark.post;

import com.google.gson.Gson;

import beans.Amenity;
import services.AmenityService;

public class AmenityController {
	private static Gson g = new Gson();

	public AmenityController(final AmenityService amenityService) {
		
		post("/amenities/add", (req, res) -> 
					amenityService.Create(g.fromJson(req.body(), Amenity.class)));

		
		get("/amenities", (req,res) -> amenityService.GetAll());
		
		post("/amenities", (req, res) -> 
						amenityService.Update(g.fromJson(req.body(), Amenity.class)));
		
		delete("/amenities/:id", (req,res) -> amenityService.Delete(req.params("id")));
	}
}
