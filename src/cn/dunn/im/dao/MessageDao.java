package cn.dunn.im.dao;

import org.springframework.stereotype.Service;

import cn.dunn.im.model.Message;

@Service
public class MessageDao extends BaseDao<Long, Message> {

	@Override
	protected Class<Message> getEntity() {
		return Message.class;
	}

}
