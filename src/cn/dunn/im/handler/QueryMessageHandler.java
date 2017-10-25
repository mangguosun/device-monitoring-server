package cn.dunn.im.handler;

import io.netty.channel.ChannelHandlerAdapter;

/**
 * 接受获取好友请求
 * 
 * @author Administrator
 * 
 */
public class QueryMessageHandler extends ChannelHandlerAdapter {/*
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof QueryMessage) {
			QueryMessage message = (QueryMessage) msg;
			switch (message.getType()) {
			case QueryMessage.QUREY_MYFRIENDS:
				String from = message.getFrom();
				Set<String> users = ConnectContainer.INSTANCE.getOnLineUsers();
				List<User> friends = new ArrayList<User>();
				for (String string : users) {
					User user = new User();
					user.setUsername(string);
					friends.add(user);
				}
//				MyFriends myFriends = new MyFriends();
//				myFriends.setFriends(friends);
				Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(from);
//				channel.writeAndFlush(myFriends);
				break;
			default:
				ctx.fireChannelRead(msg);
				break;
			}
		} else {
			ctx.fireChannelRead(msg);
		}
	}


*/}
