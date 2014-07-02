package net.koofr.api.v2.resources;

public class Hit {

    String mountId;
    String path;
    double score;
    String name;
    String type;
    long modified;
    long size;
    String contentType;
    Mount mount;

    public Mount getMount() {
		return mount;
	}

	public void setMount(Mount mount) {
		this.mount = mount;
	}

	public Hit() {
	}

	public String getMountId() {
		return mountId;
	}

	public void setMountId(String mountId) {
		this.mountId = mountId;
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public double getScore() {
		return score;
	}

	public void setScore(double score) {
		this.score = score;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getModified() {
		return modified;
	}

	public void setModified(long modified) {
		this.modified = modified;
	}

	public long getSize() {
		return size;
	}

	public void setSize(long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

}
