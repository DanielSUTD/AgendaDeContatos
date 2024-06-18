package model;

public class User {
	
	//Atributos
	private int id;
	private String name;
	private String email;
	private String password;
	private String question;
	private String answer;
	
	//Construtor
	public User() {
	}
	
	//Construtor
	public User(int id, String name, String email, String password, String question, String answer) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.question = question;
		this.answer = answer;
	}
	
	//Construtor
	public User(String name, String email, String password, String question, String answer) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.question = question;
		this.answer = answer;
	}
	
	//Método para retornar o ID
	public int getId() {
		return id;
	}

	//Método para mudar o ID
	public void setId(int id) {
		this.id = id;
	}
	
	//Método para retornar o nome
	public String getName() {
		return name;
	}

	//Método para mudar o nome
	public void setName(String name) {
		this.name = name;
	}

	//Método para retornar o email
	public String getEmail() {
		return email;
	}

	//Método para mudar o email
	public void setEmail(String email) {
		this.email = email;
	}

	//Método para retornar a senha
	public String getPassword() {
		return password;
	}

	//Método para mudar a senha
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	//Método para retornar a alternativa
	public String getQuestion() {
		return question;
	}

	//Método para mudar a alternativa
	public void setQuestion(String question) {
		this.question = question;
	}

	//Método para retornar a resposta
	public String getAnswer() {
		return answer;
	}

	//Método para mudar a resposta
	public void setAnswer(String answer) {
		this.answer = answer;
	}


	//Sobrescrita do método toString!
	@Override
	public String toString() {
		//Retornar todos os dados
		return " |ID: " +id+ " |Nome: " +name+ " |E-mail: " +email+ " |Senha: " +password+ ""
			+ " |Pergunta: " +question+ " |Resposta: " +answer;
	}
	
}
