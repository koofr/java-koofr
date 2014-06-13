package net.koofr.api.v2.resources;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Mount extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;

	public static enum Type {
		DEVICE("device"), IMPORT("import"), EXPORT("export");
		
		private String type;
		private Type(String type) {
			this.type = type;
		}
		public String toString() {
			return type;
		}
	}
	
	private String id;
	private String name;
	private String type;
	private Boolean online;
	private Boolean isShared;
	private List<User> users;
	private List<Group> groups;
	private Permissions permissions;
	private Integer version;
	private User owner;
	private Long spaceTotal, spaceUsed;
	private Root root;

	public Long getSpaceTotal() {
		return spaceTotal;
	}
	
	public void setSpaceTotal(Long spaceTotal) {
		this.spaceTotal = spaceTotal;
	}
	
	public Long getSpaceUsed() {
		return spaceUsed;
	}
	
	public void setSpaceUsed(Long spaceUsed) {
		this.spaceUsed = spaceUsed;
	}
	
	public Mount() {
	}

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

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Boolean getOnline() {
		return online;
	}
	
	public void setOnline(Boolean online) {
		this.online = online;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<Group> getGroups() {
		return groups;
	}

	public void setGroups(List<Group> groups) {
		this.groups = groups;
	}

	public Permissions getPermissions() {
		return permissions;
	}

	public void setPermissions(Permissions permissions) {
		this.permissions = permissions;
	}

	public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	public User getOwner() {
		return owner;
	}
	
	public void setOwner(User owner) {
		this.owner = owner;
	}
	
	public Boolean getIsShared() {
		return isShared;
	}
	
	public void setIsShared(Boolean isShared) {
		this.isShared = isShared;
	}
	
	public Root getRoot() {
		return root;
	}
	
	public void setRoot(Root root) {
		this.root = root;
	}
	
}
