package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SecuritySettings extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private Boolean downloadLinkAutoPassword;
	private Boolean downloadLinkRequirePassword;
	private Boolean uploadLinkAutoPassword;
	private Boolean uploadLinkRequirePassword;
	
	public SecuritySettings() {
	}

	public SecuritySettings(SecuritySettings src) {
		this.downloadLinkAutoPassword = src.downloadLinkAutoPassword;
		this.downloadLinkRequirePassword = src.downloadLinkRequirePassword;
		this.uploadLinkAutoPassword = src.uploadLinkAutoPassword;
		this.uploadLinkRequirePassword = src.uploadLinkRequirePassword;
	}
	
	public Boolean isDownloadLinkAutoPassword() {
		return downloadLinkAutoPassword;
	}

	public void setDownloadLinkAutoPassword(Boolean downloadLinkAutoPassword) {
		this.downloadLinkAutoPassword = downloadLinkAutoPassword;
	}

	public Boolean isDownloadLinkRequirePassword() {
		return downloadLinkRequirePassword;
	}

	public void setDownloadLinkRequirePassword(Boolean downloadLinkRequirePassword) {
		this.downloadLinkRequirePassword = downloadLinkRequirePassword;
	}

	public Boolean isUploadLinkAutoPassword() {
		return uploadLinkAutoPassword;
	}

	public void setUploadLinkAutoPassword(Boolean uploadLinkAutoPassword) {
		this.uploadLinkAutoPassword = uploadLinkAutoPassword;
	}

	public Boolean isUploadLinkRequirePassword() {
		return uploadLinkRequirePassword;
	}

	public void setUploadLinkRequirePassword(Boolean uploadLinkRequirePassword) {
		this.uploadLinkRequirePassword = uploadLinkRequirePassword;
	}
	
	public Object clone() {
		return new SecuritySettings(this);
	}
	
}
