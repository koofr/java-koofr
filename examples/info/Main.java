package info;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.koofr.api.auth.Authenticator;
import net.koofr.api.auth.basic.HttpBasicAuthenticator;
import net.koofr.api.auth.oauth2.OAuth2Authenticator;
import net.koofr.api.http.Client;
import net.koofr.api.http.Request;
import net.koofr.api.http.Response;
import net.koofr.api.http.errors.HttpException;
import net.koofr.api.http.errors.HttpException.*;
import net.koofr.api.http.impl.basic.BasicClient;
import net.koofr.api.http.impl.basic.BasicRequest;
import net.koofr.api.json.JsonException;
import net.koofr.api.json.Transmogrifier;
import net.koofr.api.rest.v2.Api;
import net.koofr.api.rest.v2.RMounts.RMount;
import net.koofr.api.rest.v2.RSearch.QueryParameters;
import net.koofr.api.rest.v2.data.ConnectionList;
import net.koofr.api.rest.v2.data.Groups.*;
import net.koofr.api.rest.v2.data.Devices.*;
import net.koofr.api.rest.v2.data.Mounts.*;
import net.koofr.api.rest.v2.data.Files.*;
import net.koofr.api.rest.v2.data.Jobs.*;
import net.koofr.api.rest.v2.data.Mounts;
import net.koofr.api.rest.v2.data.Permissions;
import net.koofr.api.rest.v2.data.SearchResult;
import net.koofr.api.rest.v2.data.Trash;
import net.koofr.api.rest.v2.data.SearchResult.SearchHit;
import net.koofr.api.rest.v2.data.Self;
import net.koofr.api.rest.v2.data.User;
import net.koofr.api.rest.v2.data.Bookmarks.Bookmark;

public class Main {

