package services;

import java.io.IOException;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import beans.Amenity;
import dao.AmenityDAO;

public class AmenityService {
	private static Gson g = new Gson();
	private static AmenityDAO amenityDao;
	
	public AmenityService(AmenityDAO amenityDao) {
		this.amenityDao = amenityDao;
	}
	
	
	public String Create(Amenity amenity) throws JsonSyntaxException, IOException {
		try {
			amenityDao.Create(amenity);
		}  catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return g.toJson(amenity);		
	}
	
	
	public String Update(Amenity amenity) {
		try {
			amenityDao.Update(amenity);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return g.toJson(amenity);		
	}
	
	public String GetAll() {
		try {
			return g.toJson(amenityDao.GetAll());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public String Delete(String id) {
		try {
			return g.toJson(amenityDao.Delete(id));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		return null;
	}
	
}
