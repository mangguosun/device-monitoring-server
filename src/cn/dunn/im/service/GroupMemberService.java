package cn.dunn.im.service;

import java.util.List;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.dunn.im.dao.GroupMemberDao;
import cn.dunn.im.model.GroupMember;

@Service
public class GroupMemberService {
	@Resource
	private GroupMemberDao groupMemberDao;

	public void save(GroupMember groupMember) {
		groupMemberDao.save(groupMember);
	}

	public List<GroupMember> getAll() {
		return groupMemberDao.getAll(null);
	}
}
