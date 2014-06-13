package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnore;
import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class UserInfo extends JsonBase implements Serializable {

	private static final long serialVersionUID = 1L;

	private String id;
	private String firstName, lastName;
	private String email;

	public UserInfo() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	@JsonIgnore
	public String getFormattedName() {
		StringBuilder sb = new StringBuilder();
		if(null != firstName) {
			sb.append(firstName).append(" ");			
		}
		if(null != lastName) {
			sb.append(lastName);
		}
		if(sb.length() == 0) {
			sb.append(email);
		}
		return sb.toString();
	}
	
}