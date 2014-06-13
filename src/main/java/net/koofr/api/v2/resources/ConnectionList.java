package net.koofr.api.v2.resources;

import java.io.Serializable;
import java.util.List;

public class ConnectionList extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private List<User> users;
	private List<Group> groups;
	
	public ConnectionList() {
	}
	
	public List<Group> getGroups() {
		return groups;
	}
	
	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}
	
	public List<User> getUsers() {
		return users;
	}
	
	public void setUsers(List<User> users) {
		this.users = users;
	}
}
