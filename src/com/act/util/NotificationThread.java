package com.act.util;

import com.act.insurance.InsuranceUI;

import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.Notification.Type;

public class NotificationThread extends Thread {
	
	private String notification;
	private Type type;

	public NotificationThread(String notification, Type type){
		this.notification = notification;
		this.type = type;
	}

	@Override
	public void run() {
		synchronized (((InsuranceUI)UI.getCurrent())) {
			Notification.show(notification, type);			
		}
		((InsuranceUI)UI.getCurrent()).getPusher().push();
	}


	
	
}
