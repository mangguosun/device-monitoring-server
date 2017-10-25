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

public class DeviceGroupHandler extends ChannelHandlerAdapter {
	protected Logger logger = LoggerFactory.getLogger(LoginHandler.class);

	private DeviceGroupService deviceGroupService;
	
	
	public DeviceGroupHandler() {
		this.deviceGroupService = SpringContainer.getBean(DeviceGroupService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof MyDeviceGroup) {
			
		
			MyDeviceGroup message = (MyDeviceGroup) msg;
			List<DeviceGroup> myGroup = deviceGroupService.getGroupAll();
			
			for (DeviceGroup deviceGroup : myGroup) {
				
				List<Device> devices = null;
				
				String uid = message.getUserid();
				 
				if(uid==null) {
					devices = deviceGroupService.getGroupAllDevice(deviceGroup.getId()); 
				}else {
					// 加载用户分组中的设备
					 devices = deviceGroupService.getGroupDevice(uid,deviceGroup.getId());
				}
				for (Device device : devices) {
					// 加用户与好友的未读消息数量
					device.setUnReadMessageCount(0);
					// 设置用户好友是否已经在线上
					device.setIsOnline(ConnectContainer.INSTANCE.deivceisOnline(device.getDeviceid()));
				}
				// 为该分组设置设置
				deviceGroup.getDevices().addAll(devices);
			}
			ctx.writeAndFlush(new MyDeviceGroup(myGroup));
			
		} else {
			ctx.fireChannelRead(msg);
		}
	}
	
	
	
	
}
