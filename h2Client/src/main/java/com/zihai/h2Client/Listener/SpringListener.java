package com.zihai.h2Client.Listener;

import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Service;

@Service
public class SpringListener implements ApplicationListener<ApplicationEvent> {

	@Override
	public void onApplicationEvent(ApplicationEvent event) {
		// TODO Auto-generated method stub
		
		if(event instanceof NetWorkEvent){
			NetWorkEvent netWorkEvent = (NetWorkEvent) event;
			netWorkEvent.handle();//绑定注册到云端
		}
	}

}
