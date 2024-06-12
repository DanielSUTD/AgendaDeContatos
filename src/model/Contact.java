package model;

public class Contact {

	private int id;
	private String name;
	private String phone;
	private String email;
	private int userID;

	public Contact(int id, String name, String phone, String email, int userID) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.userID = userID;
	}

	public Contact(int id, String name, String phone, int userID) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.userID = userID;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getPhone() {
		return phone;
	}


	public void setPhone(String phone) {
		this.phone = phone;
	}
	

	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}
	
	
	public int getUserID() {
		return userID;
	}

	public void setUserID(int userID) {
		this.userID = userID;
	}

	public String toString() {
		return " |ID: " +id+ " |Nome: " +name+ " |Telefone: " +phone+ " |E-mail: " +email+ " |ID do usuário:" +userID;
	}
	
}
