package cn.dunn.im.web.controller;

import java.io.File;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.model.ChatGroup;
import cn.dunn.im.model.FriendGroup;
import cn.dunn.im.model.LoginRequestMessage;
import cn.dunn.im.model.LoginResponseMessage;
import cn.dunn.im.model.MyChatGroup;
import cn.dunn.im.model.MyFriendGroup;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;
import cn.dunn.im.service.FriendGroupService;
import cn.dunn.im.service.MessageService;
import cn.dunn.im.service.PropertiesService;
import cn.dunn.im.service.UserService;

import com.alibaba.fastjson.JSON;

/**
 * 加载用户信息
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("contentProvider")
public class ContentProviderController extends BaseController {
	@Resource
	private PropertiesService propertiesService;
	@Resource
	private FriendGroupService friendGroupService;
	@Resource
	private UserService userService;
	@Resource
	private MessageService messageservice;
	@Resource
	private ChatGroupService chatGroupService;

	/**
	 * 用户登录
	 * 
	 * @param text
	 *            LoginRequestMessage.json
	 * @return
	 */
	@RequestMapping("login")
	@ResponseBody
	public LoginResponseMessage login(String text) {
		LoginResponseMessage result = null;
		LoginRequestMessage requestMessage = JSON.parseObject(text, LoginRequestMessage.class);
		if (requestMessage == null) {
			result = new LoginResponseMessage(false);
			result.setReason("非法参数!");
			return result;
		}
		User u = new User(requestMessage.getUsername(), requestMessage.getPassword());
		u = userService.checkLogin(u);
		if (u != null) {
			// if (ConnectContainer.INSTANCE.webUserOnLine(u.getUserid())) {
			ConnectContainer.INSTANCE.webUserOnLine(u.getUserid());
			result = new LoginResponseMessage(true);
			File headImage = new File(ConnectConstants.USER_IMAGE_PATH + u.getHeadImg());
			if (headImage.exists()) {
				u.setHead(new File(ConnectConstants.USER_IMAGE_PATH + u.getHeadImg()));
			}
			result.setUser(u);
			// } else {
			// result = new LoginResponseMessage(false);
			// result.setReason("该帐号已经在其他web页面登录");
			// }
		} else {
			result = new LoginResponseMessage(false);
			result.setReason("密码错误或者帐号不存在");
		}
		return result;
	}

	/**
	 * 加载用户好友
	 * 
	 * @param text
	 *            User.json
	 * @return
	 */
	@RequestMapping("loadUserFriends")
	@ResponseBody
	public MyFriendGroup loadUserFriends(String text) {
		User user = JSON.parseObject(text, User.class);
		// 加载用户分组
		List<FriendGroup> myGroup = friendGroupService.getMyGroup(user.getUserid());

		for (FriendGroup friendGroup : myGroup) {
			// 加载用户分组中的好友
			List<User> friends = friendGroupService.getGroupFriend(user.getUserid(), friendGroup.getId());
			for (User u : friends) {
				// 加载用户头像文件
				String image = u.getHeadImg();
				File headFile = new File(ConnectConstants.USER_IMAGE_PATH + image);
				if (!headFile.exists()) {
					headFile = new File(ConnectConstants.USER_IMAGE_PATH + "default.png");
					if (!headFile.exists()) {
						logger.info("lack default.png");
						headFile = null;
					}
				}
				u.setHead(headFile);
				// 加用户与好友的未读消息数量
				u.setUnReadMessageCount(messageservice.getUnReadMessageCount(user.getUserid(), u.getUserid()));
				// 设置用户好友是否已经在线上
				u.setIsOnline(ConnectContainer.INSTANCE.isOnline(u.getUserid()));
			}
			// 为该分组设置好友
			friendGroup.getFriends().addAll(friends);
		}
		return new MyFriendGroup(myGroup);
	}

	/**
	 * 加载用户群
	 * 
	 * @param text
	 * @return
	 */
	@RequestMapping("loadUserChatGroup")
	@ResponseBody
	public MyChatGroup loadUserChatGroup(String text) {
		User user = JSON.parseObject(text, User.class);
		// 获取用户所有的群
		List<ChatGroup> chatGroups = chatGroupService.getMyChatGroup(user.getUserid());
		for (ChatGroup chatGroup : chatGroups) {
			File headFile = new File(ConnectConstants.USER_IMAGE_PATH + chatGroup.getHeadImg());
			if (!headFile.exists() || !headFile.isFile()) {
				headFile = new File(ConnectConstants.USER_IMAGE_PATH + "group_defult.png");
				chatGroup.setHeadImg("group_defult.png");
			}
			chatGroup.setHead(headFile);
			// 设置该用户在群组中未读消息
			chatGroup.setUnReadMessageCount(messageservice.getGroupUnReadMessageCount(user.getUserid(), chatGroup.getId()));
		}
		return new MyChatGroup(chatGroups);
	}
}
