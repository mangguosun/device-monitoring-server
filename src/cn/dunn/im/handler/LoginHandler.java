package cn.dunn.im.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.ChatGroup;
import cn.dunn.im.model.FriendGroup;
import cn.dunn.im.model.LoginRequestMessage;
import cn.dunn.im.model.LoginResponseMessage;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.MyChatGroup;
import cn.dunn.im.model.MyFriendGroup;
import cn.dunn.im.model.NotifyMessage;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;
import cn.dunn.im.service.FriendGroupService;
import cn.dunn.im.service.FriendNexusService;
import cn.dunn.im.service.MessageService;
import cn.dunn.im.service.UserService;

public class LoginHandler extends ChannelHandlerAdapter {
	protected Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	private UserService userService;
	private FriendGroupService friendGroupService;
	private FriendNexusService friendNexusService;
	private MessageService messageservice;
	private ChatGroupService chatGroupService;
	

	public LoginHandler() {
		this.userService = SpringContainer.getBean(UserService.class);
		this.friendGroupService = SpringContainer.getBean(FriendGroupService.class);
		this.friendNexusService = SpringContainer.getBean(FriendNexusService.class);
		this.messageservice = SpringContainer.getBean(MessageService.class);
		this.chatGroupService = SpringContainer.getBean(ChatGroupService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof LoginRequestMessage) {
			
			
			LoginRequestMessage message = (LoginRequestMessage) msg;
			User u = new User(message.getUsername(), message.getPassword());
			u = userService.checkLogin(u);
			if (u != null) {
				// 给用户响应验证正确登录成功
				ConnectContainer.INSTANCE.userOnLine(u.getUserid(), ctx.channel());
				LoginResponseMessage result = new LoginResponseMessage(true);
				File headImage = new File(ConnectConstants.USER_IMAGE_PATH + u.getHeadImg());
				if (headImage.exists()) {
					u.setHead(new File(ConnectConstants.USER_IMAGE_PATH + u.getHeadImg()));
				}
				result.setUser(u);
				ctx.writeAndFlush(result);
				loadUserFriends(ctx.channel(), u);
				loadUserChatGroup(ctx.channel(), u);
				notifyUserFriendsOnline(u);
				notifyGroupMemberOnline(u);
			} else {
				
				LoginResponseMessage result = new LoginResponseMessage(false);
				result.setMsg("账号或密码错误");
				ctx.writeAndFlush(result);
				//ctx.close();
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	/**
	 * 加载用户好友
	 * 
	 * @param channel
	 * 
	 * @param u
	 */
	private void loadUserFriends(Channel channel, User u) {
		// 加载用户分组
		List<FriendGroup> myGroup = friendGroupService.getMyGroup(u.getUserid());

		for (FriendGroup friendGroup : myGroup) {
			// 加载用户分组中的好友
			List<User> friends = friendGroupService.getGroupFriend(u.getUserid(), friendGroup.getId());
			for (User user : friends) {
				// 加载用户头像文件
				String image = user.getHeadImg();
				File headFile = new File(ConnectConstants.USER_IMAGE_PATH + image);
				if (!headFile.exists()) {
					headFile = new File(ConnectConstants.USER_IMAGE_PATH + "default.png");
					if (!headFile.exists()) {
						logger.info("lack default.png");
						headFile = null;
					}
				}
				user.setHead(headFile);
				// 加用户与好友的未读消息数量
				user.setUnReadMessageCount(messageservice.getUnReadMessageCount(u.getUserid(), user.getUserid()));
				// 设置用户好友是否已经在线上
				user.setIsOnline(ConnectContainer.INSTANCE.isOnline(user.getUserid()));
			}
			// 为该分组设置好友
			friendGroup.getFriends().addAll(friends);
		}
		channel.writeAndFlush(new MyFriendGroup(myGroup));
	}

	/**
	 * 加载用户的群组
	 * 
	 * @param channel
	 * 
	 * @param u
	 */
	private void loadUserChatGroup(Channel channel, User u) {
		// 获取用户所有的群
		List<ChatGroup> chatGroups = chatGroupService.getMyChatGroup(u.getUserid());
		for (ChatGroup chatGroup : chatGroups) {
			File headFile = new File(ConnectConstants.USER_IMAGE_PATH + chatGroup.getHeadImg());
			if (!headFile.exists() || !headFile.isFile()) {
				headFile = new File(ConnectConstants.USER_IMAGE_PATH + "group_defult.png");
				chatGroup.setHeadImg("group_defult.png");
			}
			chatGroup.setHead(headFile);
			// 设置该用户在群组中未读消息
			chatGroup.setUnReadMessageCount(messageservice.getGroupUnReadMessageCount(u.getUserid(), chatGroup.getId()));
		}
		channel.writeAndFlush(new MyChatGroup(chatGroups));
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof ReadTimeoutException || cause instanceof IOException) {
			String id = ctx.channel().id().asLongText();
			String userid = ConnectContainer.INSTANCE.getUserIdByMark(id);
			User u = userService.getById(userid);
			System.out.println(u.getUsername() + ":下线了");
			ConnectContainer.INSTANCE.downMachineByMark(id);
			notifyUserFriendsOffline(u);
			notifyGroupMemberOffline(u);
			ctx.close();
		} else {
			ctx.fireExceptionCaught(cause);
		}
	}

	/**
	 * 通知用户好友下线
	 * 
	 * @param userid
	 * @param u
	 */
	private void notifyUserFriendsOffline(User u) {
		List<User> friends = friendNexusService.getAllFriends(u.getUserid());
		for (User user : friends) {
			if (ConnectContainer.INSTANCE.isOnline(user.getUserid())) {
				Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid());
				NotifyMessage notifyMessage = new NotifyMessage(NotifyMessage.STATUS_OFFLINE);
				notifyMessage.setType(Message.TYPE_CHAT);
				notifyMessage.setBody(u);
				channel.writeAndFlush(notifyMessage);
			}
		}
	}

	/**
	 * 下线通知所有群成员
	 * 
	 * @param userid
	 */
	private void notifyGroupMemberOffline(User u) {
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
	}

	/**
	 * 上线通知所有群成员
	 * 
	 * @param userid
	 */
	private void notifyGroupMemberOnline(User u) {
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
	}

	/**
	 * 通好友上线
	 * 
	 * @param u
	 */
	private void notifyUserFriendsOnline(User u) {
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
	}
}
