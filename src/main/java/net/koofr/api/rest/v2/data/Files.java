package net.koofr.api.rest.v2.data;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import net.koofr.api.http.Request.TransferCallback;
import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Common.StringList;

public class Files implements JsonBase {

  public static class File implements JsonBase {
    public String name;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public String hash;
    public Map<String, StringList> tags;
    public List<File> children;
  }
  
  public static class Version implements JsonBase {
    public String id;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public Map<String, StringList> tags;    
  }
  
  public static class Versions implements JsonBase {
    public List<Version> versions;
  }
  
  public static class UploadOptions {
    public Long modified;
    public Long size;
    public String hash;
    public Boolean ignoreNonExisting;
    public Boolean noRename;
    public Boolean forceOverwrite;
    public TransferCallback callback;
  }
  
  public static class DeleteOptions {
    public Long modified;
    public Long size;
    public String hash;
    public Boolean ifEmpty;   
  }
  
  public static class Upload implements JsonBase {
    public String name;
    public String type;
  }
  
  public static class UploadResult extends ArrayList<Upload> {
    private static final long serialVersionUID = 1L;
  }
  
  public static class DownloadResult {
    public String contentType;
    public Long contentLength;
    public InputStream downloadStream;
    
    public void close() {
      try {
        downloadStream.close();
      } catch(IOException ex) {
      }
    }
  }
  
  public List<File> files;

}
