package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.DeviceNexus;

@Repository
public class DeviceNexusDao extends BaseDao<String, DeviceNexus> {

	@Override
	protected Class<DeviceNexus> getEntity() {
		return DeviceNexus.class;
	}

}
