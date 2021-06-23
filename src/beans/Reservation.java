package beans;

import java.util.Date;

public class Reservation {

	int id;
	private Apartment appartment;
	private long startDate;
	private int daysForStay = 1;
	private double price;
	private String message;
	private Guest guest;
	private ReservationStatus status;
	
	public Reservation() {}
	
	public Reservation(int id, Apartment appartment, long startDate, int daysForStay, double price, String message, Guest guest,
			ReservationStatus status) {
		super();
		this.id = id;
		this.startDate = startDate;
		this.daysForStay = daysForStay;
		this.price = price;
		this.message = message;
		this.guest = guest;
		this.status = status;
		this.appartment = appartment;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}
	public Apartment getAppartment() {
		return appartment;
	}

	public void setAppartment(Apartment appartment) {
		this.appartment = appartment;
	}

	public long getStartDate() {
		return startDate;
	}


	public void setStartDate(long startDate) {
		this.startDate = startDate;
	}


	public int getDaysForStay() {
		return daysForStay;
	}


	public void setDaysForStay(int daysForStay) {
		this.daysForStay = daysForStay;
	}


	public double getPrice() {
		return price;
	}


	public void setPrice(double price) {
		this.price = price;
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}


	public Guest getGuest() {
		return guest;
	}


	public void setGuest(Guest guest) {
		this.guest = guest;
	}


	public ReservationStatus getStatus() {
		return status;
	}


	public void setStatus(ReservationStatus status) {
		this.status = status;
	}
	
	
}
