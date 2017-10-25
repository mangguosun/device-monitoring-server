package cn.dunn.im.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.ModifyUserInfoReq;
import cn.dunn.im.model.ModifyUserInfoResp;
import cn.dunn.im.model.NotifyMessage;
import cn.dunn.im.model.User;
import cn.dunn.im.service.FriendNexusService;
import cn.dunn.im.service.UserService;

/**
 * 修改用户信息请求处理
 * 
 * @author Administrator
 *
 */
public class ModifyUserInfoReqHandler extends ChannelHandlerAdapter {
	private UserService userService;
	private FriendNexusService friendNexusService;

	public ModifyUserInfoReqHandler() {
		userService = SpringContainer.getBean(UserService.class);
		friendNexusService = SpringContainer.getBean(FriendNexusService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ModifyUserInfoReq) {
			ModifyUserInfoReq req = (ModifyUserInfoReq) msg;
			ModifyUserInfoResp resp = new ModifyUserInfoResp();
			User user1 = null;
			try {
				User user = userService.updateUserInfo(req.getUser());
				user1 = userService.getById(user.getUserid());
				if (req.getUser().getHead() != null) {
					File headFile = req.getUser().getHead();
					user1.setHead(headFile);
					FileUtils.copyFile(headFile, new File(ConnectConstants.USER_IMAGE_PATH + req.getUser().getHeadImg()));
				}
				resp.setUser(user1);
				resp.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				resp.setSuccess(false);
				resp.setNote(e.getMessage());
			}
			// TODO 通知好友信息修改
			List<User> friends = friendNexusService.getAllFriends(resp.getUser().getUserid());
			for (User user2 : friends) {
				if (ConnectContainer.INSTANCE.getOnLineUsers().contains(user2.getUserid())) {
					NotifyMessage notifyMessage = new NotifyMessage(NotifyMessage.STATUS_MODIFY);
					notifyMessage.setType(Message.TYPE_CHAT);
					notifyMessage.setBody(user1);
					ConnectContainer.INSTANCE.getChannelByUserId(user2.getUserid()).writeAndFlush(notifyMessage);
				}
			}
			ctx.writeAndFlush(resp);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}
