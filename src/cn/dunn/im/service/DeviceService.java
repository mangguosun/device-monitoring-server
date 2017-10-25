package cn.dunn.im.service;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.DeviceDao;
import cn.dunn.im.model.Device;
import cn.dunn.im.model.DeviceNexus;
import cn.dunn.im.model.User;

@Service
public class DeviceService extends BaseService<String, Device> {
	@Resource
	private DeviceDao deviceDao;

	@Override
	protected BaseDao<String, Device> getEntityDao() {
		return deviceDao;
	}

	public Device getByDevicename(String devicename) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("devicename", devicename);
		Device device = getUnique("getByDevicename", map);
		return device;
	}
	

	public Device getById(String deviceid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceid", deviceid);
		return deviceDao.getUnique("getById", map);
	}

	public Device getByIp(String deviceip) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceip", deviceip);
		return deviceDao.getUnique("getByIp", map);
	}
	public Device getByMac(String devicemac) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("devicemac", devicemac);
		return deviceDao.getUnique("getByMac", map);
	}
	public Device updateDeviceInfo(Device device) {
		return deviceDao.updateDeviceInfo(device);
	}

	public Device SaveDevice(Device device) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceip", device.getDeviceip());
		map.put("devicemac", device.getDevicemac());
		map.put("devicename", device.getDevicename());
		map.put("password", device.getPassword());
		map.put("autograph", device.getAutograph());
		map.put("registerTime", device.getRegisterTime());
		getUnique("save", map);

		return device;
	}

	public DeviceNexus SaveDeviceNexus(DeviceNexus deviceNexus) {
		Map<String, Object> map = new HashMap<String, Object>();

		deviceNexus.setAnother("3");
		deviceNexus.setGroupId("xitong");

		map.put("self", deviceNexus.getSelf());
		map.put("another", deviceNexus.getAnother());
		map.put("groupid", deviceNexus.getGroupId());
		map.put("lastreadtime", deviceNexus.getLastReadTime());

		getUnique("savednexu", map);

		return deviceNexus;
	}

	/**
	 * 验证成功返回持久化的user 否者返回null
	 * 
	 * @param user
	 * @return
	 */
	public Device checkDevice(Device device) {
		Device d = getByMac(device.getDevicemac());
		if (d != null) {
			if(device.getDeviceip().equals(d.getDeviceip())) {
				return d;
			}else {
				
				device.setDeviceid(d.getDeviceid());
				Device s_d = updateDeviceInfo(device);
				Device e = getByIp(s_d.getDeviceip());
				return e;
			}
		} else {

			Device s_d = SaveDevice(device);
			Device e = getByIp(s_d.getDeviceip());
			DeviceNexus s_d_d = new DeviceNexus();
			s_d_d.setSelf(e.getDeviceid());
			SaveDeviceNexus(s_d_d);
			return e;
		}
	}

}
