package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.DeviceGroup;

@Repository
public class DeviceGroupDao extends BaseDao<String, DeviceGroup> {

	@Override
	protected Class<DeviceGroup> getEntity() {
		return DeviceGroup.class;
	}

}
