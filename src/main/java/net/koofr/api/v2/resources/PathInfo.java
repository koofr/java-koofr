package net.koofr.api.v2.resources;

import java.io.Serializable;
import java.util.List;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class PathInfo extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private List<File> files;
	private File file;
	
	public List<File> getFiles() {
		return files;
	}
	
	public void setFiles(List<File> files) {
		this.files = files;
	}
	
	public File getFile() {
		return file;
	}
	
	public void setFile(File file) {
		this.file = file;
	}
}
