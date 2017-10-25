package cn.dunn.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.FriendGroupDao;
import cn.dunn.im.model.FriendGroup;
import cn.dunn.im.model.User;

@Service
public class FriendGroupService extends BaseService<String, FriendGroup> {
	@Resource
	private FriendGroupDao friendGroupDao;

	@Override
	protected BaseDao<String, FriendGroup> getEntityDao() {
		return friendGroupDao;
	}

	/**
	 * 获取我的分组
	 * 
	 * @param userid
	 * @return
	 */
	public List<FriendGroup> getMyGroup(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return getList("getMyGroup", map);
	}

	/**
	 * 获取分组Id下的所有好友
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public List<User> getGroupFriend(String userid, String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("groupid", groupid);
		return getList("getGroupFriend", map, User.class);
	}

}
