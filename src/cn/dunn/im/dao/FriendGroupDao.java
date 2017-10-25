package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.FriendGroup;

@Repository
public class FriendGroupDao extends BaseDao<String, FriendGroup> {

	@Override
	protected Class<FriendGroup> getEntity() {
		return FriendGroup.class;
	}

}
