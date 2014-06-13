package net.koofr.api.v2.resources;

import java.io.Serializable;

public class Bookmark extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String mountId;
	private String path;

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
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
	
	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof Bookmark)) {
			return false;
		}
		Bookmark b = (Bookmark)obj;
		return b.getName().equals(name) &&
				b.getMountId().equals(mountId) &&
				b.getPath().equals(path);
	}
}
