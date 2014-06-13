package net.koofr.api.v2.resources;

import java.io.Serializable;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class File extends JsonBase implements Serializable {
	private static final long serialVersionUID = 1L;

	private String name;
	private String type;
	private Long modified;
	private Long size;
	private String contentType;
	private Link receiver, link;
	private Bookmark bookmark;
	private Mount mount;

	public static enum Type {
		DIR("dir"), FILE("file"), PARENT("parent");
		
		private String type;
		private Type(String type) {
			this.type = type;
		}
		@Override
		public String toString() {
			return type;
		}
	}

	public File() {
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

	public Long getModified() {
		return modified;
	}

	public void setModified(Long modified) {
		this.modified = modified;
	}

	public Long getSize() {
		return size;
	}

	public void setSize(Long size) {
		this.size = size;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Link getReceiver() {
		return receiver;
	}

	public void setReceiver(Link receiver) {
		this.receiver = receiver;
	}

	public Link getLink() {
		return link;
	}

	public void setLink(Link link) {
		this.link = link;
	}

	public Bookmark getBookmark() {
		return bookmark;
	}

	public void setBookmark(Bookmark bookmark) {
		this.bookmark = bookmark;
	}
	
	public Mount getMount() {
		return mount;
	}
	
	public void setMount(Mount mount) {
		this.mount = mount;
	}

}
