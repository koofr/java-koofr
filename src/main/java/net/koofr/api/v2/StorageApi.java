package net.koofr.api.v2;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.UUID;

import net.koofr.api.v2.resources.Bookmark;
import net.koofr.api.v2.resources.Comment;
import net.koofr.api.v2.resources.ConnectionList;
import net.koofr.api.v2.resources.Device;
import net.koofr.api.v2.resources.File;
import net.koofr.api.v2.resources.Group;
import net.koofr.api.v2.resources.Hit;
import net.koofr.api.v2.resources.JsonBase;
import net.koofr.api.v2.resources.Link;
import net.koofr.api.v2.resources.Mount;
import net.koofr.api.v2.resources.NotificationSettings;
import net.koofr.api.v2.resources.PathInfo;
import net.koofr.api.v2.resources.Permissions;
import net.koofr.api.v2.resources.SecuritySettings;
import net.koofr.api.v2.resources.User;
import net.koofr.api.v2.resources.UserInfo;
import net.koofr.api.v2.transfer.ProgressListener;
import net.koofr.api.v2.transfer.upload.MultipartEntityProgress;
import net.koofr.api.v2.transfer.upload.UploadData;
import net.koofr.api.v2.transfer.upload.UploadOptions;
import net.koofr.api.v2.util.TokenClientResource;
import net.koofr.api.v2.util.Https;
import net.koofr.api.v2.util.HttpsClientHelper;
import net.koofr.api.v2.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.BasicClientConnectionManager;
import org.apache.http.impl.conn.SingleClientConnManager;
import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.restlet.Client;
import org.restlet.Context;
import org.restlet.Request;
import org.restlet.data.ChallengeResponse;
import org.restlet.data.ChallengeScheme;
import org.restlet.data.MediaType;
import org.restlet.data.Protocol;
import org.restlet.data.Reference;
import org.restlet.data.Header;
import org.restlet.engine.Engine;
import org.restlet.engine.header.HeaderConstants;
import org.restlet.engine.security.AuthenticatorHelper;
import org.restlet.ext.jackson.JacksonConverter;
import org.restlet.resource.ClientResource;
import org.restlet.resource.ResourceException;
import org.restlet.util.Series;
import org.restlet.ext.oauth.AccessTokenClientResource;
import org.restlet.ext.oauth.GrantType;
import org.restlet.ext.oauth.OAuthParameters;
import org.restlet.ext.oauth.internal.Token;

/* TODO: (re)enable HTTP Basic and Koofr token authentication methods */

@SuppressWarnings({"unused", "deprecation"})
public abstract class StorageApi {
  public static final String TAG = StorageApi.class.getName();

  public static final int DOWNLOAD_BUFFER_SIZE = 64*1024;

  protected String baseUrl;
  protected Client client;
  protected Log log;
  protected StorageApiExceptionHandler xhandler;
  
  public void setLog(Log log) {
    this.log = log;
  }
  
  private void debug(String tag, String msg) {
    if(null != log) {
      log.debug(tag, msg);
    }
  }

  private void debug(String tag, String msg, Throwable t) {
    if(null != log) {
      log.debug(tag, msg, t);
    }
  } 

  public StorageApi(String baseUrl, Client client) {
    this.baseUrl = baseUrl;
    this.client = client;
    this.log = null;
    this.xhandler = null;
  }

  public void setExceptionHandler(StorageApiExceptionHandler h) {
    xhandler = h;
  }
  
  protected void fireExceptionHandler(Exception ex) throws StorageApiException {
    if(xhandler != null) {
      if(xhandler.handle(ex)) {
        throw new StorageApiException(ex);
      }
    }
  }
  
  protected abstract void prepareRequest() throws StorageApiException ;
  protected abstract ClientResource createResource(Reference ref) throws StorageApiException ;
  protected abstract void prepareHttpMessage(AbstractHttpMessage m) throws StorageApiException ;
  
  protected ClientResource getResource(String path) throws StorageApiException {
    debug(TAG, "Resource path: " + path);
    prepareRequest();
    Reference ref = new Reference(new Reference(baseUrl), path);
    ClientResource res = createResource(ref);
    res.setNext(client);
    return res;
  }

