package net.koofr.api.v2;

public interface StorageApiExceptionHandler {
  public boolean handle(Exception ex) throws StorageApiException;
}
