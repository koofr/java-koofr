package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class NotificationSettings extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private Boolean shared;
	private Boolean newComment;
	private Boolean deviceOffline;
	
	public NotificationSettings() {
	}

	public Boolean isShared() {
		return shared;
	}

	public void setShared(Boolean shared) {
		this.shared = shared;
	}

	public Boolean isNewComment() {
		return newComment;
	}

	public void setNewComment(Boolean newComment) {
		this.newComment = newComment;
	}

	public Boolean isDeviceOffline() {
		return deviceOffline;
	}

	public void setDeviceOffline(Boolean deviceOffline) {
		this.deviceOffline = deviceOffline;
	}
}
