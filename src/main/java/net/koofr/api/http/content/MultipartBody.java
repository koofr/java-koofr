package net.koofr.api.http.content;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.SequenceInputStream;
import java.util.UUID;
import java.util.Vector;

import net.koofr.api.http.Body;

public class MultipartBody implements Body {

  public static final String LF = "\r\n";
  
  final String boundary;
  String partFileName;
  String partContentType;
  InputStream partInputStream;
  
  public MultipartBody(String filename, String contentType, InputStream content) {
    boundary = "===" + UUID.randomUUID().toString() + "===";
    partFileName = filename;
    partContentType = contentType;
    partInputStream = content;
  }
  
  @Override
  public String getContentType() {
    return "multipart/form-data; boundary=" + boundary;
  }
  
  @Override
  public Long getContentLength() {
    return null;
  }
  
  @Override
  public InputStream getInputStream() throws IOException {
    Vector<InputStream> iss = new Vector<>();
    StringBuilder b = new StringBuilder("--").append(boundary).append(LF)
      .append("Content-Disposition: form-data; name=\"file\"; filename=\"")
      .append(partFileName).append("\"").append(LF)
      .append("Content-Type: ").append(partContentType).append(LF)
      .append("Content-Transfer-Encoding: binary").append(LF)
      .append(LF);
    iss.add(new ByteArrayInputStream(b.toString().getBytes("UTF-8")));
    iss.add(partInputStream);
    b = new StringBuilder(LF)
      .append(LF)
      .append("--").append(boundary).append("--").append(LF);
      iss.add(new ByteArrayInputStream(b.toString().getBytes("UTF-8")));
    return new SequenceInputStream(iss.elements());
  }
  
}
