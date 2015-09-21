package net.koofr.api.v2.transfer.upload;

public final class UploadOptions {
  public boolean overwrite;
  public String overwriteIfHash;
  public long overwriteIfSize;
  public long overwriteIfModified;
  
  public UploadOptions() {
    overwrite = false;
    overwriteIfHash = null;
    overwriteIfSize = overwriteIfModified = -1L;
  }
}
