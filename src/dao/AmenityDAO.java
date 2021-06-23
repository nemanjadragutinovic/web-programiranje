package dao;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import beans.Amenity;

public class AmenityDAO {
	private final String path = "./files/amenities.json";
	private static Gson g = new Gson();
	private static ApartmentDAO apartmentDao;

	
	public AmenityDAO(ApartmentDAO apartmentDao) {
		this.apartmentDao = apartmentDao;
	}
	
	public List<Amenity> GetAll() throws JsonSyntaxException, IOException{
		List<Amenity> retVal = new ArrayList<Amenity>();
		List<Amenity> fromFile = g.fromJson((Files.readAllLines(Paths.get(path),Charset.defaultCharset()).size() == 0) ? "" : Files.readAllLines(Paths.get(path),Charset.defaultCharset()).get(0), new TypeToken<List<Amenity>>(){}.getType());
		if(fromFile != null) {
			for(Amenity a: fromFile)
				if(!a.isDeleted())
					retVal.add(a);
		}
		return retVal;
	}
	
	public Amenity Create(Amenity amenity) throws JsonSyntaxException, IOException {
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) GetAll();
		amenity.setId(GetMaxID());
		if(amenities == null) {
			amenities = new ArrayList<Amenity>();
		}
		amenities.add(amenity);
		SaveAll(amenities);
		return amenity;
	}
	
	public Amenity Delete(String id) throws JsonSyntaxException, IOException {
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) GetAll();
		Amenity retVal = null;
		for(Amenity a : amenities) {
			if(a.getId() == Integer.parseInt(id)) {
				a.setDeleted(true);
				retVal = a;				
				break;
			}
		}
		SaveAll(amenities);
		apartmentDao.deleteAllAmenities(Integer.parseInt(id));
		return retVal;
	}
	
	public Amenity Update(Amenity amenity) throws JsonSyntaxException, IOException {
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) GetAll();
		for(Amenity a : amenities) {
			if(a.getId() == amenity.getId()) {
				a.setName(amenity.getName());
				break;
			}
		}
		SaveAll(amenities);
		apartmentDao.updateAllAmenities(amenity);
		return amenity;
	}
	
	private int GetMaxID() throws JsonSyntaxException, IOException {
		int maxId = 0;
		ArrayList<Amenity> amenities = (ArrayList<Amenity>) GetAll();
		if(amenities != null) {
			for(Amenity a : amenities) {
				if(a.getId() > maxId)
					maxId = a.getId();
			}
		}
		return ++maxId;
	}
	
	public void SaveAll(Collection<Amenity> amenities) throws JsonIOException, IOException{
	    Writer writer = new FileWriter(path);
		g.toJson(amenities, writer);
	    writer.close();
	}
}
