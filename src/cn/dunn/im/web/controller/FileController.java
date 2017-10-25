package cn.dunn.im.web.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import cn.dunn.im.constant.ConnectConstants;
import cn.dunn.im.util.ImageUtils;

/**
 * 
 * @author Administrator
 *
 */
@Controller
@RequestMapping("file")
public class FileController {
	/**
	 * 
	 * @param status
	 *            online 正常图片,offline灰度图片
	 * @param fileName
	 * @param request
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping("getHeadImg")
	public void getHeadImg(Integer width, Integer height, String status, String fileName, HttpServletRequest request, HttpServletResponse response) throws IOException {
		ServletOutputStream outputStream = response.getOutputStream();
		File file = new File(ConnectConstants.USER_IMAGE_PATH + fileName);
		if (!file.exists()) {
			ByteArrayInputStream inputStream = ImageUtils.scale(ConnectConstants.USER_IMAGE_PATH + "default.png", height, width);
			if ("offline".equals(status)) {
				inputStream = ImageUtils.convertGray(ConnectConstants.USER_IMAGE_PATH + "default.png", inputStream);
			}
			IOUtils.copy(inputStream, outputStream);
		} else {
			ByteArrayInputStream inputStream = ImageUtils.scale(file.getAbsolutePath(), height, width);
			if ("offline".equals(status)) {
				inputStream = ImageUtils.convertGray(ConnectConstants.USER_IMAGE_PATH + "default.png", inputStream);
			}
			IOUtils.copy(inputStream, outputStream);
		}
	}
}
