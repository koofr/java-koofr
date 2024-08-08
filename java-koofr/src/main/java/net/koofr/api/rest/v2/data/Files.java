package net.koofr.api.rest.v2.data;

import net.koofr.api.http.Request.TransferCallback;
import net.koofr.api.http.Response;
import net.koofr.api.json.JsonBase;
import net.koofr.api.rest.v2.data.Common.StringList;
import net.koofr.api.util.U;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class Files implements JsonBase, Serializable {
  private static final long serialVersionUID = 1L;

  public static class File implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public static final String TYPE_DIR  = "dir";
    public static final String TYPE_FILE = "file";

    public String name;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public String hash;
    public Map<String, StringList> tags;
    public List<File> children;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof File)) {
        return false;
      }
      File o = (File)obj;
      return U.safeEq(name, o.name) &&
        U.safeEq(type, o.type) &&
        U.safeEq(modified, o.modified) &&
        U.safeEq(size, o.size) &&
        U.safeEq(contentType, o.contentType) &&
        U.safeEq(hash, o.hash) &&
        U.safeEq(tags, o.tags) &&
        U.safeEq(children, o.children);
    }
  }
  
  public static class Version implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String id;
    public String type;
    public Long modified;
    public Long size;
    public String contentType;
    public Map<String, StringList> tags;    

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Version)) {
        return false;
      }
      Version o = (Version)obj;
      return U.safeEq(id, o.id) &&
        U.safeEq(type, o.type) &&
        U.safeEq(modified, o.modified) &&
        U.safeEq(size, o.size) &&
        U.safeEq(contentType, o.contentType) &&
        U.safeEq(tags, o.tags);
    }
  }
  
  public static class Versions implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public List<Version> versions;

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof Versions)) {
        return false;
      }
      Versions o = (Versions)obj;
      return U.safeEq(versions, o.versions);
    }
  }
  
  public static class VersionRecover implements JsonBase, Serializable {
    private static final long serialVersionUID = 1L;

    public String newPath;    

    @Override
    public boolean equals(Object obj) {
      if(obj == null || !(obj instanceof VersionRecover)) {
        return false;
      }
      VersionRecover o = (VersionRecover)obj;
      return U.safeEq(newPath, o.newPath);
    }
  }
  
  public static class UploadOptions {
    public Long overwriteIfModified;
    public Long overwriteIfSize;
    public String overwriteIfHash;
    public Boolean ignoreNonExisting;
    public Boolean noRename;
    public Boolean forceOverwrite;
    public Long setModified;
    public TransferCallback callback;
  }
  
  public static class DownloadOptions {
    public static final String THUMBNAIL_SIZE_SMALL    = "small";
    public static final String THUMBNAIL_SIZE_SHMEDIUM = "shmedium";
    public static final String THUMBNAIL_SIZE_MEDIUM   = "medium";
    public static final String THUMBNAIL_SIZE_LARGE    = "large";
    public static final String THUMBNAIL_SIZE_HUGE     = "huge";
    
    public String thumbnailSize;
    public String convertFormat;
  }
  
  public static class DeleteOptions {
    public Long modified;
    public Long size;
    public String hash;
    public Boolean ifEmpty;   
  }
  
  public static class DownloadResult {
    private Response r;
    
    public String contentType;
    public Long contentLength;
    public InputStream downloadStream;
    
    public DownloadResult(Response r) {
      this.r = r;
    }
    
    public void close() {
      try {
        downloadStream.close();
        r.close();
      } catch(IOException ex) {
      }
    }
  }
  
  public List<File> files;

  @Override
  public boolean equals(Object obj) {
    if(obj == null || !(obj instanceof Files)) {
      return false;
    }
    Files o = (Files)obj;
    return U.safeEq(files, o.files);
  }

}
