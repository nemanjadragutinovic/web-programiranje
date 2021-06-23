package beans;

public class Adress {
	private String city;
	private int postNumber;
	private String street;
	private String streetNumber;
	
	public Adress() {
		
	}
	
	public Adress(String city, int postNumber, String street, String streetNumber) {
		super();
		this.city = city;
		this.postNumber = postNumber;
		this.street = street;
		this.streetNumber = streetNumber;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public int getPostNumber() {
		return postNumber;
	}

	public void setPostNumber(int postNumber) {
		this.postNumber = postNumber;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getStreetNumber() {
		return streetNumber;
	}

	public void setStreetNumber(String streetNumber) {
		this.streetNumber = streetNumber;
	}	
}
