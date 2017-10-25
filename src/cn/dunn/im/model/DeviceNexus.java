package cn.dunn.im.model;

import java.io.File;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

//@XmlRootElement
public class DeviceNexus implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id = UUID.randomUUID().toString();
	private String self;
	private String another;
	private String groupId;
	private Long lastreadtime = new Date().getTime();


	public DeviceNexus() {
	}


	// @XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	// @XmlElement
	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	// @XmlElement
	public String getAnother() {
		return another;
	}

	public void setAnother(String another) {
		this.another = another;
	}

	// @XmlElement
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}


	// @XmlElement
	public Long getLastReadTime() {
		return lastreadtime;
	}

	public void setLastReadTime(Long lastreadtime) {
		this.lastreadtime = lastreadtime;
	}

	@Override
	public String toString() {
		return "DeviceNexus [id=" + id + ", self=" + self + ", another=" + another + ", lastreadtime=" + lastreadtime + ", groupId=" + groupId + "]";
	}

}
