package cn.dunn.im.model;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

/**
 * 
 * @Title: FriendNexus.java
 * @Package cn.dunn.im.model
 * @Description: 好友关系
 * @author TangTianXiong
 * @date 2015年6月6日 下午2:24:16
 */
//@XmlRootElement
public class FriendNexus implements Serializable {

	private static final long serialVersionUID = 1L;
	private String id = UUID.randomUUID().toString();
	/**
	 * 自己
	 */
	private String self;
	/**
	 * 对方
	 */
	private String another;
	/**
	 * 消息读取时间
	 */
	private Long lastReadTime = new Date().getTime();
	/**
	 * 所属分组ID
	 */
	private String groupId;

	//@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//@XmlElement
	public String getSelf() {
		return self;
	}

	public void setSelf(String self) {
		this.self = self;
	}

	//@XmlElement
	public String getAnother() {
		return another;
	}

	public void setAnother(String another) {
		this.another = another;
	}

	//@XmlElement
	public Long getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(Long lastReadTime) {
		this.lastReadTime = lastReadTime;
	}

	//@XmlElement
	public String getGroupId() {
		return groupId;
	}

	public void setGroupId(String groupId) {
		this.groupId = groupId;
	}

	@Override
	public String toString() {
		return "FriendNexus [id=" + id + " self=" + self + ", another=" + another + ", lastReadTime=" + lastReadTime + "]";
	}

}
