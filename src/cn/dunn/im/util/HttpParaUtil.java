package cn.dunn.im.util;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSON;

public class HttpParaUtil {
	public static Map<String, String> paraSet(Object obj) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("text", JSON.toJSONString(obj));
		return map;
	}
}
