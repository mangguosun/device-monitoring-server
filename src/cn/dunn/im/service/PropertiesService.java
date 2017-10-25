package cn.dunn.im.service;

import java.io.IOException;
import java.util.Properties;

import org.springframework.stereotype.Service;

@Service
public class PropertiesService {
	private Properties properties;

	public PropertiesService() {
		properties = new Properties();
		try {
			properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("app.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 从classpath:app.properties获取属性
	 * 
	 * @param key
	 * @return
	 */
	public String getProperty(String key) {
		return properties.getProperty(key);
	}
}
