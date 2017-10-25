package cn.dunn.im.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * 
 * @Title: SearchResp.java
 * @Package cn.dunn.im.model
 * @Description: 搜索结果
 * @author TangTianXiong
 * @date 2015年6月10日 下午3:29:34
 */
public class SearchResp implements Serializable{
		
	private static final long serialVersionUID = 1L;
	/**
	 * 搜索类型
	 */
	private String type;
	/**
	 * 窗口id
	 */
	private String windKey;

	/**
	 * 搜索结果
	 */
	private List<User> users = new ArrayList<User>();
	/**
	 * 搜索结果
	 */
	private List<ChatGroup> groups = new ArrayList<ChatGroup>();

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getWindKey() {
		return windKey;
	}

	public void setWindKey(String windKey) {
		this.windKey = windKey;
	}

	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

	public List<ChatGroup> getGroups() {
		return groups;
	}

	public void setGroups(List<ChatGroup> groups) {
		this.groups = groups;
	}

}
