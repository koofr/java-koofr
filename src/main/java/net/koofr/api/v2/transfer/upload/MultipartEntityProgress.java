package net.koofr.api.v2.transfer.upload;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;

import net.koofr.api.v2.transfer.ProgressListener;

import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;

public class MultipartEntityProgress extends MultipartEntity {

	private ProgressListener listener;

	public MultipartEntityProgress(ProgressListener listener) {
		super();
		this.listener = listener;
	}

	public MultipartEntityProgress(HttpMultipartMode mode, String boundary,
			Charset charset, ProgressListener listener) {
		super(mode, boundary, charset);
		this.listener = listener;
	}

	public MultipartEntityProgress(HttpMultipartMode mode,
			ProgressListener listener) {
		super(mode);
		this.listener = listener;
	}

	@Override
	public void writeTo(OutputStream outstream) throws IOException {
		super.writeTo(new CountingOutputStream(outstream, this.listener));
	}

	public static class CountingOutputStream extends FilterOutputStream {

		private ProgressListener listener;
		private long transferred = 0;

		public CountingOutputStream(OutputStream out, ProgressListener listener) {
			super(out);
			this.listener = listener;
		}

		@Override
		public void write(byte[] buffer, int offset, int length)
				throws IOException {
			super.write(buffer, offset, length);

			this.transferred += length;
			this.listener.transferred(this.transferred);
		}

		@Override
		public void write(byte[] buffer) throws IOException {
			super.write(buffer);
			this.transferred += buffer.length;
			this.listener.transferred(this.transferred);
		}

		@Override
		public void write(int oneByte) throws IOException {
			super.write(oneByte);
			this.transferred++;
			this.listener.transferred(this.transferred);
		}
	}

}
