package com.zihai.h2Client.Listener;

import com.zihai.h2Client.util.Constant;
import org.apache.commons.io.monitor.FileAlterationListenerAdaptor;
import org.apache.commons.io.monitor.FileAlterationObserver;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;
import org.springframework.web.context.ConfigurableWebApplicationContext;

import java.io.File;

@Component
public class FileListener extends  FileAlterationListenerAdaptor{
	private ApplicationContext applicationContext;
	private static final Logger LOGGER = Logger.getLogger(FileListener.class);
	
	public FileListener(ApplicationContext object){
		this.applicationContext = object;
	}
	
	@Override
	public void onDirectoryChange(File file) {
		// TODO Auto-generated method stub
		LOGGER.info("onDirectoryChange: " + file.getName());
	}

	@Override
	public void onDirectoryCreate(File file) {
		// TODO Auto-generated method stub
		LOGGER.info("onDirectoryCreate: " + file.getName());
	}
	/**
	 * 目录删除
	 */
	@Override
	public void onDirectoryDelete(File file) {
		// TODO Auto-generated method stub
		LOGGER.info("onDirectoryDelete: " + file.getName());
	}
	@Override
	public void onFileChange(File file) {
		// TODO Auto-generated method stub
		//改变的时候重新加载数据
		LOGGER.info("change file: " + file.getName());
		this.applicationContext.publishEvent(new NetWorkEvent(Constant.LISTENER_EVENT_TYPE.DOWNLOAD_QUEUE_CLEAN));
		//reload properties
	}

	@Override
	public void onFileCreate(File file) {
		// TODO Auto-generated method stub
		LOGGER.info("onFileCreate: " + file.getName());
	}

	@Override
	public void onFileDelete(File file) {
		// TODO Auto-generated method stub
		LOGGER.info("onFileDelete: " + file.getName());
	}

	@Override
	public void onStart(FileAlterationObserver arg0) {
		// TODO Auto-generated method stub
		super.onStart(arg0);
	}

	@Override
	public void onStop(FileAlterationObserver arg0) {
		// TODO Auto-generated method stub
		super.onStop(arg0);
	}

}
