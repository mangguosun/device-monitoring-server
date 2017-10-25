package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.User;

@Repository
public class UserDao extends BaseDao<String, User> {

	protected Class<User> getEntity() {
		return User.class;
	}

	public User updateUserInfo(User user) {
		getSqlSession().update(getEntity().getName() + ".updateUserInfo", user);
		return user;
	}
}
