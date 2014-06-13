package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Device extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String id;
	private String apiKey;
	private String name;
	private String status;
	private Long spaceTotal, spaceUsed;
	private Version version;
	private Boolean readonly;
	
	/* TODO: provider missing. not sure if we need this at all. */
	
	public static enum Status {
		ONLINE("online"), OFFLINE("offline");
		
		private String s;
		private Status(String s) {
			this.s = s;
		}
		public String toString() {
			return s;
		}
	}
	
	public static class Version {
		private String version;
		private Boolean canUpdate;
		private Boolean shouldUpdate;
		private Boolean outdated;
		public String getVersion() {
			return version;
		}
		public void setVersion(String version) {
			this.version = version;
		}
		public Boolean getCanUpdate() {
			return canUpdate;
		}
		public void setCanUpdate(Boolean canUpdate) {
			this.canUpdate = canUpdate;
		}
		public Boolean getShouldUpdate() {
			return shouldUpdate;
		}
		public void setShouldUpdate(Boolean shouldUpdate) {
			this.shouldUpdate = shouldUpdate;
		}
		public Boolean getOutdated() {
			return outdated;
		}
		public void setOutdated(Boolean outdated) {
			this.outdated = outdated;
		}
	}
		
	public Device() {	
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

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
	
	public Version getVersion() {
		return version;
	}

	public void setVersion(Version version) {
		this.version = version;
	}

	public Boolean getReadonly() {
		return readonly;
	}

	public void setReadonly(Boolean readonly1) {
		readonly = readonly1;
	}	
}