  private static void cover(Api api) throws IOException, JsonException {
    /*
    Self self = api.self().get();
    Transmogrifier.dumpObject(self); System.out.println();

    Trash t = api.trash().get();
    Transmogrifier.dumpObject(t); System.out.println();
    
    ConnectionList cl = api.self().connections().get();
    Transmogrifier.dumpObject(cl); System.out.println();
    String oldF = self.firstName;
    String oldL = self.lastName;
    api.self().edit("bu", "ba");
    self = api.self().get();
    Transmogrifier.dumpObject(self); System.out.println();
    api.self().edit(oldF, oldL);
    Map<String, Object> attributes = api.self().attributes().get();
    Transmogrifier.dumpObject(attributes); System.out.println();
    Map<String, Object> appConfig = api.self().appConfig().get();
    Transmogrifier.dumpObject(appConfig); System.out.println();
    Transmogrifier.dumpObject(api.self().settings().branding().get()); System.out.println();
    Transmogrifier.dumpObject(api.self().settings().notifications().get()); System.out.println();
    Transmogrifier.dumpObject(api.self().settings().security().get()); System.out.println();
    Transmogrifier.dumpObject(api.self().settings().seen().get()); System.out.println();
    Transmogrifier.dumpObject(api.self().settings().language().get()); System.out.println();
    Transmogrifier.dumpObject(api.self().bookmarks().get()); System.out.println();
    Bookmark b = new Bookmark();
    b.mountId = "686d1449-bd30-4a2a-a255-0579c9b9c605";
    b.path = "/docz/";
    b.name = "DOCZ";
    api.self().bookmarks().create(b);
    Transmogrifier.dumpObject(api.self().bookmarks().get()); System.out.println();
    api.self().bookmarks().delete("686d1449-bd30-4a2a-a255-0579c9b9c605", "/docz/");
    Transmogrifier.dumpObject(api.self().bookmarks().get()); System.out.println();
    Transmogrifier.dumpObject(api.groups().get()); System.out.println();
    System.out.println();
    Permissions permissions = new Permissions();
    permissions.put(Permissions.P.READ.name(), true);
    permissions.put(Permissions.P.WRITE.name(), true);
    GroupUser gu = api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").users()
        .add("monitor@koofr.net", permissions, 1024L);
    Transmogrifier.dumpObject(gu); System.out.println();
    System.out.println();
    Transmogrifier.dumpObject(api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").get()); System.out.println();
    System.out.println();
    api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").users().user(gu.id).delete();
    Transmogrifier.dumpObject(api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").get()); System.out.println();
    System.out.println();
    Transmogrifier.dumpObject(api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").get()); System.out.println();
    System.out.println();
    Transmogrifier.dumpObject(api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").attributes().get()); System.out.println();
    System.out.println();
    GroupBranding br = new GroupBranding();
    br.backgroundColor = "#abcdef";
    br.foregroundColor = "#fedcba";
    api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").branding().edit(br);
    api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").common().edit(1024L);
    Transmogrifier.dumpObject(api.groups().group("f287b54e-8da5-4215-8688-d7a6dfa8d733").get()); System.out.println();
    System.out.println();
    Transmogrifier.dumpObject(api.devices().get()); System.out.println();
    System.out.println();
    Device d = api.devices().create("DB3", "storagehub");
    Transmogrifier.dumpObject(d); System.out.println();
    System.out.println();
    Transmogrifier.dumpObject(api.devices().device(d.id).get()); System.out.println();
    System.out.println();
    api.devices().device(d.id).setSearchEnabled(false);
    Transmogrifier.dumpObject(api.devices().device(d.id).get()); System.out.println();
    System.out.println();
    api.devices().device(d.id).edit("DB4");
    Transmogrifier.dumpObject(api.devices().device(d.id).get()); System.out.println();
    System.out.println();
    api.devices().device(d.id).delete();
    try {
      Transmogrifier.dumpObject(api.devices().device(d.id).get()); System.out.println();
    } catch(NotFound ex) {      
    }

    Mounts mounts = api.mounts().get();
    Transmogrifier.dumpObject(mounts); System.out.println();
    System.out.println();
    String mid = null;
    for(Mount m: mounts.mounts) {
      if(m.isPrimary) {
        mid = m.id;
        break;
      }
    }
    if(mid != null) {
      RMount rm = api.mounts().mount(mid);

      Transmogrifier.dumpObject(api.mounts().mount(mid).get()); System.out.println();
      System.out.println();
      api.mounts().mount(mid).edit("New name");
      Transmogrifier.dumpObject(api.mounts().mount(mid).get()); System.out.println();
      System.out.println();
      Mount sm;
      sm = null;
      for(Mount m: mounts.mounts) {
        if(m.name.equals("Test submount")) {
          sm = m;
          break;
        }
      }
      if(sm == null) {
        sm = api.mounts().mount(mid).createSubmount("Test submount", "/Test");
      }
      Transmogrifier.dumpObject(sm); System.out.println();
      System.out.println();
      Transmogrifier.dumpObject(api.mounts().get()); System.out.println();
      System.out.println();
      MountUser mu = api.mounts().mount(sm.id).users().add(null, "monitor@koofr.net", new Permissions());
      Transmogrifier.dumpObject(mu); System.out.println();
      System.out.println();
      Permissions p = new Permissions();
      p.put(Permissions.P.WRITE.toString(), true);
      api.mounts().mount(sm.id).users().user(mu.id).edit(p);
      Transmogrifier.dumpObject(api.mounts().mount(sm.id).get()); System.out.println();
      System.out.println();
      api.mounts().mount(sm.id).users().user(mu.id).delete();
      Transmogrifier.dumpObject(api.mounts().mount(sm.id).get()); System.out.println();
      System.out.println();
      api.mounts().mount(sm.id).delete();
      Transmogrifier.dumpObject(api.mounts().get()); System.out.println();
      System.out.println();

      Transmogrifier.dumpObject(rm.bundle("/Test")); System.out.println();
      System.out.println();
      Transmogrifier.dumpObject(rm.files().list("/Test")); System.out.println();
      System.out.println();
      Transmogrifier.dumpObject(rm.files().info("/Test")); System.out.println();
      System.out.println();
      Transmogrifier.dumpObject(rm.files().tree("/Test")); System.out.println();
      System.out.println();
      Transmogrifier.dumpObject(rm.files().versions().get("/Test")); System.out.println();
      System.out.println();

      rm.files().createFolder("/Test", "Folder");
      rm.files().rename("/Test/Folder", "Folder2");
      System.out.print(rm.files().getDownloadUrl("/Test/Folder2")); System.out.println();
      System.out.println();
      System.out.print(rm.files().getUploadUrl("/Test/Folder2")); System.out.println();
      System.out.println();
      rm.files().copy("/Test/Folder2", mid, "/Test/Folder3");
      rm.files().move("/Test/Folder3", mid, "/Test/Folder4");
      Map<String, List<String>> tags = new HashMap<>();
      List<String> l = new ArrayList<>();
      l.add("A"); l.add("B");
      tags.put("Tag1", l);
      l = new ArrayList<>();
      l.add("C");
      tags.put("Tag2", l);
      rm.files().tags().add("/Test/Folder4", tags);
      Transmogrifier.dumpObject(rm.files().info("/Test/Folder4")); System.out.println();
      System.out.println();
      rm.files().tags().remove("/Test/Folder4", tags);
      Transmogrifier.dumpObject(rm.files().info("/Test/Folder4")); System.out.println();
      System.out.println();

      File fl = new File("./test.jpg");
      InputStream is = new FileInputStream(fl);
      UploadOptions uo = new UploadOptions();
      uo.callback = new Request.TransferCallback() {
        public void progress(Long c, Long t) {
          System.out.print("\r    " + c + "/" + t + "               ");
        }
      };
      
      Transmogrifier.dumpObject(rm.files().upload("/Test/Folder2", "test.jpg", "image/jpeg", fl.length(), is, uo));
      System.out.println();
      is.close();
      DownloadResult dl = rm.files().download("/Test/Folder2/test.jpg");
      System.out.println("Content type: " + dl.contentType);
      System.out.println("Content length: " + dl.contentLength);
      Files.deleteIfExists(FileSystems.getDefault().getPath("dl.bin"));
      Files.copy(dl.downloadStream, FileSystems.getDefault().getPath("dl.bin"));
      dl.close();
      byte[] b1 = Files.readAllBytes(FileSystems.getDefault().getPath("test.jpg"));
      byte[] b2 = Files.readAllBytes(FileSystems.getDefault().getPath("dl.bin"));
      System.out.println("Uploaded and downloaded equals original: " + Arrays.equals(b1,  b2));
      rm.files().delete("/Test/Folder2/test.jpg", null);
      
      dl = api.self().profilePicture().get();
      Files.deleteIfExists(FileSystems.getDefault().getPath("pp.bin"));
      Files.copy(dl.downloadStream, FileSystems.getDefault().getPath("pp.bin"));
      dl.close();
      is = new FileInputStream("./test.jpg");
      api.self().profilePicture().update(is, "picture.jpg", "image/jpeg");
      
      dl = api.users().user("a1ca8c20-e82c-4169-91b0-92980e1736f9").profilePicture().get();
      Files.deleteIfExists(FileSystems.getDefault().getPath("pp2.bin"));
      Files.copy(dl.downloadStream, FileSystems.getDefault().getPath("pp2.bin"));
      dl.close();
      
      JobCopyFiles cj = new JobCopyFiles();
      cj.files = new ArrayList<JobMountPathPair>();
      JobMountPathPair pp = new JobMountPathPair();
      pp.dst = new JobMountPath();
      pp.src = new JobMountPath();
      pp.src.mountId = mid;
      pp.src.path = "/Test/Folder2";
      pp.dst.mountId = mid;
      pp.dst.path = "/Test/Folder4/CopyOfFolder2";
      cj.files.add(pp);
      Job job = api.jobs().files().copy(cj.files);
      Transmogrifier.dumpObject(job);
      
      while(!job.state.equals("done") && !job.state.equals("failed")) {
        job = api.jobs().job(job.id).get();        
        try { Thread.sleep(1*1000L); } catch(InterruptedException ex) {}
      }
      
      rm.files().delete("/Test/Folder2", null);
      rm.files().delete("/Test/Folder4", null);
      
    }

    */
  
    QueryParameters qp = new QueryParameters();
    qp.query = "IMG";
    qp.limit = 1024;
    SearchResult sr = api.search().query(qp);
    for(SearchHit h: sr.hits) {
      System.out.println(h.mountId + ":" + h.path);
    }
  }
  
  public static void main(String[] args) throws Exception {
    Client c = new BasicClient();
    c.setRequestDecorator(new Client.RequestDecorator<BasicRequest>() {
      @Override
      public BasicRequest decorate(BasicRequest r) {
        return r;
      }
    });
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
    Self s = api.self().get();
    System.out.println("You are " + s.firstName + " " + s.lastName + " at " + s.email);
    cover(api);
  }
}
