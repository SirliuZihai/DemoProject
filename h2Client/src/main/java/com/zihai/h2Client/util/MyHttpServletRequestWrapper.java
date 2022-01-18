package com.zihai.h2Client.util;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

public class MyHttpServletRequestWrapper extends HttpServletRequestWrapper {
	private static Logger logger = LoggerFactory.getLogger(MyHttpServletRequestWrapper.class);
	private byte[] body; // 用于保存读取body中数据
	private static Set<String> contentTypes;
	static {
		contentTypes = new HashSet<>();
		contentTypes.add("application/json");
		//contentTypes.add("application/x-www-form-urlencoded");
		//contentTypes.add("multipart/form-data");
	}

	public MyHttpServletRequestWrapper(HttpServletRequest request) {
		super(request);
		// 读取请求的数据保存到本类当中
		try {
			logger.info("header contentType=={}",request.getHeader("Content-Type"));
			if(contentTypes.contains(request.getHeader("Content-Type"))){
				body = IOUtils.toByteArray(request.getReader(), StandardCharsets.UTF_8);
			}
		} catch (Exception e) {
			logger.error("初始化MyHttpServletRequestWrapper异常："+e.getMessage());
		}

	}
	public String getBodyStr() {
		return new String(body);
	}

	// 覆盖（重写）父类的方法
	@Override
	public BufferedReader getReader() throws IOException {
		return new BufferedReader(new InputStreamReader(getInputStream()));
	}

	// 覆盖（重写）父类的方法
	@Override
	public ServletInputStream getInputStream() throws IOException {
		final ByteArrayInputStream bais = new ByteArrayInputStream(body);
		return new ServletInputStream() {
			@Override
			public int read() throws IOException {
				return bais.read();
			}

			@Override
			public boolean isFinished() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isReady() {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public void setReadListener(ReadListener listener) {
				// TODO Auto-generated method stub

			}
		};
	}

	/**
	 * 获取body中的数据
	 *
	 * @return
	 */
	public byte[] getBody() {
		return body;
	}

	/**
	 * 把处理后的参数放到body里面
	 *
	 * @param body
	 */
	public void setBody(byte[] body) {
		this.body = body;
	}


}
