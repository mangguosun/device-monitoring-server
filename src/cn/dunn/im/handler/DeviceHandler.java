package cn.dunn.im.handler;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.timeout.ReadTimeoutException;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.Device;
import cn.dunn.im.model.DeviceGroup;
import cn.dunn.im.model.DeviceRequestMessage;
import cn.dunn.im.model.DeviceResponseMessage;
import cn.dunn.im.model.FriendGroup;
import cn.dunn.im.model.MyDeviceGroup;
import cn.dunn.im.model.User;
import cn.dunn.im.service.DeviceGroupService;
import cn.dunn.im.service.DeviceService;

public class DeviceHandler extends ChannelHandlerAdapter {
	protected Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	private DeviceGroupService deviceGroupService;
	
	private DeviceService deviceService;
	
	public DeviceHandler() {
		this.deviceService = SpringContainer.getBean(DeviceService.class);
		this.deviceGroupService = SpringContainer.getBean(DeviceGroupService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof DeviceRequestMessage) {
			
		
			DeviceRequestMessage message = (DeviceRequestMessage) msg;
			
			Device d = new Device(message.getDeviceip(),message.getDevicename(),message.getAutograph(),message.getPassword(),message.getDevicemac());
			d = deviceService.checkDevice(d);
			// 给用户响应验证正确登录成功
			ConnectContainer.INSTANCE.deviceOnLine(d.getDeviceid(), ctx.channel());
			DeviceResponseMessage result = new DeviceResponseMessage(true);
			
			result.setDevice(d);
			ctx.writeAndFlush(result);
			//loadUserDevices(ctx.channel(), d,message);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		if (cause instanceof ReadTimeoutException || cause instanceof IOException) {
			String id = ctx.channel().id().asLongText();
			
			String deviceid = ConnectContainer.INSTANCE.getDeviceIdByMark(id);
			Device d = deviceService.getById(deviceid);
			System.out.println(d.getDevicename() + ":下线了");
			ConnectContainer.INSTANCE.downDeviceMachineByMark(id);
			
			
			
			//notifyUserFriendsOffline(d);
			//notifyGroupMemberOffline(d);
			ctx.close();
		} else {
			ctx.fireExceptionCaught(cause);
		}
	}
	
	
}
