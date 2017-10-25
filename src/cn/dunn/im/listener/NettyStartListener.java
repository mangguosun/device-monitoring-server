package cn.dunn.im.listener;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.io.IOException;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.handler.ChatGroupMemberReqHandler;
import cn.dunn.im.handler.HaveReadMessageChatNotifyHandler;
import cn.dunn.im.handler.HistoryMessageReqHandler;
import cn.dunn.im.handler.LoginHandler;
import cn.dunn.im.handler.MarshallingCodeCFactory;
import cn.dunn.im.handler.MessageDispatchHandler;
import cn.dunn.im.handler.ModifyUserInfoReqHandler;
import cn.dunn.im.handler.UnReadMessageReqHandler;

public class NettyStartListener implements ServletContextListener {
	@Override
	public void contextInitialized(ServletContextEvent event) {

		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 100).handler(new LoggingHandler(LogLevel.INFO))
				.childHandler(new ChannelInitializer<SocketChannel>() {
					@Override
					public void initChannel(SocketChannel ch) throws IOException {
						// ch.pipeline().addLast(new PacageDecoderHandler(1024 *
						// 1024 * 1024, 0, 4));
						// ch.pipeline().addLast(new PacageEncoderHandler());
						ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
						ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
						// ch.pipeline().addLast(new ReadTimeoutHandler(5));
						ch.pipeline().addLast(new LoginHandler());
						ch.pipeline().addLast(new MessageDispatchHandler());
						ch.pipeline().addLast(new HaveReadMessageChatNotifyHandler());
						ch.pipeline().addLast(new UnReadMessageReqHandler());
						ch.pipeline().addLast(new HistoryMessageReqHandler());
						ch.pipeline().addLast(new ModifyUserInfoReqHandler());
						ch.pipeline().addLast(new ChatGroupMemberReqHandler());
					}
				});

		// 绑定端口，同步等待成功
		try {
			b.bind(ConnectConstants.LOCAL_IP, ConnectConstants.LOCAL_PORT).sync();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.err.println("服务启动成功...");

	}

	@Override
	public void contextDestroyed(ServletContextEvent event) {

	}

}
