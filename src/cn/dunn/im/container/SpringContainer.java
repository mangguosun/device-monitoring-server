package cn.dunn.im.container;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

@Service
public class SpringContainer implements ApplicationContextAware {
	private static ApplicationContext applicationContext;

	public static void init() {
		applicationContext = new ClassPathXmlApplicationContext("classpath:application-core.xml");
	}

	public static <T> T getBean(Class<T> paramClass) {
		return applicationContext.getBean(paramClass);
	}

	public static Object getBean(String paramString) {
		return applicationContext.getBean(paramString);
	}

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		SpringContainer.applicationContext = applicationContext;
	}
}
