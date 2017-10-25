package cn.dunn.im.model;

import java.io.Serializable;
import java.util.UUID;

/**
 * 
 * @Title: GroupMember.java
 * @Package cn.dunn.im.model
 * @Description: 群组成员
 * @author TangTianXiong
 * @date 2015年6月6日 下午3:04:08
 */
//@XmlRootElement
public class GroupMember implements Serializable {
	private static final long serialVersionUID = 1L;
	private String id = UUID.randomUUID().toString();
	/**
	 * 群组ID
	 */
	private String chatGroupId;
	/**
	 * 成员ID
	 */
	private String memberId;
	/**
	 * 该成员在群组中最后读取消息的时间
	 */
	private Long lastReadTime;

	//@XmlElement
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	//@XmlElement
	public String getChatGroupId() {
		return chatGroupId;
	}

	public void setChatGroupId(String chatGroupId) {
		this.chatGroupId = chatGroupId;
	}

	//@XmlElement
	public String getMemberId() {
		return memberId;
	}

	public void setMemberId(String memberId) {
		this.memberId = memberId;
	}

	//@XmlElement
	public Long getLastReadTime() {
		return lastReadTime;
	}

	public void setLastReadTime(Long lastReadTime) {
		this.lastReadTime = lastReadTime;
	}

}
