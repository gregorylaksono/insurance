package com.act.insurance;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import com.act.login.LoginPage;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("insurance")
public class InsuranceUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = false, ui = InsuranceUI.class, widgetset = "com.act.insurance.widgetset.InsuranceWidgetset")
	public static class Servlet extends VaadinServlet {
	}

	@Override
	protected void init(VaadinRequest request) {
		final VerticalLayout layout = new VerticalLayout();
		layout.setMargin(true);
		layout.setHeight(100, Unit.PERCENTAGE);
		
		LoginPage loginPage = new LoginPage();
		layout.addComponent(loginPage);
		layout.setComponentAlignment(loginPage, Alignment.MIDDLE_CENTER);
		
		setContent(layout);
	}

}