package net.koofr.api.rest.v2;

import net.koofr.api.http.Response;
import net.koofr.api.http.content.MultipartBody;
import net.koofr.api.json.JsonBase;
import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.RMounts.RMount;
import net.koofr.api.rest.v2.data.Files;
import net.koofr.api.rest.v2.data.Files.DeleteOptions;
import net.koofr.api.rest.v2.data.Files.DownloadOptions;
import net.koofr.api.rest.v2.data.Files.DownloadResult;
import net.koofr.api.rest.v2.data.Files.File;
import net.koofr.api.rest.v2.data.Files.UploadOptions;
import net.koofr.api.rest.v2.data.Files.Versions;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class RFiles extends Resource {

  public RFiles(RMount parent) {
    super(parent, "/files");
  }
  
  private static class RFilesList extends Resource {
    public RFilesList(RFiles parent) {
      super(parent, "/list");
    }
    
    public Files get(String path) throws IOException, JsonException {
      return getResult(Files.class, "path", path);
    }
  }
  
  public Files list(String path) throws IOException, JsonException {
    return new RFilesList(this).get(path);
  }

  private static class RFilesInfo extends Resource {
    public RFilesInfo(RFiles parent) {
      super(parent, "/info");
    }
    
    public File get(String path) throws IOException, JsonException {
      return getResult(File.class, "path", path);
    }
  }
  
  public File info(String path) throws IOException, JsonException {
    return new RFilesInfo(this).get(path);
  }

  private static class RFilesTree extends Resource {
    public RFilesTree(RFiles parent) {
      super(parent, "/tree");
    }
    
    public File get(String path) throws IOException, JsonException {
      return getResult(File.class, "path", path);
    }
  }
  
  public File tree(String path) throws IOException, JsonException {
    return new RFilesTree(this).get(path);
  }

  public static class VersionsRecover implements JsonBase {
    public String newPath;
  }    
  
  private static class RFilesVersionsRecover extends Resource {
    public RFilesVersionsRecover(RFilesVersions parent) {
      super(parent, "/recover");
    }
    
    public String get(String path, String version) throws IOException, JsonException {
      VersionsRecover r = postJsonResult(VersionsRecover.class, "path", path, "version", version);
      return r.newPath;
    }
  }
  
  public static class RFilesVersions extends Resource {
    public RFilesVersions(RFiles parent) {
      super(parent, "/versions");
    }
    
    public Versions get(String path) throws IOException, JsonException {
      return getResult(Versions.class, "path", path);
    }
    
    public String recover(String path, String version) throws IOException, JsonException {
      return new RFilesVersionsRecover(this).get(path, version);
    }
  }

  public RFilesVersions versions() {
    return new RFilesVersions(this);
  }
  
  public static class FilesFolderCreate implements JsonBase {
    public String name; 
  }
  
  private static class RFilesFolder extends Resource {
    public RFilesFolder(RFiles parent) {
      super(parent, "/folder");
    }
    
    public void create(String path, String name) throws IOException, JsonException {
      FilesFolderCreate c = new FilesFolderCreate();
      c.name = name;
      postJsonNoResult(c, "path", path);
    }
  }
  
  public void createFolder(String path, String name) throws IOException, JsonException {
    new RFilesFolder(this).create(path, name);
  }

  public static class DeleteResult implements JsonBase {
    public String version;
  }

  private static class RFilesRemove extends Resource {
    public RFilesRemove(RFiles parent) {
      super(parent, "/remove");
    }
    
    public DeleteResult delete(String path, DeleteOptions options) throws IOException, JsonException  {
      ArrayList<String> params = new ArrayList<String>();
      params.add("path"); params.add(path);
      if(options != null) {
        if(options.size != null) {        
          params.add("removeIfSize"); params.add(options.size.toString());
        }
        if(options.modified != null) {
          params.add("removeIfModified"); params.add(options.modified.toString());
        }
        if(options.hash != null) {
          params.add("removeIfHash"); params.add(options.hash);
        }
        if(options.ifEmpty != null) {
          params.add("removeIfEmpty"); params.add(options.ifEmpty.toString());        
        }
      }
      String[] p = new String[params.size()];
      params.toArray(p);
      return deleteResult(DeleteResult.class, p);
    }
  }

  public DeleteResult delete(String path, DeleteOptions options) throws IOException, JsonException {
    return new RFilesRemove(this).delete(path, options);
  }
  
  public static class FilesRename implements JsonBase {
    public String name;
  }
  
  private static class RFilesRename extends Resource {
    public RFilesRename(RFiles parent) {
      super(parent, "/rename");
    }
    
    public void rename(String path, String newName) throws IOException, JsonException {
      FilesRename r = new FilesRename();
      r.name = newName;
      putJsonNoResult(r, "path", path);
    }
  }
  
  public void rename(String path, String newName) throws IOException, JsonException {
    new RFilesRename(this).rename(path, newName);
  }
  
  public static class FilesCopyMove implements JsonBase {
    public String toMountId;
    public String toPath;
  }
  
  public static class FilesCopyMoveResult implements JsonBase {
    public String name;
  }
  
  private static class RFilesCopyMove extends Resource {
    public RFilesCopyMove(RFiles parent, String path) {
      super(parent, path);
    }
    
    public String execute(String path, String dstMountId, String dstPath) throws IOException, JsonException {
      FilesCopyMove c = new FilesCopyMove();
      c.toMountId = dstMountId;
      c.toPath = dstPath;
      return putJsonResult(c, FilesCopyMoveResult.class, "path", path).name;
    }
  }
  
  public String copy(String path, String dstMountId, String dstPath) throws IOException, JsonException {
    return new RFilesCopyMove(this, "/copy").execute(path, dstMountId, dstPath);
  }

  public String move(String path, String dstMountId, String dstPath) throws IOException, JsonException {
    return new RFilesCopyMove(this, "/move").execute(path, dstMountId, dstPath);
  }

  public static class FilesTags implements JsonBase {
    public Map<String, List<String>> tags;
  }
  
  private static class RTagsAction extends Resource {
    public RTagsAction(RTags parent, String path) {
      super(parent, path);
    }
    
    public void execute(String path, Map<String, List<String>> tags) throws IOException, JsonException {
      FilesTags t = new FilesTags();
      t.tags = tags;
      postJsonNoResult(t, "path", path);
    } 
  }
  
  public static class RTags extends Resource {
    public RTags(RFiles parent) {
      super(parent, "/tags");
    }
    
    public void add(String path, Map<String, List<String>> tags) throws IOException, JsonException {
      new RTagsAction(this, "/add").execute(path, tags);
    }

    public void remove(String path, Map<String, List<String>> tags) throws IOException, JsonException {
      new RTagsAction(this, "/remove").execute(path, tags);
    }
  }
  
  public RTags tags() {
    return new RTags(this);
  }
  
  public static class FilesLink implements JsonBase {
    public String link;
  }
  
  private static class RFilesLink extends Resource {
    public RFilesLink(RFiles parent, String path) {
      super(parent, path);
    }
    
    public String get(String path) throws IOException, JsonException {
      return getResult(FilesLink.class, "path", path).link;
    }
  }
  
  public String getDownloadUrl(String path) throws IOException, JsonException {
    return new RFilesLink(this, "/download").get(path);
  }
  
  public String getUploadUrl(String path) throws IOException, JsonException {
    return new RFilesLink(this, "/upload").get(path);
  }
  
  private static class RFilesContent extends Resource {
    public RFilesContent(RFiles parent, String path) {
      super(parent, path);      
      url = contentUrl(url);      
    }
    
    public Response get(String path, DownloadOptions options) throws IOException {
      ArrayList<String> params = new ArrayList<String>();
      params.add("path"); params.add(path);
      if(options != null) {
        if(options.thumbnailSize != null) {
          params.add("thumb"); params.add(options.thumbnailSize);
        }
        if(options.convertFormat != null) {
          params.add("convert"); params.add(options.convertFormat);
        }
      }
      String[] p = new String[params.size()];
      params.toArray(p);            
      return httpGet(p);
    }
    
    public File put(String path, String name, String contentType, Long contentSize, InputStream content, UploadOptions options) throws IOException, JsonException {
      MultipartBody body = new MultipartBody("dummyfilename", contentType, contentSize, content);
      ArrayList<String> params = new ArrayList<String>();
      params.add("path"); params.add(path);
      params.add("info"); params.add("true");
      params.add("filename"); params.add(name);
      if(options != null) {
        if(options.overwriteIfSize != null) {        
          params.add("overwriteIfSize"); params.add(options.overwriteIfSize.toString());
        }
        if(options.overwriteIfModified != null) {
          params.add("overwriteIfModified"); params.add(options.overwriteIfModified.toString());
        }
        if(options.overwriteIfHash != null) {
          params.add("overwriteIfHash"); params.add(options.overwriteIfHash);
        }
        if(options.ignoreNonExisting != null) {
          params.add("overwriteIgnoreNonexisting"); params.add(options.ignoreNonExisting.toString());        
        }
        if(options.noRename != null) {
          params.add("autorename"); params.add(String.valueOf(!options.noRename));        
        }
        if(options.forceOverwrite != null) {
          params.add("overwrite"); params.add(options.forceOverwrite.toString());        
        }
        if(options.setModified != null) {
          params.add("modified"); params.add(options.setModified.toString());
        }
      }      
      String[] p = new String[params.size()];
      params.toArray(p);
      return resolveJsonResult(httpPost(body, options.callback, p), File.class);
    }
  }
  
  public DownloadResult download(String path, DownloadOptions options) throws IOException {
    Response r = new RFilesContent(this, "/get").get(path, options);
    return resolveDownload(r);
  }
  
  public File upload(String path, String name, String contentType, Long contentSize, InputStream content, UploadOptions options) throws IOException, JsonException {
    return new RFilesContent(this, "/put").put(path, name, contentType, contentSize, content, options);
  }
  
  /* TODO: template creation, link local, discover local */
}
