package net.koofr.api.v2;

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

}
