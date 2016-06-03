package com.act.insurance;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.act.login.LoginPage;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("insurance")
public class InsuranceUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = InsuranceUI.class, widgetset = "com.act.insurance.widgetset.InsuranceWidgetset")
	public static class Servlet extends VaadinServlet {
	}
	
	private String sessionId = null;
	private VerticalLayout layout;
	private Component header;
	
	@Override
	protected void init(VaadinRequest request) {
		layout = new VerticalLayout();
		layout.setHeight(100, Unit.PERCENTAGE);
		
		LoginPage loginPage = new LoginPage();
		layout.addComponent(loginPage);
		layout.setComponentAlignment(loginPage, Alignment.MIDDLE_CENTER);
		
		setContent(layout);
	}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	
	public void setInsidePage(Component page){
		layout.removeAllComponents();
		if(header!= null){
			layout.addComponent(header);
			layout.setExpandRatio(header, 0.0f);
		}
		layout.addComponent(page);
		layout.setExpandRatio(page, 1.0f);
	}
	
	public void setHeader(Component header){
		this.header = header;
	}

}