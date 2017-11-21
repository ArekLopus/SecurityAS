package loginModuleDB;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {
	
	@Id @GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;
	@Column(unique=true)
	private String name;
	private String surname;
	private String password;
	private String role;
	
	public User() {}
	
	public User(String name, String surname) {
		super();
		this.name = name;
		this.surname = surname;
	}

	public User(String name, String surname, String password, String role) {
		super();
		this.name = name;
		this.surname = surname;
		this.password = password;
		this.role = role;
	}


	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", name=" + name + ", surname=" + surname
				+ ", role=" + role + "]";
	}
	
	
}
