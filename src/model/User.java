package model;

public class User {
	
	
	private int id;
	private String name;
	private String email;
	private String password;
	private String question;
	private String answer;
	
	
	public User() {
	}
	
	
	public User(int id, String name, String email, String password, String question, String answer) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.password = password;
		this.question = question;
		this.answer = answer;
	}
	
	
	public User(String name, String email, String password, String question, String answer) {
		this.name = name;
		this.email = email;
		this.password = password;
		this.question = question;
		this.answer = answer;
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

	
	public String getEmail() {
		return email;
	}

	
	public void setEmail(String email) {
		this.email = email;
	}

	
	public String getPassword() {
		return password;
	}

	
	public void setPassword(String password) {
		this.password = password;
	}
	
	
	
	public String getQuestion() {
		return question;
	}

	
	public void setQuestion(String question) {
		this.question = question;
	}

	
	public String getAnswer() {
		return answer;
	}

	
	public void setAnswer(String answer) {
		this.answer = answer;
	}


	@Override
	public String toString() {
		return " |ID: " +id+ " |Nome: " +name+ " |E-mail: " +email+ " |Senha: " +password+ ""
			+ " |Pergunta: " +question+ " |Resposta: " +answer;
	}
	
}
