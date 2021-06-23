package beans;

public class Location {
	private double latitude; // sirina
	private double longitude; // duzina
	private Adress adress;
	
	public Location() {
		
	}
	
	public Location(double latitude, double longitude, Adress adress) {
		super();
		this.latitude = latitude;
		this.longitude = longitude;
		this.adress = adress;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public Adress getAdress() {
		return adress;
	}

	public void setAdress(Adress adress) {
		this.adress = adress;
	}
}
