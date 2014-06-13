package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class User extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private String id;
	private String name;
	private String email;
	private Permissions permissions;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public Permissions getPermissions() {
		return permissions;
	}	

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}	
	
	public User() {
	}

}
