package com.ITtraining.project.entities.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class UserDTO {

	@NotNull(message="First name must be provided.")
	private String firstName;

	@NotNull(message="Last name must be provided.")
	private String lastName;

	@NotNull(message = "Email must be provided.")
	@Pattern(regexp = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
			+ "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$", message="Email is not valid.")
	private String email;

	@NotNull(message="Username name must be provided.")
	@Size(min=5, max=20,  message = "Username must be between {min} and {max} characters long.")
	private String username;

	@NotNull(message="Password name must be provided.")
	// ne mora se koristiti anotacija @Size, u okviru regexp smo naveli min broj karaktera - {5,}
	@Pattern(regexp = "^[A-Za-z0-9]{5,}$", message="Password is not valid.")
	private String password;

	@NotNull(message="Repeated password name must be provided.")
	private String repeatedPassword;

	public UserDTO() {
		super();
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRepeatedPassword() {
		return repeatedPassword;
	}

	public void setRepeatedPassword(String repeatedPassword) {
		this.repeatedPassword = repeatedPassword;
	}

}
