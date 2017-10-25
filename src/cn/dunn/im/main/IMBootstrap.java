package cn.dunn.im.main;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateHandler;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.handler.ChatGroupMemberReqHandler;
import cn.dunn.im.handler.DeviceGroupHandler;
import cn.dunn.im.handler.DeviceHandler;
import cn.dunn.im.handler.HaveReadMessageChatNotifyHandler;
import cn.dunn.im.handler.HeartBeatServerHandler;
import cn.dunn.im.handler.HistoryMessageReqHandler;
import cn.dunn.im.handler.LoginHandler;
import cn.dunn.im.handler.MarshallingCodeCFactory;
import cn.dunn.im.handler.MessageDispatchHandler;
import cn.dunn.im.handler.ModifyDeviceInfoReqHandler;
import cn.dunn.im.handler.ModifyUserInfoReqHandler;
import cn.dunn.im.handler.UnReadMessageReqHandler;

public class IMBootstrap {
	public static void main(String[] args) throws Exception {
		SpringContainer.init();
		System.err.println("Spring容器初始化成功...");
		new IMBootstrap().start();
	}

	public void start() throws Exception {
		// 创建服务器引导对象  
		ServerBootstrap bootstrap = new ServerBootstrap();
		// 配置服务端的NIO线程组
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		
		try {  
			// 设置“线程池” 
			bootstrap.group(bossGroup, workerGroup);
			// 告诉Netty它也使用NioServerSocketChannel作为连接通道  
			bootstrap.channel(NioServerSocketChannel.class);
			bootstrap.option(ChannelOption.SO_BACKLOG, 100);
			bootstrap.handler(new LoggingHandler(LogLevel.INFO));
			
			// 当客户端向服务器发送数据的时候都会经过这个方法 
			bootstrap.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws IOException {
							// ch.pipeline().addLast(new PacageDecoderHandler(1024 *
							// 1024 * 1024, 0, 4));
							// ch.pipeline().addLast(new PacageEncoderHandler());
	
							// ch.pipeline().addLast(new IdleStateHandler(5,0,0,TimeUnit.SECONDS));
							// ch.pipeline().addLast(new HeartBeatServerHandler());
	
							ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingDecoder());
							ch.pipeline().addLast(MarshallingCodeCFactory.buildMarshallingEncoder());
							// ch.pipeline().addLast(new ReadTimeoutHandler(5));
							ch.pipeline().addLast(new DeviceHandler());
							ch.pipeline().addLast(new LoginHandler());
							ch.pipeline().addLast(new DeviceGroupHandler());
							ch.pipeline().addLast(new ModifyDeviceInfoReqHandler());
							
							
							
							
							ch.pipeline().addLast(new MessageDispatchHandler());
							ch.pipeline().addLast(new HaveReadMessageChatNotifyHandler());
							ch.pipeline().addLast(new UnReadMessageReqHandler());
							ch.pipeline().addLast(new HistoryMessageReqHandler());
							ch.pipeline().addLast(new ModifyUserInfoReqHandler());
							ch.pipeline().addLast(new ChatGroupMemberReqHandler());
						}
					});
			
			// 同步绑定端口，如果绑定端口失败，则会抛出异常 
			ChannelFuture future= bootstrap.bind(ConnectConstants.LOCAL_IP, ConnectConstants.LOCAL_PORT).sync();
		    if(future.isSuccess()){  
		    	System.err.println("服务启动成功...IP（" + ConnectConstants.LOCAL_IP + "）端口（" + ConnectConstants.LOCAL_PORT + "）");
		    }
		    // 同步监听服务器端口的关闭  
		    future.channel().closeFuture().sync();  
			
			
		} catch (Exception e) {
			System.out.println(e.getMessage());
           // e.printStackTrace();  
        } finally {  
        	
            // 优雅的释放线程资源，并关闭服务器  
        	bossGroup.shutdownGracefully();  
        	workerGroup.shutdownGracefully();  
        }
	}
}
