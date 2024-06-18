package model;

public class Contact {

	//Atributos
	private int id;
	private String name;
	private String phone;
	private String email;
	private int userID;
	
	//Construtor
	public Contact() {
	}

	//Construtor
	public Contact(int id, String name, String phone, String email, int userID) {
		this.id = id;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.userID = userID;
	}

	//Construtor
	public Contact(String name, String phone, String email, int userID) {
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.userID = userID;
	}



	//Retornar o ID
	public int getId() {
		return id;
	}
	
	//Mudar o ID
	public void setId(int id) {
		this.id = id;
	}


	//Retornar o nome
	public String getName() {
		return name;
	}

	//Mudar o nome
	public void setName(String name) {
		this.name = name;
	}
	
	
	//Retornar o telefone
	public String getPhone() {
		return phone;
	}

	//Mudar o telefone
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	//Retornar o Email
	public String getEmail() {
		return email;
	}

	//Mudar o Email
	public void setEmail(String email) {
		this.email = email;
	}
	
	//Retornar o ID do usuário que pertence
	public int getUserID() {
		return userID;
	}

	//Mudar o ID do usuário que pertence
	public void setUserID(int userID) {
		this.userID = userID;
	}

	//Sobrescrita do método toString
	@Override
	public String toString() {
		//Retorna os dados completos!
		return " |ID: " +id+ " |Nome: " +name+ " |Telefone: " +phone+ " |E-mail: " +email+ " |ID do usuário:" +userID;
	}
	
}
