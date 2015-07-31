package cmdline;

import net.koofr.api.v2.StorageApi;
import net.koofr.api.v2.DefaultClientFactory;
import net.koofr.api.v2.StorageApiException;
import net.koofr.api.v2.resources.Hit;
import net.koofr.api.v2.resources.Mount;
import net.koofr.api.v2.resources.File;
import net.koofr.api.v2.transfer.ProgressListener;
import net.koofr.api.v2.transfer.upload.FileUploadData;
import net.koofr.api.v2.transfer.upload.UploadData;

import java.lang.Override;
import java.lang.StringBuilder;
import java.lang.System;
import java.util.HashMap;
import java.util.Scanner;
import java.util.List;

public class Main {
    public static void main(String[] args) throws StorageApiException {
        StorageApi api = null;
        if(args.length == 2) { //using token
            String host = args[0];
            String token = args[1];
            api = DefaultClientFactory.createToken(host, token);
        } else if (args.length == 3) { // using username/pass
            String host = args[0];
            String username = args[1];
            String password = args[2];
            api = DefaultClientFactory.createToken(host, username, password);
        } else {
            System.out.println("Pass in hostname and auth token or username/pass");
            System.exit(1);
        }

        new Example(api).run();
    }
}

class Example implements Runnable{
    private final StorageApi api;

    private String mountId = null;
    private String mount = null;
    private String path = "/";
    private Scanner sc = new Scanner(System.in);

    public Example(StorageApi api) {
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

    private void mounts() throws StorageApiException {
        List<Mount> mounts = api.getMounts();
        for (Mount m : mounts) {
            System.out.println(m.getId() + " " + m.getName());
        }
        System.out.println();
    }

    private void mount() throws  StorageApiException {
        String mountId = sc.next();
        Mount m = api.getMount(mountId);
        this.mount = m.getName();
        this.mountId = m.getId();
    }

    private void ls() throws StorageApiException {
        List<File> files = api.listFiles(this.mountId, this.path);
        for(File f : files) {
            System.out.printf("%4s %12d %s\n", f.getType(), f.getSize(), f.getName());
        }
        System.out.println();
    }

    private void cd() throws StorageApiException {
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

        List<File> files = api.listFiles(this.mountId, this.path);
        for(File f : files) {
            if (f.getType().equals("dir") && f.getName().toLowerCase().equals(dirname)) {
                this.path = (this.path + "/" + f.getName()).replaceAll("/+", "/");
                return;
            }
        }
        System.out.println("No such directory");
    }

    private void mkdir() throws  StorageApiException {
        String dirname = sc.nextLine().trim();
        api.createFolder(this.mountId, this.path, dirname);
    }

    private void rm() throws StorageApiException {
        String name = sc.nextLine().trim();
        api.removePath(this.mountId, this.path + "/" + name);
    }

    private void download() throws StorageApiException {
        String name = sc.nextLine().trim();
        System.out.print("enter target directory: ");
        String dirname = sc.nextLine().trim();
        String path = (this.path + "/" + name).replaceAll("/+", "/");
        api.filesDownload(this.mountId, path, dirname, new SimpleProgressListener());
        System.out.println("\n");
    }

    private void upload() throws StorageApiException {
        String localPath = sc.nextLine().trim();
        UploadData data = new FileUploadData(localPath);
        api.filesUpload(this.mountId, this.path, data, new SimpleProgressListener());
        System.out.println("\n");
    }

    private void search() throws StorageApiException {
      String query = sc.nextLine().trim();
      List<Hit> hits = api.search(query);
      if(hits != null) {
        for(Hit h: hits) {
          System.out.println(h.getMount().getName() + ":" + h.getPath());
        }
      } else {
        System.out.println("No hits.");
      }
        System.out.println("\n");    
    }
    
    private void attributes() throws StorageApiException {
      HashMap attributes = api.getAttributes();
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
            } catch (StorageApiException e) {
                System.out.println("error:" + e);
            }
        }
    }
}

class SimpleProgressListener implements ProgressListener  {
    private long total = 0;

    public void transferred(long bytes) {
        setTotal(total + bytes);
    }

    public void setTotal(long bytes) {
        this.total = bytes;
        System.out.print("Progress: " + bytes + " bytes\r");
    }

    public boolean isCanceled() {
        return false;
    }
};
