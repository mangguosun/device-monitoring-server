package cn.dunn.im.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.UserDao;
import cn.dunn.im.model.User;

@Service
public class UserService extends BaseService<String, User> {
	@Resource
	private UserDao userDao;

	@Override
	protected BaseDao<String, User> getEntityDao() {
		return userDao;
	}

	public User getUserByUsername(String username) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("username", username);
		User user = getUnique("getUserByUsername", map);
		return user;
	}

	/**
	 * 验证成功返回持久化的user 否者返回null
	 * 
	 * @param user
	 * @return
	 */
	public User checkLogin(User user) {
		User u = getUserByUsername(user.getUsername());
		if (u != null && u.getPassword().equals(user.getPassword())) {
			return u;
		} else {
			return null;
		}
	}

	public User getById(String userid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		return userDao.getUnique("getById", map);
	}

	public User updateUserInfo(User user) {
		return userDao.updateUserInfo(user);
	}
}
