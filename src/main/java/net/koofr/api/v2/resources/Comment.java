package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Comment extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private User user;
	private String content;
	private Long added;
	
	public Comment() {
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Long getAdded() {
		return added;
	}

	public void setAdded(Long added) {
		this.added = added;
	}
}
