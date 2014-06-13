package net.koofr.api.v2.transfer.upload;

import java.io.File;

import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.FileBody;

public class FileUploadData implements UploadData {

	private static final long serialVersionUID = 1L;

	private String filePath, mimeType;

	public FileUploadData(String filePath, String mimeType) {
		this.filePath = filePath;
		this.mimeType = mimeType;
		
	}
	
	public FileUploadData(File file, String mimeType) {
		this(file.getAbsolutePath(), mimeType);
	}

	public FileUploadData(File file) {
		this(file, "application/octet-stream");
	}

	public FileUploadData(String filePath) {
		this(filePath, "application/octet-stream");
	}
	
	@Override
	public AbstractContentBody getBody() {
		File file = new File(filePath);
		return new FileBody(file, mimeType);
	}

	@Override
	public String getName() {
		return new File(filePath).getName();
	}
	
}
