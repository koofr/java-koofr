package net.koofr.api.v2.transfer.upload;

import java.io.Serializable;

import org.apache.http.entity.mime.content.AbstractContentBody;

public interface UploadData extends Serializable {
	public AbstractContentBody getBody();

	public String getName();
}
