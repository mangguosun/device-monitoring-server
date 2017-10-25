package cn.dunn.im.web.controller;

import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.ThreadContainer;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.NotifyMessage;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;
import cn.dunn.im.service.FriendNexusService;
import cn.dunn.im.service.PropertiesService;
import cn.dunn.im.util.HttpUtil;

import com.alibaba.fastjson.JSON;

/**
 * 提供web服务的出席服务
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("presenceProvider")
public class PresenceProviderController extends BaseController {
	@Resource
	private PropertiesService propertiesService;
	@Resource
	private ChatGroupService chatGroupService;
	@Resource
	private FriendNexusService friendNexusService;

	/**
	 * 通知用户好友下线
	 * 
	 * @param text
	 *            User.json
	 * 
	 */
	@RequestMapping("notifyUserFriendsOffline")
	@ResponseBody
	public void notifyUserFriendsOffline(String text) {
		User u = JSON.parseObject(text, User.class);
		List<User> friends = friendNexusService.getAllFriends(u.getUserid());
		for (User user : friends) {
			// netty通知
			if (ConnectContainer.INSTANCE.isOnline(user.getUserid())) {
				Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid());
				NotifyMessage notifyMessage = new NotifyMessage(NotifyMessage.STATUS_OFFLINE);
				notifyMessage.setType(Message.TYPE_CHAT);
				notifyMessage.setBody(u);
				channel.writeAndFlush(notifyMessage);
			}
		}
		// web通知
		final String offLineUrl = propertiesService.getProperty("offLineUrl");
		final Map<String, String> map = new HashMap<String, String>();
		map.put("users", JSON.toJSONString(friends));
		ThreadContainer.EXECUTOR_THREAD_POOL.execute(new Runnable() {
			public void run() {
				HttpUtil.post(offLineUrl, map);
			}
		});
	}

	/**
	 * 下线通知所有群成员
	 * 
	 * @param u
	 *            User.json
	 */
	@RequestMapping("notifyGroupMemberOffline")
	@ResponseBody
	public void notifyGroupMemberOffline(String text) {
		User u = JSON.parseObject(text, User.class);
		List<User> users = chatGroupService.getAllGroupMember(u.getUserid());
		for (User user : users) {
			Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid());
			if (channel != null) {
				NotifyMessage notifyMessage = new NotifyMessage(NotifyMessage.STATUS_OFFLINE);
				notifyMessage.setType(Message.TYPE_GROUP);
				u.setChatGroupId(user.getChatGroupId());
				notifyMessage.setBody(u);
				channel.writeAndFlush(notifyMessage);
			}
		}

		// web通知
		final String groupMemberOfflineUrl = propertiesService.getProperty("groupMemberOfflineUrl");
		final Map<String, String> map = new HashMap<String, String>();
		map.put("users", JSON.toJSONString(users));
		ThreadContainer.EXECUTOR_THREAD_POOL.execute(new Runnable() {
			public void run() {
				HttpUtil.post(groupMemberOfflineUrl, map);
			}
		});
	}

	/**
	 * 通好友上线
	 * 
	 * @param text
	 *            User.json
	 */
	@RequestMapping("notifyUserFriendsOnline")
	@ResponseBody
	public void notifyUserFriendsOnline(String text) {
		User u = JSON.parseObject(text, User.class);
		// 该用户上线通知在线的好友
		List<User> allFriends = friendNexusService.getAllFriends(u.getUserid());
		for (User user : allFriends) {
			Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid());
			if (channel != null) {
				NotifyMessage notifyMessage = new NotifyMessage(NotifyMessage.STATUS_ONLINE);
				notifyMessage.setType(Message.TYPE_CHAT);
				notifyMessage.setBody(u);
				channel.writeAndFlush(notifyMessage);
			}
		}
		// web通知
		final String userFriendsOnlineUrl = propertiesService.getProperty("userFriendsOnlineUrl");
		final Map<String, String> map = new HashMap<String, String>();
		map.put("users", JSON.toJSONString(allFriends));
		ThreadContainer.EXECUTOR_THREAD_POOL.execute(new Runnable() {
			public void run() {
				HttpUtil.post(userFriendsOnlineUrl, map);
			}
		});
	}

	/**
	 * 上线通知所有群成员
	 * 
	 * @param text
	 *            User.json
	 */
	@RequestMapping("notifyGroupMemberOnline")
	@ResponseBody
	public void notifyGroupMemberOnline(String text) {
		User u = JSON.parseObject(text, User.class);
		List<User> users = chatGroupService.getAllGroupMember(u.getUserid());
		for (User user : users) {
			Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid());
			if (channel != null) {
				NotifyMessage notifyMessage = new NotifyMessage(NotifyMessage.STATUS_ONLINE);
				notifyMessage.setType(Message.TYPE_GROUP);
				u.setChatGroupId(user.getChatGroupId());
				notifyMessage.setBody(u);
				channel.writeAndFlush(notifyMessage);
			}

		}
		// web通知
		final String groupMemberOnlineUrl = propertiesService.getProperty("groupMemberOnlineUrl");
		final Map<String, String> map = new HashMap<String, String>();
		map.put("users", JSON.toJSONString(users));
		ThreadContainer.EXECUTOR_THREAD_POOL.execute(new Runnable() {
			public void run() {
				HttpUtil.post(groupMemberOnlineUrl, map);
			}
		});
	}

}