  protected ClientResource getResource(String path, String... query) throws StorageApiException {
    debug(TAG, "Resource path: " + path);
    prepareRequest();
    Reference ref = new Reference(new Reference(baseUrl), path);
    for(int i = 0; i < query.length - 1; i += 2) {
      ref = ref.addQueryParameter(query[i], query[i + 1]);
    }
    ClientResource res = createResource(ref);
    res.setNext(client);
    return res;
  }
  
  protected ClientResource getResource(Reference ref) throws StorageApiException {
    debug(TAG, "Resource path: " + ref.getPath());
    prepareRequest();    
    ClientResource res = createResource(ref);
    res.setNext(client);
    return res;
  }

  private HttpClient getHttpClient() {
    SchemeRegistry schemeRegistry = new SchemeRegistry();
    schemeRegistry.register(new Scheme("https", Https.getFactory(), 443));
    schemeRegistry.register(new Scheme("http", PlainSocketFactory
        .getSocketFactory(), 80));

    HttpParams params = new BasicHttpParams();

    ClientConnectionManager mgr =
        new SingleClientConnManager(params, schemeRegistry);

    HttpClient client = new DefaultHttpClient(mgr, params);

    return client;
  }

  public UserInfo getUserInfo() throws StorageApiException {
    try {
      return getResource("/api/v2/user").get(UserInfo.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public ConnectionList getUserConnections() throws StorageApiException {
    try {
      return getResource("/api/v2/user/connections").get(ConnectionList.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  private static class UserUpdate extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private String firstName, lastName;
    
    public String getFirstName() {
      return firstName;
    }
    public void setFirstName(String firstName) {
      this.firstName = firstName;
    }   
    public String getLastName() {
      return lastName;
    }
    public void setLastName(String lastName) {
      this.lastName = lastName;
    }
  }
  
  public void updateUserInfo(String firstName, String lastName)
      throws StorageApiException {
    UserUpdate uu = new UserUpdate();
    uu.setFirstName(firstName);
    uu.setLastName(lastName);
    
    try {
      getResource("/api/v2/user").put(uu);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  private static class UserPasswordUpdate extends JsonBase
      implements Serializable {
    private static final long serialVersionUID = 1L;

    private String newPassword;
    private String oldPassword;
    
    public String getNewPassword() {
      return newPassword;
    }
    public void setNewPassword(String newPassword) {
      this.newPassword = newPassword;
    }
    public String getOldPassword() {
      return oldPassword;
    }
    public void setOldPassword(String oldPassword) {
      this.oldPassword = oldPassword;
    }
  }
  
  public void updateUserPassword(String newPassword, String oldPassword)
      throws StorageApiException {
    UserPasswordUpdate upu = new UserPasswordUpdate();
    upu.setNewPassword(newPassword);
    upu.setOldPassword(oldPassword);
    
    try {
      getResource("/api/v2/user/password").put(upu);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public NotificationSettings getNotificationSettings()
      throws StorageApiException {
    try {
      return getResource("/api/v2/user/settings/notifications").
          get(NotificationSettings.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  public void updateNotificationSettings(NotificationSettings s)
      throws StorageApiException {
    try {
      getResource("/api/v2/user/settings/notifications").put(s);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  /* TODO: missing settings/branding API */
  
  public SecuritySettings getSecuritySettings()
      throws StorageApiException {
    try {
      return getResource("/api/v2/user/settings/security").
          get(SecuritySettings.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public void updateSecuritySettings(SecuritySettings s)
      throws StorageApiException {
    try {
      getResource("/api/v2/user/settings/security").put(s);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  private static class BookmarkList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Bookmark> bookmarks;
    
    public List<Bookmark> getBookmarks() {
      return bookmarks;
    }
    public void setBookmarks(List<Bookmark> bookmarks) {
      this.bookmarks = bookmarks;
    }
  }
  
  public List<Bookmark> getBookmarks() throws StorageApiException {
    try {
      BookmarkList rv = getResource("/api/v2/user/bookmarks").
          get(BookmarkList.class);
      if(null != rv) {
        return rv.getBookmarks();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public void putBookmarks(List<Bookmark> bookmarks)
      throws StorageApiException {
    BookmarkList rv = new BookmarkList();
    rv.setBookmarks(bookmarks);
    
    try {
      getResource("/api/v2/user/bookmarks").put(rv);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public void deleteBookmark(String mountId, String path) throws StorageApiException {
    try {
      getResource("/api/v2/user/bookmarks", "mountId", mountId, "path", path).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public void addBookmark(String mountId, String path, String name) throws StorageApiException {
    Bookmark b = new Bookmark();
    b.setMountId(mountId);
    b.setPath(path);
    b.setName(name);
    try {
      getResource("/api/v2/user/bookmarks").post(b);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public InputStream getProfilePicture(String userId)
      throws StorageApiException {
    try {
      return getResource("/content/api/v2/users/" + userId + "/profile-picture")
          .get(MediaType.IMAGE_ALL).getStream();
    }
    catch(Exception ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  } 

  private static class NameRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;

    private String name;
    
    public String getName() {
      return name;
    }   
    public void setName(String name) {
      this.name = name;
    }
  }
  
  public Group newGroup(String name) throws StorageApiException {
    NameRequest r = new NameRequest();
    r.setName(name);
    
    try {
      return getResource("/api/v2/groups").post(r, Group.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  private static class GroupList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Group> groups;
    
    public List<Group> getGroups() {
      return groups;
    }
    
    public void setGroups(List<Group> groups) {
      this.groups = groups;
    }   
  }
  
  public List<Group> getGroups() throws StorageApiException {
    try {
      GroupList rv = getResource("/api/v2/groups").get(GroupList.class);
      if(null != rv) {
        return rv.getGroups();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public Group getGroup(String id) throws StorageApiException {
    try {
      return getResource("/api/v2/groups/" + id).get(Group.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public void updateGroup(String id, String name) throws StorageApiException {
    NameRequest r = new NameRequest();
    r.setName(name);
    try {
      getResource("/api/v2/groups/" + id).put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  private static class AddUserRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String email;
    private Permissions permissions;
    
    public String getEmail() {
      return email;
    }
    public void setEmail(String email) {
      this.email = email;
    }
    public Permissions getPermissions() {
      return permissions;
    }
    public void setPermissions(Permissions permissions) {
      this.permissions = permissions;
    }
  }
  
  public User addUserToGroup(String groupId, String email, Permissions permissions)
      throws StorageApiException {
    AddUserRequest r = new AddUserRequest();
    r.setEmail(email);
    r.setPermissions(permissions);
    
    try {
      return getResource("/api/v2/groups/" + groupId + "/users").
          post(r, User.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  private static class UpdatePermissionsRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Permissions permissions;
    
    public Permissions getPermissions() {
      return permissions;
    }
    public void setPermissions(Permissions permissions) {
      this.permissions = permissions;
    }
  }
  
  public void updateGroupUser(String groupId, String userId, Permissions perm)
      throws StorageApiException {    
    UpdatePermissionsRequest r = new UpdatePermissionsRequest();
    
    r.setPermissions(perm);
    try {
      getResource("/api/v2/groups/" + groupId + "/users/" + userId).put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public void deleteGroupUser(String groupId, String userId)
      throws StorageApiException {
    try {
      getResource("/api/v2/groups/" + groupId + "/users/" + userId).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public void deleteGroup(String groupId) throws StorageApiException {
    try {
      getResource("/api/v2/groups/" + groupId).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  private static class DeviceList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Device> devices;
    
    public List<Device> getDevices() {
      return devices;
    }   
    public void setDevices(List<Device> devices) {
      this.devices = devices;
    }
  }
  
  public List<Device> getDevices() throws StorageApiException {
    try {
      DeviceList rv = getResource("/api/v2/devices").get(DeviceList.class);
      if(null != rv) {
        return rv.getDevices();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  private static class DeviceCreateRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String name;
    private String providerName;
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getProviderName() {
      return providerName;
    }
    public void setProviderName(String providerName) {
      this.providerName = providerName;
    }
  }
  
  public Device createDevice(String name, String providerName)
      throws StorageApiException {
    DeviceCreateRequest r = new DeviceCreateRequest();
    r.setName(name);
    r.setProviderName(providerName);
    
    try {
      return getResource("/api/v2/devices").post(r, Device.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public Device getDevice(String deviceId) throws StorageApiException {
    try {
      return getResource("/api/v2/devices/" + deviceId).get(Device.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public void updateDevice(String deviceId, String newName)
      throws StorageApiException {
    NameRequest r = new NameRequest();
    r.setName(newName);
    try {
      getResource("/api/v2/devices/" + deviceId).put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public void deleteDevice(String deviceId) throws StorageApiException {
    try {
      getResource("/api/v2/devices/" + deviceId).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  /* TODO: missing setting of provider block for cloud devices */
  
  private static class MountList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Mount> mounts;
    
    public List<Mount> getMounts() {
      return mounts;
    }
    
    public void setMounts(List<Mount> mounts) {
      this.mounts = mounts;
    }
  }

  public List<Mount> getMounts() throws StorageApiException {
    try {
      MountList rv = getResource("/api/v2/mounts").get(MountList.class);
      if(null != rv) {
        return rv.getMounts();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public Mount getMount(String id) throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + id).get(Mount.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  private static class CreateMountRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String path;
    private String name;
    
    public String getName() {
      return name;
    }
    public void setName(String name) {
      this.name = name;
    }
    public String getPath() {
      return path;
    }
    public void setPath(String path) {
      this.path = path;
    }
  }
  
  public Mount createMount(String parentId, String path, String name)
      throws StorageApiException {
    CreateMountRequest r = new CreateMountRequest();
    r.setName(name);
    r.setPath(path);
    try {
      return getResource("/api/v2/mounts/" + parentId + "/submounts").
          post(r, Mount.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public void updateMount(String id, String newName)
      throws StorageApiException {
    NameRequest r = new NameRequest();
    r.setName(newName);
    try {
      getResource("/api/v2/mounts/" + id).put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }

  public User addUserToMount(String mountId, String email, Permissions perm)
      throws StorageApiException {
    AddUserRequest r = new AddUserRequest();
    r.setEmail(email);
    r.setPermissions(perm);
    try {
      return getResource("/api/v2/mounts/" + mountId + "/users").
          post(r, User.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public void updateMountUser(String mountId, String userId, Permissions perm)
    throws StorageApiException {
    UpdatePermissionsRequest r = new UpdatePermissionsRequest();
    r.setPermissions(perm);
    try {
      getResource("/api/v2/mounts/" + mountId + "/users/" + userId).
          put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public void deleteMountUser(String mountId, String userId)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/users/" + userId).
          delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }

  public void updateMountGroup(String mountId, String groupId, Permissions perm)
      throws StorageApiException {
    UpdatePermissionsRequest r = new UpdatePermissionsRequest();
    r.setPermissions(perm);
    try {
      getResource("/api/v2/mounts/" + mountId + "/groups/" + groupId).
          put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public void deleteMountGroup(String mountId, String groupId)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/groups/" + groupId).
          delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  public void deleteMount(String mountId) throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }

  public void createFolder(String mountId, String path, String folderName)
      throws StorageApiException {
    NameRequest r;
    r = new NameRequest();
    r.setName(folderName);
    try {
      getResource("/api/v2/mounts/" + mountId + "/files/folder",
          "path", path).post(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  private static class FileLink extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String link;
    
    public String getLink() {
      return link;
    }   
    public void setLink(String link) {
      this.link = link;
    }
  }
  
  public String getUploadURL(String mountId, String path)
      throws StorageApiException {
    try {
      FileLink fl = getResource("/api/v2/mounts/" + mountId +
          "/files/upload", "path", path).get(FileLink.class);
      if(null != fl) {
        return fl.getLink();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public String getDownloadURL(String mountId, String path)
      throws StorageApiException {
    try {
      FileLink fl = getResource("/api/v2/mounts/" + mountId +
          "/files/download", "path", path).get(FileLink.class);
      if(null != fl) {
        return fl.getLink();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  private static class FileList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<File> files;
    
    public List<File> getFiles() {
      return files;
    }
    public void setFiles(List<File> files) {
      this.files = files;
    }
  }
  
  public List<File> listFiles(String mountId, String path)
      throws StorageApiException {
    try {
      FileList fl = getResource("/api/v2/mounts/" + mountId +
          "/files/list", "path", path).get(FileList.class);
      if(null != fl) {
        return fl.getFiles();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public PathInfo getPathInfo(String mountId, String path)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId +
          "/bundle", "path", path).get(PathInfo.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public File getFileInfo(String mountId, String path) throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/files/info",
          "path", path).get(File.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  public void renamePath(String mountId, String path, String newName)
      throws StorageApiException {
    NameRequest r = new NameRequest();
    r.setName(newName);
    try {
      getResource("/api/v2/mounts/" + mountId + "/files/rename",
          "path", path).put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  public void removePath(String mountId, String path)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/files/remove",
          "path", path).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  private static class DestinationRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String toMountId;
    private String toPath;
    
    public String getToMountId() {
      return toMountId;
    }
    public void setToMountId(String toMountId) {
      this.toMountId = toMountId;
    }
    public String getToPath() {
      return toPath;
    }
    public void setToPath(String toPath) {
      this.toPath = toPath;
    }
  }
  
  public String copyPath(String srcMountId, String srcPath,
      String dstMountId, String dstPath) throws StorageApiException {
    try {
      DestinationRequest r = new DestinationRequest();
      r.setToMountId(dstMountId);
      r.setToPath(dstPath);
      NameRequest rv = getResource("/api/v2/mounts/" + srcMountId + "/files/copy",
          "path", srcPath).put(r, NameRequest.class);
      if(null != rv) {
        return rv.getName();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }

  public String movePath(String srcMountId, String srcPath,
      String dstMountId, String dstPath) throws StorageApiException {
    try {
      DestinationRequest r = new DestinationRequest();
      r.setToMountId(dstMountId);
      r.setToPath(dstPath);
      NameRequest rv = getResource("/api/v2/mounts/" + srcMountId + "/files/move",
          "path", srcPath).put(r, NameRequest.class);
      if(null != rv) {
        return rv.getName();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  private static class PostCommentRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String content;
    
    public String getContent() {
      return content;
    }
    public void setContent(String content) {
      this.content = content;
    }
  }
  
  public Comment postComment(String mountId, String content)
      throws StorageApiException {
    PostCommentRequest r = new PostCommentRequest();    
    r.setContent(content);
    try {
      return getResource("/api/v2/mounts/" + mountId + "/comments").
          post(r, Comment.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  private static class CommentList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Comment> comments;
    
    public List<Comment> getComments() {
      return comments;
    }
    public void setComments(List<Comment> comments) {
      this.comments = comments;
    }
  }
  
  public List<Comment> getComments(String mountId)
      throws StorageApiException {
    return getComments(mountId, -1, -1);
  }
  
  public List<Comment> getComments(String mountId, int from, int limit)
      throws StorageApiException {
    ClientResource res;
    if(from >= 0 && limit > 0) {
      res = getResource("/api/v2/mounts/" + mountId + "/comments",
          "from", String.valueOf(from),
          "limit", String.valueOf(limit));
    }
    else {
      res = getResource("/api/v2/mounts/" + mountId + "/comments");
    }
    try {
      CommentList cl = res.get(CommentList.class);
      return cl.getComments();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  public Comment getComment(String mountId, String commentId)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/comments/" +
          commentId).get(Comment.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }

  public void deleteComment(String mountId, String commentId)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/comments/" +
          commentId).delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }   
  }
  
  private static class PathRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String path;
    
    public String getPath() {
      return path;
    }
    public void setPath(String path) {
      this.path = path;
    }
  }
  
  private static class LinkList extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private List<Link> links;
    
    public List<Link> getLinks() {
      return links;
    }
    public void setLinks(List<Link> links) {
      this.links = links;
    }
  }
  
  public Link createReceiver(String mountId, String path)
      throws StorageApiException {
    PathRequest r = new PathRequest();
    r.setPath(path);
    try {
      return getResource("/api/v2/mounts/" + mountId + "/receivers").
          post(r, Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  public List<Link> getReceivers(String mountId)
      throws StorageApiException {
    try {
      LinkList ll = getResource("/api/v2/mounts/" + mountId).
          get(LinkList.class);
      if(null != ll) {
        return ll.getLinks();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  public Link getReceiver(String mountId, String linkId)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/receivers/" +
          linkId).get(Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  public Link getReceiverForPath(String mountId, String path)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/receivers",
          "path", path).get(Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public List<Link> getReceiversInParent(String mountId, String parent)
      throws StorageApiException {
    try {
      LinkList ll = getResource("/api/v2/mounts/" + mountId + "/receivers",
          "parent", parent).get(LinkList.class);
      if(null != ll) {
        return ll.getLinks();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }

  private static class HashRequest extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private String hash;
    
    public String getHash() {
      return hash;
    }
    public void setHash(String hash) {
      this.hash = hash;
    }
  }
  
  public void setReceiverURLPath(String mountId, String linkId, String path)
      throws StorageApiException {
    HashRequest r = new HashRequest();
    r.setHash(path);
    
    try {
      getResource("/api/v2/mounts/" + mountId + "/receivers/" +
          linkId + "/urlHash").put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public Link resetReceiverPassword(String mountId, String linkId)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/receivers/" +
          linkId + "/password/reset").put(null, Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public void deleteReceiverPassword(String mountId, String linkId)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/receivers/" +
          linkId + "/password").delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public void deleteReceiver(String mountId, String linkId)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/receivers/" + linkId).
          delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }

  public Link createLink(String mountId, String path)
      throws StorageApiException {
    PathRequest r = new PathRequest();
    r.setPath(path);
    try {
      return getResource("/api/v2/mounts/" + mountId + "/links").
          post(r, Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  public List<Link> getLinks(String mountId)
      throws StorageApiException {
    try {
      LinkList ll = getResource("/api/v2/mounts/" + mountId).
          get(LinkList.class);
      if(null != ll) {
        return ll.getLinks();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }

  public Link getLink(String mountId, String linkId) 
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/links/" +
          linkId).get(Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }       
  }
  
  public Link getLinkForPath(String mountId, String path)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/links",
          "path", path).get(Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public List<Link> getLinksInParent(String mountId, String parent)
      throws StorageApiException {
    try {
      LinkList ll = getResource("/api/v2/mounts/" + mountId + "/links",
          "parent", parent).get(LinkList.class);
      if(null != ll) {
        return ll.getLinks();
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }

  public void setLinkURLPath(String mountId, String linkId, String path)
      throws StorageApiException {
    HashRequest r = new HashRequest();
    r.setHash(path);
    
    try {
      getResource("/api/v2/mounts/" + mountId + "/links/" +
          linkId + "/urlHash").put(r);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public Link resetLinkPassword(String mountId, String linkId)
      throws StorageApiException {
    try {
      return getResource("/api/v2/mounts/" + mountId + "/links/" +
          linkId + "/password/reset").put(null, Link.class);
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public void deleteLinkPassword(String mountId, String linkId)
      throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/links/" +
          linkId + "/password").delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public void deleteLink(String mountId, String linkId) throws StorageApiException {
    try {
      getResource("/api/v2/mounts/" + mountId + "/links/" + linkId).
          delete();
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }           
  }
  
  public static class QueryResults extends JsonBase implements Serializable {
    private static final long serialVersionUID = 1L;
    
    List<Hit> hits;
    Hashtable<String, Mount> mounts;
    
    public QueryResults() {
    }

    public List<Hit> getHits() {
      return hits;
    }

    public void setHits(List<Hit> hits) {
      this.hits = hits;
    }

    public Hashtable<String, Mount> getMounts() {
      return mounts;
    }

    public void setMounts(Hashtable<String, Mount> mounts) {
      this.mounts = mounts;
    }
  }
  
  /*
  GET /api/v2/search controllers.api.v2.Search.search(query: Option[String] ?= None, offset: Int ?= 0, limit: Int ?= 256, sortField ?= "", sortDir ?= "", mime ?= "", mountId ?= "", path ?= "")
  */

  private String[] searchUrlParameters(String query, Integer offset, Integer limit, String mimeType, String sortField, String sortDir, String mountId, String path) {
    ArrayList<String> params = new ArrayList<String>();
    
    if(offset != null) {
      params.add("offset");
      params.add(offset.toString());
    }
    if(limit != null) {
      params.add("limit");
      params.add(limit.toString());
    }
    if(sortField != null) {
      params.add("sortField");
      params.add(sortField);
    }
    if(sortDir != null) {
      params.add("sortDir");
      params.add(sortDir);
    }
    if(mimeType != null) {
      params.add("mimeType");
      params.add(mimeType);
    }
    if(query != null) {
      params.add("query");
      params.add(query);
    }
    if(mountId != null) {
      params.add("mountId");
      params.add(mountId);
    }
    if(path != null) {
      params.add("path");
      params.add(path);
    }
    
    String[] rv = new String[params.size()];
    return params.toArray(rv);
  }
  
  public List<Hit> search(String query, Integer offset, Integer limit, String mimeType, String sortField, String sortDir, String mountId, String path) throws StorageApiException {
    try {
      QueryResults r = getResource("/api/v2/search", searchUrlParameters(query, offset, limit, mimeType, sortField, sortDir, mountId, path)).get(QueryResults.class);
      if(null != r) {
        List<Hit> rv = new ArrayList<Hit>();
        for(Hit h: r.hits) {
          if(r.mounts.containsKey(h.getMountId())) {
            h.setMount(r.mounts.get(h.getMountId()));
            rv.add(h);
          }
        }
        return rv;
      }
      else {
        return null;
      }
    }
    catch(ResourceException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }               
  }

  public List<Hit> search(String query) throws StorageApiException {
    return search(query, null, null, null, null, null, null, null);
  }
  
  public List<Hit> search(String query, String mountId, String path) throws StorageApiException {
    return search(query, null, null, null, null, null, mountId, path);
  }

  public boolean checkAuthentication() throws StorageApiException {
    try {
      return getUserInfo() != null;
    } catch (Exception e) {
      return false;
    }
  }

  public int filesUpload(String mountId, String path, UploadData uploadData,
      ProgressListener listener) throws StorageApiException {
    return filesUpload(mountId, path, uploadData, listener, null);
  }
  
  public int filesUpload(String mountId, String path, UploadData uploadData,
      ProgressListener listener, UploadOptions options) throws StorageApiException {
    try {
      prepareRequest();
      String uploadUrl = baseUrl + "/content/api/v2/mounts/" + mountId + "/files/put";      
      try {
        uploadUrl = uploadUrl +
          "?path=" + URLEncoder.encode(path, "UTF-8") +
          "&filename=" + URLEncoder.encode(uploadData.getName(), "UTF-8");
        if(options != null) {
          if(options.overwrite) {
            uploadUrl += "&overwrite=true";
          }
          if(options.overwriteIfSize >= 0) {
            uploadUrl += "&overwriteIfSize=" + options.overwriteIfSize;
          }
          if(options.overwriteIfModified >= 0) {
            uploadUrl += "&overwriteIfSize=" + options.overwriteIfModified;
          }
          if(options.overwriteIfHash != null) {
            uploadUrl += "&overwriteIfHash=" + URLEncoder.encode(options.overwriteIfHash);
          }
        }
      }
      catch(UnsupportedEncodingException ex) {
        throw new StorageApiException(ex);
      }

      HttpPost upload = new HttpPost(uploadUrl);
      prepareHttpMessage(upload);

      MultipartEntityProgress form = new MultipartEntityProgress(
          HttpMultipartMode.BROWSER_COMPATIBLE, listener);
      form.addPart("file", uploadData.getBody());
      
      listener.setTotal(form.getContentLength());
      
      upload.setEntity(form);
      
      HttpClient client = getHttpClient();

      try {
        HttpResponse resp = client.execute(upload);
        return resp.getStatusLine().getStatusCode();
      } catch (Exception e) {
        fireExceptionHandler(e);
        throw new StorageApiException(e);
      }
    } catch (ResourceException e) {
      fireExceptionHandler(e);
      throw new StorageApiException(e);
    }
  }

  public String filesDownload(String mountId, String path,
      String downloadLocation, ProgressListener listener)
      throws StorageApiException {
    return filesDownload(mountId, path, downloadLocation, listener, null);
  }

  public String filesDownload(String mountId, String path,
      String downloadLocation, ProgressListener listener,
      String thumbSize) throws StorageApiException {
    return filesDownload(mountId, path, downloadLocation, listener, thumbSize, null);
  }

  public String filesDownload(String mountId, String path,
      String downloadLocation, ProgressListener listener,
      String thumbSize, String destName) throws StorageApiException {
    String[] parts = path.split("/");
    String filePath;
    BufferedOutputStream outStream = null;
    FileOutputStream fileStream = null;
    InputStream inStream = null;
    
    if (destName == null) {
      destName = parts[parts.length - 1];
      String ext = "";
      if (path.endsWith("/")) {
        ext = ".zip";
      } else {
        int pos = destName.lastIndexOf('.');
        if(pos >= 0) {
          ext = destName.substring(pos).toLowerCase();
          destName = destName.substring(0, pos);
        }
        else {
          ext = "";
        }
      }
      filePath = downloadLocation + java.io.File.separator + destName + ext;
      java.io.File outFile = new java.io.File(filePath);
      for (int i = 1; outFile.exists(); i++) {
        filePath = downloadLocation + java.io.File.separator + destName + "(" + i + ")"
            + ext;
        outFile = new java.io.File(filePath);
      }
    }
    else {
      filePath = downloadLocation + java.io.File.separator + destName;
    }

    try {
      inStream = filesDownload(mountId, path, listener, thumbSize);   
      fileStream = new FileOutputStream(filePath);
      outStream = new BufferedOutputStream(fileStream,
          DOWNLOAD_BUFFER_SIZE);
  
      byte[] data = new byte[DOWNLOAD_BUFFER_SIZE];
      int bytesRead = 0;
      long totalRead = 0;
  
      while (!listener.isCanceled()
          && (bytesRead = inStream.read(data, 0, data.length)) >= 0) {
        outStream.write(data, 0, bytesRead);
        totalRead += bytesRead;
        listener.transferred(totalRead);
      }
  
      if (listener.isCanceled()) {
        new java.io.File(filePath).delete();
        return null;
      } else {
        return filePath;
      }
    }
    catch(IOException ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
    finally {
      if(outStream != null)
        try { outStream.close(); } catch(Exception e) {}
      if(fileStream != null)
        try { fileStream.close(); } catch(Exception e) {}   
      if(inStream != null)
        try { inStream.close(); } catch(Exception e) {} 
    }   
  }

  public InputStream filesDownload(String mountId, String path,
      ProgressListener listener, String thumbSize) throws StorageApiException {
    try {
      prepareRequest();
      
      BufferedInputStream inStream;     

      String downloadUrl = baseUrl + "/content/api/v2/mounts/" + mountId + "/files/get?path=";      
      try {
        downloadUrl = downloadUrl + URLEncoder.encode(path, "UTF-8");
      }
      catch(UnsupportedEncodingException ex) {
        downloadUrl = downloadUrl + path;
      }

      if(thumbSize != null) {
        downloadUrl += "&thumb=" + thumbSize;         
      }

      final HttpGet request = new HttpGet(downloadUrl);
      prepareHttpMessage(request);

      HttpClient client = getHttpClient();

      try {
        HttpResponse resp = client.execute(request);

        if (resp.getStatusLine().getStatusCode() != 200) {          
          resp.getEntity().consumeContent();
          return null;
        }

        HttpEntity entity = resp.getEntity();
        listener.setTotal(entity.getContentLength());

        inStream = new BufferedInputStream(entity.getContent(), DOWNLOAD_BUFFER_SIZE) {
          @Override
          public void close() throws IOException {
            request.abort();
            super.close();
          }
        };

        return inStream;
      } catch (Exception e) {
        fireExceptionHandler(e);
        throw new StorageApiException(e);
      }
    } catch (ResourceException e) {
      fireExceptionHandler(e);
      throw new StorageApiException(e);
    }
  }
  
  public String getMyIp() throws StorageApiException {
    try {
      Reference ref = new Reference(baseUrl + "/ip");
      ClientResource res = createResource(ref);   
      res.setNext(client);
      return res.get().getText();
    } catch(IOException e) {
      fireExceptionHandler(e);
      throw new StorageApiException(e);
    }
  }
  
  @SuppressWarnings("rawtypes")
  public HashMap getAttributes() throws StorageApiException {
    try {
      return getResource("/api/v2/user/attributes").get(HashMap.class);
    }
    catch(Exception ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
  @SuppressWarnings("rawtypes")
  public HashMap getAppConfig() throws StorageApiException {
    try {
      return getResource("/api/v2/user/appconfig").get(HashMap.class);
    }
    catch(Exception ex) {
      fireExceptionHandler(ex);
      throw new StorageApiException(ex);
    }
  }
  
}
