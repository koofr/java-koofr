package net.koofr.api.v2;

public interface StorageApiExceptionHandler {
	public void handle(Exception ex) throws StorageApiException;
}
