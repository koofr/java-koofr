package cmdline;

import java.io.FileInputStream;
import java.io.IOException;
import java.lang.Override;
import java.lang.StringBuilder;
import java.lang.System;
import java.nio.file.FileSystems;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.List;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.auth.basic.HttpBasicAuthenticator;
import net.koofr.api.auth.oauth2.OAuth2Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request.TransferCallback;
import net.koofr.api.http.impl.basic.BasicClient;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.rest.v2.Api;
import net.koofr.api.rest.v2.RSearch.QueryParameters;
import net.koofr.api.rest.v2.data.Files;
import net.koofr.api.rest.v2.data.Files.DownloadResult;
import net.koofr.api.rest.v2.data.Files.File;
import net.koofr.api.rest.v2.data.Files.UploadOptions;
import net.koofr.api.rest.v2.data.Files.UploadResult;
import net.koofr.api.rest.v2.data.Mounts;
import net.koofr.api.rest.v2.data.Mounts.Mount;
import net.koofr.api.rest.v2.data.SearchResult;
import net.koofr.api.rest.v2.data.SearchResult.SearchHit;

public class Main {
  public static void main(String[] args) throws Exception {
    Client c = new BasicClient();
    Authenticator a = null;
    if(args.length == 5) {
      a = new OAuth2Authenticator(c, args[1], args[2], args[3], "urn:ietf:wg:oauth:2.0:oob");
      ((OAuth2Authenticator)a).setGrantCode(args[4]);
    } else if(args.length == 3) {
      a = new HttpBasicAuthenticator(args[1], args[2]);
    } else {
      System.err.println("Usage: info <server> [<token-url> <client-id> <client-secret> <grant-code>|<username> <password>]");
      System.exit(42);
    }
    Api api = new Api("https://" + args[0] + "/api/v2", a, c);

    new Example(api).run();
  }
}

class Example implements Runnable {
  private final Api api;

  private String mountId = null;
  private String mount = null;
  private String path = "/";
  private Scanner sc = new Scanner(System.in);

  public Example(Api api) {
      this.api = api;
  }

  private String prompt() {
      String status = "";
      if (mount != null) {
          status += mount + " " + path;
      }
      status += " $ ";
      System.out.print(status);
      return sc.next();
  }

  private void printHelp() {
      System.out.println("supported commands: 'help' 'exit' 'mounts' 'mount mount-id' 'ls' 'cd' 'mkdir name' 'rm name' 'download name' 'upload localpath' 'search terms' 'attributes'");
  }

  private void mounts() throws IOException, JsonException {
      Mounts mounts = api.mounts().get();
      for (Mount m : mounts.mounts) {
          System.out.println(m.id + " " + m.name);
      }
      System.out.println();
  }

  private void mount() throws IOException, JsonException {
      String mountId = sc.next();
      Mount m = api.mounts().mount(mountId).get();
      this.mount = m.name;
      this.mountId = m.id;
  }

  private void ls() throws IOException, JsonException {
      Files files = api.mounts().mount(this.mountId).files().list(this.path);
      for(File f : files.files) {
          System.out.printf("%4s %12d %s\n", f.type, f.size, f.name);
      }
      System.out.println();
  }

  private void cd() throws IOException, JsonException {
      String dirname = sc.nextLine().trim().toLowerCase(); //for doing case-insensitive comparison
      if (dirname.equals("..")) {
          String[] parts = this.path.split("/");
          StringBuilder sb = new StringBuilder("/");
          for(int i = 1, up = parts.length-1; i < up; i++) {
              sb.append(parts[i]);
              sb.append("/");
          }
          this.path = sb.toString();
          return;
      }

      Files files = api.mounts().mount(this.mountId).files().list(this.path);
      for(File f : files.files) {
          if (f.type.equals("dir") && f.name.toLowerCase().equals(dirname)) {
              this.path = (this.path + "/" + f.name).replaceAll("/+", "/");
              return;
          }
      }
      System.out.println("No such directory");
  }

  private void mkdir() throws IOException, JsonException {
      String dirname = sc.nextLine().trim();
      api.mounts().mount(this.mountId).files().createFolder(this.path, dirname);
  }

  private void rm() throws IOException, JsonException {
      String name = sc.nextLine().trim();
      api.mounts().mount(this.mountId).files().delete(this.path + "/" + name, null);
  }

  private void download() throws IOException, JsonException {
      String name = sc.nextLine().trim();
      System.out.print("enter target directory: ");
      String dirname = sc.nextLine().trim();
      String path = (this.path + "/" + name).replaceAll("/+", "/");
      DownloadResult dl = api.mounts().mount(this.mountId).files().download(path);
      java.nio.file.Files.copy(dl.downloadStream, FileSystems.getDefault().getPath(dirname, name));
      dl.close();
      System.out.println("\n");
  }

  private void upload() throws IOException, JsonException {
      String localPath = sc.nextLine().trim();
      UploadOptions options = new UploadOptions();
      options.forceOverwrite = true;
      options.callback = new SimpleProgressListener();
      String[] parts = localPath.split("/");
      String name = parts[parts.length - 1];
      java.io.File fl = new java.io.File(localPath);
      FileInputStream is = new FileInputStream(fl);
      UploadResult ul = api.mounts().mount(this.mountId).files().upload(this.path, name, "application/octet-stream", fl.length(), is, options);
  }

  private void search() throws IOException, JsonException {
    String query = sc.nextLine().trim();
    QueryParameters p = new QueryParameters();
    p.query = query;
    SearchResult result = api.search().query(p);
    for(SearchHit hit: result.hits) {
      System.out.println(hit.mountId + ":" + hit.path);
    }
    System.out.println("\n");    
  }
  
  private void attributes() throws IOException, JsonException {
    Map<String, Object> attributes = api.self().attributes().get();
    for(Object key: attributes.keySet()) {
      System.out.println(key + ": " + attributes.get(key));
    }
  }
  
  public void run() {
    System.out.println("First pick a mount. Use 'help' for help.");
    while(true) {
      try {
        switch (prompt()) {
        case "exit":
          System.exit(0);
          break;
        case "help":
          printHelp();
          break;
        case "mounts":
          mounts();
          break;
        case "mount":
          mount();
          break;
        case "ls":
          ls();
          break;
        case "cd":
          cd();
          break;
        case "mkdir":
          mkdir();
          break;
        case "rm":
          rm();
          break;
        case "download":
          download();
          break;
        case "upload":
          upload();
          break;
        case "search":
          search();
          break;
        case "attributes":
          attributes();
          break;
        default:
          printHelp();
        }
      } catch (Exception e) {
        System.out.println("error:" + e);
        e.printStackTrace(System.out);
      }
    }
  }
}

class SimpleProgressListener implements TransferCallback  {
  public void progress(Long transferred, Long total) {
    System.out.print("Progress: " + transferred + "/" + total + " bytes\r");
  }
};
