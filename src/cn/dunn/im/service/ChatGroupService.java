package cn.dunn.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.ChatGroupDao;
import cn.dunn.im.model.ChatGroup;
import cn.dunn.im.model.User;

@Service
public class ChatGroupService extends BaseService<String, ChatGroup> {
	@Resource
	private ChatGroupDao chatGroupDao;

	@Override
	protected BaseDao<String, ChatGroup> getEntityDao() {
		return chatGroupDao;
	}

	/**
	 * 获取我的聊天群组
	 * 
	 * @param userid
	 * @return
	 */
	public List<ChatGroup> getMyChatGroup(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return chatGroupDao.getList("getMyChatGroup", map);
	}

	/**
	 * 获取群成员
	 * 
	 * @param groupid
	 * @return
	 */
	public List<User> getGroupMember(String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		return chatGroupDao.getList("getGroupMember", map, User.class);
	}

	/**
	 * 获取该用户加的群所有成员
	 * 
	 * @param userid
	 * @return
	 */
	public List<User> getAllGroupMember(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return chatGroupDao.getList("getAllGroupMember", map, User.class);
	}

}
