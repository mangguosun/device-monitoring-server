package cn.dunn.im.web.controller;

import io.netty.channel.Channel;

import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.dunn.im.container.ConnectContainer;
import cn.dunn.im.model.Message;
import cn.dunn.im.service.MessageService;

import com.alibaba.fastjson.JSON;

/**
 * 提供web服务的消息接口
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("messageProvider")
public class MessageProviderController extends BaseController {
	@Resource
	private MessageService messageService;

	@RequestMapping("sendMessage")
	@ResponseBody
	public String sendMessage(String text) {
		Message message = JSON.parseObject(text, Message.class);
		logger.info(message.toString());
		messageService.save(message);
		String to = message.getTo();
		Channel channel = ConnectContainer.INSTANCE.getChannelByUserId(to);
		if (channel != null) {
			channel.writeAndFlush(message);
		}
		return "发送成功";
	}

	@RequestMapping("loadHistoryMessage")
	@ResponseBody
	public String loadHistoryMessage(String text) {
		@SuppressWarnings("unchecked")
		Map<String, String> map = JSON.parseObject(text, Map.class);
		String selt = map.get("self");
		String another = map.get("another");
		String time = map.get("time");
		List<Message> historyMessage = messageService.getHistoryMessage(Long.parseLong(time), another, selt);
		return JSON.toJSONString(historyMessage);
	}
}
