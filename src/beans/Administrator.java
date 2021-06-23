package beans;

public class Administrator extends User {

	public Administrator() {
		super();
	}
	
	public Administrator(String username, String password, String name, String surname, Gender gender) {
		setGender(gender);
		setSurname(surname);
		setName(name);
		setPassword(password);
		setUsername(username);
		setUserType(UserType.Administrator);

		// TODO Auto-generated constructor stub
	}
	
}
