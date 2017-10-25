package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.Device;

@Repository
public class DeviceDao extends BaseDao<String, Device> {

	protected Class<Device> getEntity() {
		return Device.class;
	}

	public Device updateDeviceInfo(Device device) {
		getSqlSession().update(getEntity().getName() + ".updateDeviceInfo", device);
		return device;
	}
}
