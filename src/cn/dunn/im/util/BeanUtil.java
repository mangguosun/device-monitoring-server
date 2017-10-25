package cn.dunn.im.util;

import java.io.File;

import javax.servlet.ServletContext;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class BeanUtil implements ApplicationContextAware {
	private static ApplicationContext applicationContext;
	private static ServletContext servletContext;

	public static void init(ServletContext paramServletContext) {
		servletContext = paramServletContext;
	}

	public void setApplicationContext(ApplicationContext paramApplicationContext) throws BeansException {
		applicationContext = paramApplicationContext;
	}

	public static ApplicationContext getContext() {
		return applicationContext;
	}

	public static ServletContext getServletContext() throws Exception {
		return servletContext;
	}

	public static <T> T getBean(Class<T> paramClass) {
		return applicationContext.getBean(paramClass);
	}

	public static Object getBean(String paramString) {
		return applicationContext.getBean(paramString);
	}

	public static String getAppAbsolutePath() {
		return servletContext.getRealPath("/");
	}

	public static String getRealPath(String paramString) {
		return servletContext.getRealPath(paramString);
	}

	public static String getClasspath() {
		String str1 = Thread.currentThread().getContextClassLoader().getResource("").getPath();
		String str2 = "";
		if ("\\".equals(File.separator)) {
			str2 = str1.substring(1);
			str2 = str2.replace("/", "\\");
		}
		if ("/".equals(File.separator)) {
			str2 = str1.substring(1);
			str2 = str2.replace("\\", "/");
		}
		return str2;
	}
}