package net.koofr.api.v2.transfer.upload;

import java.io.InputStream;

import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.InputStreamBody;

public class StreamUploadData implements UploadData {
	private static final long serialVersionUID = 1L;
	
	InputStream stream;
	String mimeType, fileName;
	
	public StreamUploadData(InputStream stream, String mimeType, String fileName) {
		this.stream = stream;
		this.mimeType = mimeType;
		this.fileName = fileName;
	}

	@Override
	public AbstractContentBody getBody() {
		return new InputStreamBody(stream, mimeType, fileName);
	}
	
	@Override
	public String getName() {
		return fileName;
	}
}
