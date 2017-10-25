package cn.dunn.im.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.util.List;

import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.HistoryMessageReq;
import cn.dunn.im.model.HistoryMessageResp;
import cn.dunn.im.model.Message;
import cn.dunn.im.service.MessageService;

/**
 * 
 * @Title: HistoryMessageReqHandler.java
 * @Package cn.dunn.im.handler
 * @Description: 历史消息请求
 * @author TangTianXiong
 * @date 2015年6月8日 上午11:09:37
 */
public class HistoryMessageReqHandler extends ChannelHandlerAdapter {
	private MessageService messageService;

	public HistoryMessageReqHandler() {
		this.messageService = SpringContainer.getBean(MessageService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof HistoryMessageReq) {
			HistoryMessageReq historyMessageReq = (HistoryMessageReq) msg;

			switch (historyMessageReq.getType()) {
			case Message.TYPE_CHAT:
				List<Message> historyMessage = messageService.getHistoryMessage(historyMessageReq.getLastLoadTime(), historyMessageReq.getWindKey(), historyMessageReq.getUserid());
				HistoryMessageResp historyMessageResp = new HistoryMessageResp();
				historyMessageResp.setWindKey(historyMessageReq.getWindKey());
				historyMessageResp.setHistoryMessage(historyMessage);
				historyMessageResp.setType(Message.TYPE_CHAT);
				ctx.writeAndFlush(historyMessageResp);
				break;

			case Message.TYPE_GROUP:
				List<Message> chatGroupHistoryMessage = messageService.getChatGroupHistoryMessage(historyMessageReq.getLastLoadTime(), historyMessageReq.getWindKey());
				HistoryMessageResp groupHistoryMessageResp = new HistoryMessageResp();
				groupHistoryMessageResp.setHistoryMessage(chatGroupHistoryMessage);
				groupHistoryMessageResp.setType(Message.TYPE_GROUP);
				groupHistoryMessageResp.setWindKey(historyMessageReq.getWindKey());
				ctx.writeAndFlush(groupHistoryMessageResp);
				break;

			default:
				break;
			}

		} else {
			ctx.fireChannelRead(msg);
		}
	}
}
