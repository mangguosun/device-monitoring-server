package cn.dunn.im.handler;

import java.util.List;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;
import cn.dunn.im.service.MessageService;
import cn.dunn.im.service.PropertiesService;
import cn.dunn.im.service.UserService;
import cn.dunn.im.util.HttpParaUtil;
import cn.dunn.im.util.HttpUtil;

public class MessageDispatchHandler extends SimpleChannelInboundHandler<Object> {

	private MessageService messageService;
	private ChatGroupService chatGroupService;
	private PropertiesService propertiesService;

	public MessageDispatchHandler() {
		this.messageService = SpringContainer.getBean(MessageService.class);
		this.chatGroupService = SpringContainer.getBean(ChatGroupService.class);
		this.propertiesService = SpringContainer.getBean(PropertiesService.class);
	}

	@Override
	protected void messageReceived(ChannelHandlerContext ctx, Object obj) throws Exception {
		if (obj instanceof Message) {
			final Message message = (Message) obj;
			ctx.executor().execute(new Runnable() {
				public void run() {
					messageService.save(message);
				}
			});
			String to = message.getTo();
			switch (message.getType()) {
			case Message.TYPE_CHAT:
				Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(to);
				if (channel != null) {
					channel.writeAndFlush(message);
				}
				// 对web端 进行推送
				if (ConnectContainer.INSTANCE.webUserIsOnLine(to)) {
					String post = HttpUtil.post(propertiesService.getProperty("webMessageReceiveUrl"),
							HttpParaUtil.paraSet(message));
					System.out.println(post);
				}
				break;
			case Message.TYPE_GROUP:
				List<User> member = chatGroupService.getGroupMember(to);
				for (User user : member) {
					Channel channel2 = ConnectContainer.INSTANCE.getChannelByUserId(user.getUserid());
					if (channel2 != null) {
						channel2.writeAndFlush(message);
					}
				}
				break;
			default:
				ctx.fireChannelRead(obj);
				break;
			}

		} else {
			ctx.fireChannelRead(obj);
		}
	}
}
