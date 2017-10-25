package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.FriendNexus;

@Repository
public class FriendNexusDao extends BaseDao<String, FriendNexus> {

	@Override
	protected Class<FriendNexus> getEntity() {
		return FriendNexus.class;
	}

}
