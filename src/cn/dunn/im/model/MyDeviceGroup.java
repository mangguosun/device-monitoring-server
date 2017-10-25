package cn.dunn.im.model;

import java.io.Serializable;
import java.util.List;

/**
 * 我的好友列表
 * 
 * @author Administrator
 * 
 */
//@XmlRootElement
public class MyDeviceGroup implements Serializable {

	private static final long serialVersionUID = 1L;
	private List<DeviceGroup> deviceGroup;
	
	private String userid;
	
	public MyDeviceGroup() {
	}

	public MyDeviceGroup(List<DeviceGroup> deviceGroup) {
		this.deviceGroup = deviceGroup;
	}

	public List<DeviceGroup> getDeviceGroup() {
		return deviceGroup;
	}

	public void setDeviceGroup(List<DeviceGroup> deviceGroup) {
		this.deviceGroup = deviceGroup;
	}
	
	public String getUserid() {
		return userid;
	}

	public void setUserid(String userid) {
		this.userid = userid;
	}
	

}
