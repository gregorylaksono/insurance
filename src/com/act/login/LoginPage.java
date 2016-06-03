package com.act.login;

import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;

import com.act.insurance.InsuranceUI;
import com.act.main.CommodityInsuranceTab;
import com.act.util.CallSOAPAction;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
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
	
	private ClickListener loginListener = new ClickListener() {
		
		@Override
		public void buttonClick(ClickEvent event) {
			String sEmail = email.getValue().trim();
			String sPassword  = password.getValue().trim();
			
			if(sEmail.isEmpty() || sPassword.isEmpty()){
				Notification.show("Please input email and pasword", Type.ERROR_MESSAGE);
				return;
			}
			
			LinkedHashMap<String, Object> m = new LinkedHashMap<>();
			m.put("username", sEmail);
			m.put("password", sPassword);
			
			ISOAPResultCallBack callback = new ISOAPResultCallBack() {
				
				@Override
				public void handleResult(SoapObject data) {
					String sessionId = data.getProperty("sessionId").toString();
					((InsuranceUI)UI.getCurrent()).setSessionId(sessionId);
					((InsuranceUI)UI.getCurrent()).setInsidePage(new CommodityInsuranceTab());
					
				}
				
				@Override
				public void handleError() {
					
				}
			};
			
			new CallSOAPAction(m, "login",callback );
			
		}
	};

	private PasswordField password;

	private TextField email;
	
	public LoginPage(){
		setHeight(120, Unit.PIXELS);
		setWidth(300, Unit.PIXELS);
		createContents();
	}

	private void createContents() {
		FormLayout form = new FormLayout();
		
		email = new TextField("Email");
		password = new PasswordField("Password");
		
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
		
		login.addClickListener(loginListener);
	}

}
