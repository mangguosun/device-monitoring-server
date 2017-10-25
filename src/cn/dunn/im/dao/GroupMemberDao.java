package cn.dunn.im.dao;

import org.springframework.stereotype.Repository;

import cn.dunn.im.model.GroupMember;

@Repository
public class GroupMemberDao extends BaseDao<String, GroupMember> {

	@Override
	protected Class<GroupMember> getEntity() {
		return GroupMember.class;
	}

}
