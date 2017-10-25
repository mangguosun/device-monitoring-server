package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.ChatGroup;

@Repository
public class ChatGroupDao extends BaseDao<String, ChatGroup> {

	@Override
	protected Class<ChatGroup> getEntity() {
		return ChatGroup.class;
	}

}
