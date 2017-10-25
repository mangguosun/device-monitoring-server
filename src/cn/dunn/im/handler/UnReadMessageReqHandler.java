package cn.dunn.im.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.UnReadMessageReq;
import cn.dunn.im.model.UnReadMessageResp;
import cn.dunn.im.service.MessageService;

/**
 * 未读消息处理器
 * 
 * @author Administrator
 *
 */
public class UnReadMessageReqHandler extends ChannelHandlerAdapter {
	private MessageService messagerService;

	public UnReadMessageReqHandler() {
		this.messagerService = SpringContainer.getBean(MessageService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof UnReadMessageReq) {
			UnReadMessageReq message = (UnReadMessageReq) msg;
			switch (message.getType()) {
			case UnReadMessageReq.CHAT:
				List<Message> unReadMessage = messagerService.getUnReadMessage(message.getUserid(), message.getAnother());
				UnReadMessageResp unReadMessageResp = new UnReadMessageResp();
				unReadMessageResp.setWindKey(message.getAnother());
				unReadMessageResp.setMessages(unReadMessage);
				unReadMessageResp.setType(UnReadMessageReq.CHAT);
				ctx.writeAndFlush(unReadMessageResp);
				break;
			case UnReadMessageReq.CHAT_GROUP:
				List<Message> groupUnReadMessage = messagerService.getGroupUnReadMessage(message.getUserid(), message.getAnother());
				UnReadMessageResp groupUnReadMessageResp = new UnReadMessageResp();
				groupUnReadMessageResp.setWindKey(message.getAnother());
				groupUnReadMessageResp.setMessages(groupUnReadMessage);
				groupUnReadMessageResp.setType(UnReadMessageReq.CHAT_GROUP);
				ctx.writeAndFlush(groupUnReadMessageResp);
				break;

			default:
				ctx.fireChannelRead(msg);
				break;
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}
