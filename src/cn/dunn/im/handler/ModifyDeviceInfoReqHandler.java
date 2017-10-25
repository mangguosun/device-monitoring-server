package cn.dunn.im.handler;

import io.netty.channel.ChannelHandlerAdapter;
import io.netty.channel.ChannelHandlerContext;

import java.io.File;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.container.SpringContainer;
import cn.dunn.im.model.Device;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.ModifyDeviceInfoReq;
import cn.dunn.im.model.ModifyDeviceInfoResp;
import cn.dunn.im.model.ModifyUserInfoReq;
import cn.dunn.im.model.ModifyUserInfoResp;
import cn.dunn.im.model.NotifyMessage;
import cn.dunn.im.model.NotifyMessageDevice;
import cn.dunn.im.model.User;
import cn.dunn.im.service.DeviceNexusService;
import cn.dunn.im.service.DeviceService;
import cn.dunn.im.service.FriendNexusService;
import cn.dunn.im.service.UserService;

/**
 * 修改用户信息请求处理
 * 
 * @author Administrator
 *
 */
public class ModifyDeviceInfoReqHandler extends ChannelHandlerAdapter {
	private DeviceService deviceService;
	private DeviceNexusService deviceNexusService;

	public ModifyDeviceInfoReqHandler() {
		deviceService = SpringContainer.getBean(DeviceService.class);
		deviceNexusService = SpringContainer.getBean(DeviceNexusService.class);
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof ModifyDeviceInfoReq) {
			ModifyDeviceInfoReq req = (ModifyDeviceInfoReq) msg;
			ModifyDeviceInfoResp resp = new ModifyDeviceInfoResp();
			Device device1 = null;
			try {
				Device device = deviceService.updateDeviceInfo(req.getDevice());
				device1 = deviceService.getById(device.getDeviceid());
				resp.setDevice(device1);
				resp.setSuccess(true);
			} catch (Exception e) {
				e.printStackTrace();
				resp.setSuccess(false);
				resp.setNote(e.getMessage());
			}
			// TODO 通知好友信息修改
			List<Device> devices = deviceService.getAll(new HashMap<String, Object>());

			for (Device device2 : devices) {
				if (ConnectContainer.INSTANCE.getOnLineDevices().contains(device2.getDeviceid())) {
					NotifyMessageDevice notifyMessageDevice = new NotifyMessageDevice(NotifyMessageDevice.STATUS_MODIFY);
					notifyMessageDevice.setType(Message.TYPE_CHAT);
					notifyMessageDevice.setBody(device1);
					ConnectContainer.INSTANCE.getChannelByDeviceId(device2.getDeviceid()).writeAndFlush(notifyMessageDevice);
				}
			}
			ctx.writeAndFlush(resp);
		} else {
			ctx.fireChannelRead(msg);
		}
	}
}
