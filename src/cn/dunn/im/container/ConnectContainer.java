package cn.dunn.im.container;

import io.netty.channel.Channel;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public enum ConnectContainer {
	INSTANCE;
	private Map<String, Channel> onLineUserConnect = new HashMap<String, Channel>();

	private Map<String, Channel> markChannelConnect = new HashMap<String, Channel>();

	private Map<String, String> markMapUsername = new HashMap<String, String>();

	private Map<String, String> userNameMapMark = new HashMap<String, String>();
	
	private Map<String, String> markMapDevicename = new HashMap<String, String>();

	private Map<String, String> deviceNameMapMark = new HashMap<String, String>();
	
	private Map<String, Channel> onLineDeviceConnect = new HashMap<String, Channel>();

	private Map<String, Channel> markDeviceChannelConnect = new HashMap<String, Channel>();

	private Set<String> webOnLineUser = new HashSet<String>();

	/**
	 * 通过用户名获取该用户对应的通道
	 * 
	 * @param userid
	 * @return
	 */
	public Channel getChannelByUserId(String userid) {
		return onLineUserConnect.get(userid);
	}

	/**
	 * 通过通道标志获取通道
	 * 
	 * @param mark
	 * @return
	 */
	public Channel getChannelByMark(String mark) {
		return markChannelConnect.get(mark);
	}

	/**
	 * 获取在线的所有用户
	 * 
	 * @return
	 */
	public Set<String> getOnLineUsers() {
		return onLineUserConnect.keySet();
	}

	/**
	 * 通过用户名下线
	 * 
	 * @param userid
	 */
	public void downMachineByUserId(String userid) {
		String mark = userNameMapMark.remove(userid);
		onLineUserConnect.remove(userid).close();
		markChannelConnect.remove(mark).close();
		markMapUsername.remove(mark);
	}

	/**
	 * web用户离线
	 * 
	 * @param userid
	 */
	public void webUserOffLine(String userid) {
		webOnLineUser.remove(userid);
	}

	/**
	 * web用户上线
	 * 
	 * @param userid
	 * @return
	 */
	public boolean webUserOnLine(String userid) {
		return webOnLineUser.add(userid);
	}

	/**
	 * web用户是否在线
	 * 
	 * @param userid
	 * @return
	 */
	public boolean webUserIsOnLine(String userid) {
		return webOnLineUser.contains(userid);
	}

	/**
	 * 通过通道标志下线
	 * 
	 * @param mark
	 */
	public void downMachineByMark(String mark) {
		String username = markMapUsername.remove(mark);
		onLineUserConnect.remove(username).close();
		markChannelConnect.remove(mark).close();
		userNameMapMark.remove(mark);
	}

	/**
	 * 用户上线
	 * 
	 * @param userid
	 * @param channel
	 */
	public void userOnLine(String userid, Channel channel) {
		markMapUsername.put(channel.id().asLongText(), userid);
		onLineUserConnect.put(userid, channel);
		markChannelConnect.put(channel.id().asLongText(), channel);
		userNameMapMark.put(userid, channel.id().asLongText());
	}

	/**
	 * 通过通道标识获得用户名
	 * 
	 * @param mark
	 * @return
	 */
	public String getUserIdByMark(String mark) {
		return markMapUsername.get(mark);
	}

	/**
	 * 获取所有通道
	 * 
	 * @return
	 */
	public Collection<Channel> getAllChannel() {
		return onLineUserConnect.values();
	}

	public boolean isOnline(String userid) {
		return onLineUserConnect.keySet().contains(userid);
	}
	
	
	
	
	
	
	
	
	
	
	
	/**
	 * 通过用户名获取该用户对应的通道
	 * 
	 * @param userid
	 * @return
	 */
	public Channel getChannelByDeviceId(String deviceid) {
		return onLineDeviceConnect.get(deviceid);
	}

	/**
	 * 通过通道标志获取通道
	 * 
	 * @param mark
	 * @return
	 */
	public Channel getChannelByDeviceMark(String mark) {
		return markDeviceChannelConnect.get(mark);
	}

	/**
	 * 获取在线的所有用户
	 * 
	 * @return
	 */
	public Set<String> getOnLineDevices() {
		return onLineDeviceConnect.keySet();
	}

	/**
	 * 通过用户名下线
	 * 
	 * @param userid
	 */
	public void downMachineByDeviceId(String deviceid) {
		String mark = deviceNameMapMark.remove(deviceid);
		onLineDeviceConnect.remove(deviceid).close();
		markDeviceChannelConnect.remove(mark).close();
		markMapDevicename.remove(mark);
	}


	/**
	 * 通过通道标志下线
	 * 
	 * @param mark
	 */
	public void downDeviceMachineByMark(String mark) {
		String username = markMapDevicename.remove(mark);
		onLineDeviceConnect.remove(username).close();
		markDeviceChannelConnect.remove(mark).close();
		deviceNameMapMark.remove(mark);
	}
	
	
	/**
	 * 设备上线
	 * 
	 * @param userid
	 * @param channel
	 */
	public void deviceOnLine(String deviceid, Channel channel) {
		markMapDevicename.put(channel.id().asLongText(), deviceid);
		onLineDeviceConnect.put(deviceid, channel);
		markDeviceChannelConnect.put(channel.id().asLongText(), channel);
		deviceNameMapMark.put(deviceid, channel.id().asLongText());
	}

	/**
	 * 通过通道标识获得设备名
	 * 
	 * @param mark
	 * @return
	 */
	public String getDeviceIdByMark(String mark) {
		return markMapDevicename.get(mark);
	}
	
	
	/**
	 * 获取所有通道
	 * 
	 * @return
	 */
	public Collection<Channel> getAllDeviceChannel() {
		return onLineDeviceConnect.values();
	}

	public boolean deivceisOnline(String deviceid) {
		return onLineDeviceConnect.keySet().contains(deviceid);
	}
	
	
}
