package beans;

import java.util.Date;
import java.util.List;

public class Apartment {
	
	private int id;
	private ApartmentType type;
	private int numberOfRoom;
	private int numberOfGuest;
	private Location location;
	private List<Period> dateForRenting;
	private List<Long> freeDateForRenting;
	private Host host;
	private List<String> pictures;
	private double priceForNight;
	private String checkInTime = "14:00";
	private String checkOutTime ="14:00";
	private ApartmentStatus status;
	private List<Amenity> amenities;
	private List<Reservation> reservations;
	private boolean deleted=false;
	
	
	public Apartment() {
	
	}


	public Apartment(int id,ApartmentType type, int numberOfRoom, int numberOfGuest, Location location,
			List<Period> dateForRenting, List<Long> freeDateForRenting, Host host, 
			List<String> pictures, double priceForNight, String checkInTime, String checkOutTime, ApartmentStatus status,
			List<Amenity> amenities, List<Reservation> reservations) {
		super();
		this.id=id;
		this.type = type;
		this.numberOfRoom = numberOfRoom;
		this.numberOfGuest = numberOfGuest;
		this.location = location;
		this.dateForRenting = dateForRenting;
		this.freeDateForRenting = freeDateForRenting;
		this.host = host;
		this.pictures = pictures;
		this.priceForNight = priceForNight;
		this.checkInTime = checkInTime;
		this.checkOutTime = checkOutTime;
		this.status = status;
		this.amenities = amenities;
		this.reservations = reservations;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}
	
	public ApartmentType getType() {
		return type;
	}


	public void setType(ApartmentType type) {
		this.type = type;
	}


	public int getNumberOfRoom() {
		return numberOfRoom;
	}


	public void setNumberOfRoom(int numberOfRoom) {
		this.numberOfRoom = numberOfRoom;
	}


	public int getNumberOfGuest() {
		return numberOfGuest;
	}


	public void setNumberOfGuest(int numberOfGuest) {
		this.numberOfGuest = numberOfGuest;
	}


	public Location getLocation() {
		return location;
	}


	public void setLocation(Location location) {
		this.location = location;
	}


	public List<Period> getDateForRenting() {
		return dateForRenting;
	}


	public void setDateForRenting(List<Period> dateForRenting) {
		this.dateForRenting = dateForRenting;
	}


	public List<Long> getFreeDateForRenting() {
		return freeDateForRenting;
	}


	public void setFreeDateForRenting(List<Long> freeDateForRenting) {
		this.freeDateForRenting = freeDateForRenting;
	}


	public Host getHost() {
		return host;
	}


	public void setHost(Host host) {
		this.host = host;
	}



	public List<String> getPictures() {
		return pictures;
	}


	public void setPictures(List<String> pictures) {
		this.pictures = pictures;
	}


	public double getPriceForNight() {
		return priceForNight;
	}


	public void setPriceForNight(double priceForNight) {
		this.priceForNight = priceForNight;
	}


	public String getCheckInTime() {
		return checkInTime;
	}


	public void setCheckInTime(String checkInTime) {
		this.checkInTime = checkInTime;
	}


	public String getCheckOutTime() {
		return checkOutTime;
	}


	public void setCheckOutTime(String checkOutTime) {
		this.checkOutTime = checkOutTime;
	}


	public ApartmentStatus getStatus() {
		return status;
	}


	public void setStatus(ApartmentStatus status) {
		this.status = status;
	}


	public List<Amenity> getAmenities() {
		return amenities;
	}


	public void setAmenities(List<Amenity> amenities) {
		this.amenities = amenities;
	}


	public List<Reservation> getReservations() {
		return reservations;
	}


	public void setReservations(List<Reservation> reservations) {
		this.reservations = reservations;
	}


	public boolean isDeleted() {
		return deleted;
	}


	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}
	
	
	
	
}
