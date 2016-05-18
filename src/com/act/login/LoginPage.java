package com.act.login;

import com.act.main.Main;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.PasswordField;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;

public class LoginPage extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -5993298903068017656L;
	
	public LoginPage(){
		setHeight(120, Unit.PIXELS);
		setWidth(300, Unit.PIXELS);
		createContents();
	}

	private void createContents() {
		FormLayout form = new FormLayout();
		
		TextField email = new TextField("Email");
		PasswordField password = new PasswordField("Password");
		
		email.setWidth(100, Unit.PERCENTAGE);
		password.setWidth(100, Unit.PERCENTAGE);
		
		form.addComponent(email);
		form.addComponent(password);
		
		//----------------------------------------------
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setHeight(null);
		buttonLayout.setWidth(73, Unit.PERCENTAGE);
		buttonLayout.setSpacing(true);
		Button login = new Button("Login");
		Button register = new Button("Register");
		register.setWidth(null);
		login.setWidth(100, Unit.PERCENTAGE);
		buttonLayout.addComponent(register);
		buttonLayout.addComponent(login);
		//--------------------------------------------------		
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		//--------------------------------------------------
		
		addComponent(form);
		addComponent(buttonLayout);
		setExpandRatio(form, 1.0f);
		setExpandRatio(buttonLayout, 0.0f);
		setComponentAlignment(buttonLayout, Alignment.MIDDLE_RIGHT);
		
		//--------------------------------------------------
		
		login.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				UI.getCurrent().setContent(new Main());
			}
		});
	}

}
