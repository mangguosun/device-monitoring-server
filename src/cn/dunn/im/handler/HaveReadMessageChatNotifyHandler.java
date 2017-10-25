package cn.dunn.im.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.HaveReadMessageChatNotify;
import cn.dunn.im.service.MessageService;

/**
 * 更新最后消息的读取时间
 * 
 * @author Administrator
 *
 */
public class HaveReadMessageChatNotifyHandler extends ChannelHandlerAdapter {
	private MessageService messagerService;

	public HaveReadMessageChatNotifyHandler() {
		this.messagerService = SpringContainer.getBean(MessageService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HaveReadMessageChatNotify) {
			HaveReadMessageChatNotify message = (HaveReadMessageChatNotify) msg;
			if (HaveReadMessageChatNotify.CHAT.equals(message.getType())) {
				messagerService.updateChatLastReadTime(message);
			} else if (HaveReadMessageChatNotify.CHAT_GROUP.equals(message.getType())) {
				messagerService.updateChatGroupLastReadTime(message);
			} else {
				ctx.fireChannelRead(msg);
			}
		} else {
			ctx.fireChannelRead(msg);
		}

	}
}
