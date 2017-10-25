package cn.dunn.im.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.BaseDao;
import cn.dunn.im.dao.DeviceNexusDao;
import cn.dunn.im.model.Device;
import cn.dunn.im.model.DeviceNexus;

@Service
public class DeviceNexusService extends BaseService<String, DeviceNexus> {
	@Resource
	private DeviceNexusDao deviceNexusDao;

	@Override
	protected BaseDao<String, DeviceNexus> getEntityDao() {
		return deviceNexusDao;
	}

	/**
	 * 是否是好友
	 * 
	 * @param nexus
	 * @return
	 */
	public boolean isDeviceNexus(DeviceNexus nexus) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("self", nexus.getSelf());
		map.put("another", nexus.getAnother());
		DeviceNexus deviceNexus = getUnique("isDeviceNexus", map);
		if (deviceNexus != null) {
			return true;
		}
		return false;
	}

	/**
	 * 保存好友关系
	 */
	public void save(DeviceNexus nexus) {
		deviceNexusDao.save(nexus);
		String another = nexus.getAnother();
		String self = nexus.getSelf();
		nexus.setSelf(another);
		nexus.setAnother(self);
		nexus.setId(UUID.randomUUID().toString());
		deviceNexusDao.save(nexus);
	}

	/**
	 * 获取用户的所有好友
	 * 
	 * @param userid
	 * @return
	 */
	public List<Device> getAllDevices(String deviceid) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("deviceid", deviceid);
		return deviceNexusDao.getList("getAllDevices", map, Device.class);
	}

}
