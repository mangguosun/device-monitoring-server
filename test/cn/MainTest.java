package cn;

import static org.junit.Assert.*;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;

import javax.annotation.Resource;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import cn.dunn.im.model.ChatGroup;
import cn.dunn.im.model.FriendGroup;
import cn.dunn.im.model.Message;
import cn.dunn.im.model.User;
import cn.dunn.im.service.ChatGroupService;
import cn.dunn.im.service.FriendGroupService;
import cn.dunn.im.service.FriendNexusService;
import cn.dunn.im.service.MessageService;
import cn.dunn.im.util.ImageUtils;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:application-core.xml")
public class MainTest {
	@Resource
	private MessageService messageService;

	@Resource
	private FriendGroupService friendGroupService;

	@Resource
	private ChatGroupService chatGroupService;

	@Resource
	private FriendNexusService friendNexusService;

	@Test
	public void test1() throws Exception {
		List<Message> unReadMessage = messageService.getUnReadMessage("zhangsan", "lisi");
		for (Message message : unReadMessage) {
			System.out.print(message.getBody());
			System.out.println("-->: " + message.getUser().getUsername());
		}
	}

	@Test
	public void test2() throws Exception {
		int count = messageService.getUnReadMessageCount("zhangsan", "lisi");
		System.out.println(count);
	}

	@Test
	public void test3() throws Exception {
		List<FriendGroup> myGroup = friendGroupService.getMyGroup("zhangsan");
		System.out.println(myGroup.get(0).getGroupName());
	}

	@Test
	public void test4() throws Exception {
		List<User> list = friendGroupService.getGroupFriend("zhangsan", "jiaren");
		System.out.println(list.get(0).getNickname());
	}

	@Test
	public void test5() throws Exception {
		List<ChatGroup> myChatGroup = chatGroupService.getMyChatGroup("lisi");
		System.out.println(myChatGroup.get(0).getChatGroupName());
	}

	@Test
	public void test6() throws Exception {
		List<Message> messages = messageService.getGroupUnReadMessage("lisi", "zhufang");
		System.out.println(messages.get(0).getUser().getUsername());
	}

	@Test
	public void test7() throws Exception {
		int count = messageService.getGroupUnReadMessageCount("lisi", "zhufang");
		System.out.println(count);
	}

	@Test
	public void test8() throws Exception {
		List<Message> message = messageService.getHistoryMessage(200L, "lisi", "zhangsan");
		System.out.println(message.size());
		System.out.println(message.get(0).getUser().getUsername());
	}

	@Test
	public void test9() throws Exception {
		List<Message> messages = messageService.getChatGroupHistoryMessage(1111L, "zhufang");
		for (Message message : messages) {
			System.out.println(message.getBody() + "---->" + message.getUser().getUsername());
		}
	}

	@Test
	public void test10() throws Exception {
		List<User> friends = friendNexusService.getAllFriends("2");
		System.out.println(friends);
	}

	@Test
	public void test11() throws Exception {
		System.err.println(ImageUtils.getMd5ByFile(new File("E:/WorkSpace/IMServer/resources/resource/userImg/1000个常用嗾标-592.png")));
	}
	
	@Test
	public void test12() throws Exception {
		String[] split = "asd_sdas".split("_");
		System.out.println(split.length);
	}
	public static void main(String[] args) throws URISyntaxException {
		URL resource = Thread.currentThread().getContextClassLoader().getResource("");
		File file = new File((resource.toString()+"resource/userImg/4_a2701286709542b858dfdc3b2f49b59.png").toString().replace("file:\\", ""));
		System.out.println((resource.toString()+"resource/userImg/4_a2701286709542b858dfdc3b2f49b59.png").toString().replace("file:\\", ""));
		System.out.println(file.exists());
		System.out.println(file.getPath());
		System.out.println(resource);
	}
}
