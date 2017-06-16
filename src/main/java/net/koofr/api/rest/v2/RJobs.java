package net.koofr.api.rest.v2;

import java.io.IOException;
import java.util.List;

import net.koofr.api.json.JsonException;
import net.koofr.api.rest.v2.data.Jobs;
import net.koofr.api.rest.v2.data.Jobs.*;

public class RJobs extends Resource {
  public RJobs(Api parent) {
    super(parent, "/jobs");
  }
  
  public Jobs get() throws IOException, JsonException {
    return getResult(Jobs.class);
  }
  
  public static class RJob extends Resource {
    public RJob(RJobs parent, String id) {
      super(parent, "/" + id);
    }
    
    public Job get() throws IOException, JsonException {
      return getResult(Job.class);
    }
    
    public void delete() throws IOException, JsonException {
      deleteNoResult();
    }
  }
  
  public RJob job(String id) {
    return new RJob(this, id);
  }
  
  private static class RJobsFilesCreate extends Resource {
    public RJobsFilesCreate(RJobsFiles parent, String path) {
      super(parent, path);
    }
    
    public Job execute(JobCreate c) throws IOException, JsonException {
      return postJsonResult(c, Job.class);
    }
  }
  
  public static class RJobsFiles extends Resource {
    public RJobsFiles(RJobs parent) {
      super(parent, "/files");
    }
    
    public Job remove(List<JobMountPath> files) throws IOException, JsonException {
      JobRemoveFiles r = new JobRemoveFiles();
      r.files = files;
      return new RJobsFilesCreate(this, "/remove").execute(r);
    }

    public Job copy(List<JobMountPathPair> files, String resolution) throws IOException, JsonException {
      JobCopyFiles r = new JobCopyFiles();
      r.files = files;
      r.conflictResolution = resolution;
      return new RJobsFilesCreate(this, "/copy").execute(r);
    }

    public Job move(List<JobMountPathPair> files, String resolution) throws IOException, JsonException {
      JobMoveFiles r = new JobMoveFiles();
      r.files = files;
      r.conflictResolution = resolution;
      return new RJobsFilesCreate(this, "/move").execute(r);
    }
    
  }
  
  public RJobsFiles files() {
    return new RJobsFiles(this);
  }
    
}
