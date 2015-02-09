package net.koofr.api.v2;

import org.restlet.resource.ResourceException;

public class StorageApiException extends Exception {

	private static final long serialVersionUID = 1L;

	public StorageApiException() {
		super();
	}

	public StorageApiException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public StorageApiException(String detailMessage) {
		super(detailMessage);
	}

	public StorageApiException(Throwable throwable) {
		super(throwable);
	}

  public boolean is404() {
    return (getCause() instanceof ResourceException) &&
        (((ResourceException)getCause()).getStatus().getCode() == 404);
  }

}
