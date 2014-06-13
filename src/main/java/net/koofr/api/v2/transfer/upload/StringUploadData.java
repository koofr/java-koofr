package net.koofr.api.v2.transfer.upload;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import org.apache.http.entity.mime.content.AbstractContentBody;
import org.apache.http.entity.mime.content.StringBody;

public class StringUploadData implements UploadData {

	private static final long serialVersionUID = 1L;

	private String data;
	private String contentType;
	private String fileName;

	public StringUploadData(String data, String contentType, String fileName) {
		this.data = data;
		this.contentType = contentType;
		this.fileName = fileName;
	}

	@Override
	public AbstractContentBody getBody() {
		try {
			return new NamedStringBody(data, contentType, fileName);
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	@Override
	public String getName() {
		return new File(fileName).getName();
	}

	public static class NamedStringBody extends StringBody {

		private String fileName;

		public NamedStringBody(String text, String mimeType, String fileName)
				throws UnsupportedEncodingException {
			super(text, mimeType, Charset.defaultCharset());
			this.fileName = fileName;
		}

		@Override
		public String getFilename() {
			return fileName;
		}

	}
}
