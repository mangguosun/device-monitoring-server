package cn.dunn.im.handler;

import java.io.File;
import java.util.List;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.ChatGroupMemberReq;
import cn.dunn.im.model.ChatGroupMemberResp;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;

/**
 * 
 * @Title: ChatGroupMemberReqHandler.java
 * @Package cn.dunn.im.handler
 * @Description: 获取群成员请求
 * @author TangTianXiong
 * @date 2015年6月9日 上午9:41:29
 */
public class ChatGroupMemberReqHandler extends ChannelHandlerAdapter {
	private ChatGroupService chatGroupService;

	public ChatGroupMemberReqHandler() {
		this.chatGroupService = SpringContainer.getBean(ChatGroupService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ChatGroupMemberReq) {
			ChatGroupMemberReq req = (ChatGroupMemberReq) msg;
			List<User> members = chatGroupService.getGroupMember(req.getWindKey());
			for (User user : members) {
				File headFile = new File(ConnectConstants.USER_IMAGE_PATH + user.getHeadImg());
				if (!headFile.exists())
					headFile = new File(ConnectConstants.USER_IMAGE_PATH + "default.png");
				user.setHead(headFile);
				if (ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid()) == null) {
					user.setIsOnline(false);
				} else {
					user.setIsOnline(true);
				}
			}
			ChatGroupMemberResp result = new ChatGroupMemberResp();
			result.setMember(members);
			result.setWindKey(req.getWindKey());
			ctx.writeAndFlush(result);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}
