package cn.dunn.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.MessageDao;
import cn.dunn.im.model.HaveReadMessageChatNotify;
import cn.dunn.im.model.Message;

@Service
public class MessageService extends BaseService<Long, Message> {

	@Resource
	private MessageDao messageDao;

	@Override
	protected BaseDao<Long, Message> getEntityDao() {
		return messageDao;
	}

	/**
	 * 获取未读消息
	 * 
	 * @param self
	 * @param another
	 * @return
	 */
	public List<Message> getUnReadMessage(String self, String another) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("self", self);
		map.put("another", another);
		return messageDao.getList("getUnReadMessage", map);
	}

	/**
	 * 获取未读消息条数
	 * 
	 * @param self
	 * @param another
	 * @return
	 */
	public int getUnReadMessageCount(String self, String another) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("another", another);
		map.put("self", self);
		return getCount("getUnReadMessageCount", map);
	}

	/**
	 * 获取群未读消息
	 * 
	 * @param userid
	 * @param another
	 * @return
	 */
	public List<Message> getGroupUnReadMessage(String userid, String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		map.put("userid", userid);
		return getList("getGroupUnReadMessage", map);
	}

	/**
	 * 获取群未读消息条数
	 * 
	 * @param self
	 * @param another
	 * @return
	 */
	public int getGroupUnReadMessageCount(String userid, String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		map.put("userid", userid);
		return getCount("getGroupUnReadMessageCount", map);
	}

	/**
	 * 获取历史消息
	 * 
	 * @param time
	 * @param another
	 * @param selt
	 * @return
	 */
	public List<Message> getHistoryMessage(Long time, String another, String selt) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", time);
		map.put("another", another);
		map.put("selt", selt);
		return getList("getHistoryMessage", map);
	}

	/**
	 * 获取群组历史消息
	 * 
	 * @param time
	 * @param groupid
	 * @return
	 */
	public List<Message> getChatGroupHistoryMessage(Long time, String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", time);
		map.put("groupid", groupid);
		return getList("getChatGroupHistoryMessage", map);
	}

	/**
	 * 更新好友最后阅时间
	 * 
	 * @param notify
	 */
	public void updateChatLastReadTime(HaveReadMessageChatNotify notify) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", notify.getReadTime());
		map.put("userid", notify.getUserid());
		map.put("another", notify.getAnother());
		messageDao.update("updateChatLastReadTime", map);
	}

	/**
	 * 更新群组最后阅读时间
	 * 
	 * @param notify
	 */
	public void updateChatGroupLastReadTime(HaveReadMessageChatNotify notify) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("time", notify.getReadTime());
		map.put("userid", notify.getUserid());
		map.put("another", notify.getAnother());
		messageDao.update("updateChatGroupLastReadTime", map);
	}
}
