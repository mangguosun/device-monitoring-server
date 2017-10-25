package cn.dunn.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.FriendNexusDao;
import cn.dunn.im.model.FriendNexus;
import cn.dunn.im.model.User;

@Service
public class FriendNexusService extends BaseService<String, FriendNexus> {
	@Resource
	private FriendNexusDao friendNexusDao;

	@Override
	protected BaseDao<String, FriendNexus> getEntityDao() {
		return friendNexusDao;
	}

	/**
	 * 是否是好友
	 * 
	 * @param nexus
	 * @return
	 */
	public boolean isFriendNexus(FriendNexus nexus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("self", nexus.getSelf());
		map.put("another", nexus.getAnother());
		FriendNexus friendNexus = getUnique("isFriendNexus", map);
		if (friendNexus != null) {
			return true;
		}
		return false;
	}

	/**
	 * 保存好友关系
	 */
	public void save(FriendNexus nexus) {
		friendNexusDao.save(nexus);
		String another = nexus.getAnother();
		String self = nexus.getSelf();
		nexus.setSelf(another);
		nexus.setAnother(self);
		nexus.setId(UUID.randomUUID().toString());
		friendNexusDao.save(nexus);
	}

	/**
	 * 获取用户的所有好友
	 * 
	 * @param userid
	 * @return
	 */
	public List<User> getAllFriends(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return friendNexusDao.getList("getAllFriends", map, User.class);
	}

}
