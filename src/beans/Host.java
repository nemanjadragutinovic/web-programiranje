package beans;

import java.util.ArrayList;
import java.util.List;

public class Host extends User{

	private List<Apartment> appartments;

	public Host() {
		appartments = new ArrayList<Apartment>();
		// TODO Auto-generated constructor stub
	}

	public Host(String username, String password, String name, String surname, Gender gender) {
		setGender(gender);
		setSurname(surname);
		setName(name);
		setPassword(password);
		setUsername(username);
		setUserType(UserType.Host);
		appartments = new ArrayList<Apartment>();
		// TODO Auto-generated constructor stub
	}

	public List<Apartment> getAppartments() {
		return appartments;
	}

	public void setAppartments(List<Apartment> appartments) {
		this.appartments = appartments;
	}
	
	
}
