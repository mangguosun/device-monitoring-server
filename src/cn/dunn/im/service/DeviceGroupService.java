package cn.dunn.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.DeviceGroupDao;
import cn.dunn.im.model.Device;
import cn.dunn.im.model.DeviceGroup;

@Service
public class DeviceGroupService extends BaseService<String, DeviceGroup> {
	@Resource
	private DeviceGroupDao deviceGroupDao;

	@Override
	protected BaseDao<String, DeviceGroup> getEntityDao() {
		return deviceGroupDao;
	}
	/**
	 * 获取所有分组
	 * 
	 * @param userid
	 * @return
	 */
	public List<DeviceGroup> getGroupAll() {
		return getList("getAll", null);
	}
	/**
	 * 获取我的分组
	 * 
	 * @param userid
	 * @return
	 */
	public List<DeviceGroup> getGroup(String deviceid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceid", deviceid);
		return getList("getAll", map);
	}
	/**
	 * 获取分组Id下的所有设备
	 * 
	 * @param groupid
	 * @return
	 */
	public List<Device> getGroupAllDevice(String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("groupid", groupid);
		return getList("getGroupAllDevice", map, Device.class);
	}

	/**
	 * 获取分组Id下的所有好友
	 * 
	 * @param userid
	 * @param groupid
	 * @return
	 */
	public List<Device> getGroupDevice(String userid, String groupid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("userid", userid);
		map.put("groupid", groupid);
		return getList("getGroupDevice", map, Device.class);
	}

}
