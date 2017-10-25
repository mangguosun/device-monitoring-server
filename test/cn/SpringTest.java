package cn;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.dunn.im.model.ChatGroup;
import cn.dunn.im.model.FriendGroup;
import cn.dunn.im.model.FriendNexus;
import cn.dunn.im.model.GroupMember;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;
import cn.dunn.im.service.FriendGroupService;
import cn.dunn.im.service.FriendNexusService;
import cn.dunn.im.service.GroupMemberService;
import cn.dunn.im.service.MessageService;
import cn.dunn.im.service.UserService;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-core.xml")
public class SpringTest {

	@Resource
	private GroupMemberService groupMemberService;
	@Resource
	private ChatGroupService chatGroupService;
	@Resource
	private MessageService messageService;
	@Resource
	private UserService userService;
	@Resource
	private FriendNexusService friendNexusService;
	@Resource
	private FriendGroupService friendGroupService;

	@Test
	public void test1() throws Exception {
		Message msg = new Message();
		msg.setBody("11");
		msg.setTo("123465");
		msg.setType(Message.TYPE_CHAT);
		msg.setFrom("454545");
		msg.setCreateTime(new Date().getTime());
		messageService.save(msg);
	}

	@Test
	public void test2() throws Exception {
		Map<String, Object> condition = new HashMap<String, Object>();
		condition.put("from", "1");
		condition.put("to", "1");
		condition.put("type", "chat");
		condition.put("createTime", 22323L);
		List<Message> all = messageService.getAll(condition);
		System.out.println(all);
	}

	@Test
	public void test3() throws Exception {
		User user = new User();
		user.setUsername("张三12");
		user.setPassword("123456");
		user.setAutograph("今天打牌输钱了");
		if (userService.getUserByUsername(user.getUsername()) == null) {
			userService.save(user);
		}
	}

	@Test
	public void test4() throws Exception {
		User userByUsername = userService.getUserByUsername("张三12");
		System.out.println(userByUsername);
	}

	@Test
	public void test5() throws Exception {
		FriendNexus nexus = new FriendNexus();
		nexus.setSelf("882");
		nexus.setAnother("99");
		if (!friendNexusService.isFriendNexus(nexus)) {
			friendNexusService.save(nexus);
		}
	}

	@Test
	public void test6() throws Exception {
		FriendNexus nexus = new FriendNexus();
		nexus.setSelf("882");
		nexus.setAnother("99");
		boolean friendNexus = friendNexusService.isFriendNexus(nexus);
		System.out.println(friendNexus);
	}

	@Test
	public void test7() throws Exception {
		FriendGroup friendGroup = new FriendGroup();
		friendGroup.setGroupName("家人");
		friendGroup.setPertain("9527");
		friendGroupService.save(friendGroup);
	}

	@Test
	public void test8() throws Exception {
		List<FriendGroup> list = friendGroupService.getAll(null);
		System.out.println(list);
	}

	@Test
	public void test9() throws Exception {
		ChatGroup chatGroup = new ChatGroup();
		chatGroup.setChatGroupName("租房群");
		chatGroup.setHeadImg("sss.jsp");
		chatGroup.setTheme("租房网提供");
		chatGroupService.save(chatGroup);
	}

	@Test
	public void test10() throws Exception {
		List<ChatGroup> list = chatGroupService.getAll(null);
		System.out.println(list);

	}

	@Test
	public void test11() throws Exception {
		GroupMember groupMember = new GroupMember();
		groupMember.setChatGroupId("111");
		groupMember.setLastReadTime(new Date().getTime());
		groupMember.setMemberId("9527");
		groupMemberService.save(groupMember);
	}

	@Test
	public void test12() throws Exception {
		List<GroupMember> all = groupMemberService.getAll();
		System.out.println(all);

	}
}
