package net.koofr.api.rest.v2;

import java.io.IOException;
import java.util.ArrayList;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.SearchResult;

public class RSearch extends Resource {

  public RSearch(Api parent) {
    super(parent, "/search");
  }
  
  public static class QueryParameters {
    public String query;
    public Integer offset;
    public Integer limit;
    public String sortField;
    public String sortDir;
    public String mimeType;
    public String mountId;
    public String path;
    
    public String[] toParameterArray() {
      ArrayList<String> params = new ArrayList<String>();
      if(query != null) {
        params.add("query");
        params.add(query);
      }
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
        params.add("mime");
        params.add(mimeType);
      }
      if(mountId != null) {
        params.add("mountId");
        params.add(mountId);
        if(path != null) {
          params.add("path");
          params.add(path);
        }
      }
      String[] rv = new String[params.size()];
      params.toArray(rv);
      return rv;
    }
  }
  
  public SearchResult query(QueryParameters p) throws IOException, JsonException {
    return getResult(SearchResult.class, p.toParameterArray());
  }
  
}
