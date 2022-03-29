package net.koofr.api.http.content;

import net.koofr.api.http.Body;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.UUID;
import java.util.Vector;

public class MultipartBody implements Body {

  public static final String LF = "\r\n";
  
  final String boundary;
  String partFileName;
  String partContentType;
  InputStream partInputStream, multipartStream;
  Long partSize;
  Long contentLength;
  
  public MultipartBody(String filename, String contentType, Long size, InputStream content) throws IOException {
    boundary = "JavaKoofrApi3-" + UUID.randomUUID().toString();
    partFileName = filename;
    partContentType = contentType;
    partInputStream = content;
    partSize = size;
    
    Vector<InputStream> iss = new Vector<InputStream>();
    long overhead;
    byte[] bytes = new StringBuilder("--").append(boundary).append(LF)
      .append("Content-Disposition: form-data; name=\"file\"; filename=\"")
      .append(partFileName).append("\"").append(LF)
      .append("Content-Type: ").append(partContentType).append(LF)
      .append(LF)
      .toString().getBytes("UTF-8");
    overhead = bytes.length;
    iss.add(new ByteArrayInputStream(bytes));
    iss.add(partInputStream);
    bytes = new StringBuilder(LF)
      .append("--").append(boundary).append("--").append(LF)
      .toString().getBytes("UTF-8");
    iss.add(new ByteArrayInputStream(bytes));
    overhead += bytes.length;
    multipartStream = new SequenceInputStream(iss.elements());
    
    if(partSize != null) {
      contentLength = overhead + partSize;
    } else {
      contentLength = null;
    }
  }
  
  public MultipartBody(String filename, String contentType, InputStream content) throws IOException {
    this(filename, contentType, null, content);
  }
  
  @Override
  public String getContentType() {
    return "multipart/form-data; boundary=" + boundary;
  }
  
  @Override
  public Long getContentLength() {
    return contentLength;
  }
  
  @Override
  public InputStream getInputStream() throws IOException {
    return multipartStream;
  }
  
}
