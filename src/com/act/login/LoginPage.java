package com.act.login;


import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.act.insurance.InsuranceUI;
import com.act.insurance.model.User;
import com.act.main.CommodityInsuranceTab;
import com.act.main.Main;
import com.act.main.UserHeaderComponent;
import com.act.util.CallJSONAction;
import com.act.util.CallSOAPAction;
import com.act.util.NotificationThread;
import com.act.util.CallJSONAction.IJSonCallbackListener;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.vaadin.data.Item;

import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;

import com.act.insurance.InsuranceUI;
import com.act.main.CommodityInsuranceTab;
import com.act.util.CallSOAPAction;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;

import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
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

				public void handleResult(SoapObject data,  String StatusCode) {
					String sessionId = data.getProperty("sessionId").toString();
					String ca_id = data.getProperty("ca_id").toString();
					String ap_3lc= data.getProperty("ap_3lc").toString();
					String userId = data.getProperty("userId").toString();
					String sAddId= data.getProperty("addId").toString();
					Integer addId = Integer.parseInt(sAddId);
					
					User user = new User();
					user.setSessionId(sessionId);
					user.setAp_3lc(ca_id);
					user.setCa_id(ap_3lc);
					user.setAddId(addId);
					user.setUserId(userId);	
					
//					new NotificationThread("Login is success", Type.HUMANIZED_MESSAGE).start();
					((InsuranceUI)UI.getCurrent()).setUser(user);
//					new NotificationThread("Please wait. While loading commodities...", Type.HUMANIZED_MESSAGE).start();
					((InsuranceUI)UI.getCurrent()).initCommodities();
//					new NotificationThread("Please wait. While loading currencies...", Type.HUMANIZED_MESSAGE).start();
					((InsuranceUI)UI.getCurrent()).initCurrencies();
//					new NotificationThread("Finish", Type.HUMANIZED_MESSAGE).start();
					
					Component userProfile = new UserHeaderComponent();
					((InsuranceUI)UI.getCurrent()).setHeader(userProfile);
					//-----------------------------------------------------
					
					//-----------------------------------------------------					
					((InsuranceUI)UI.getCurrent()).setInsidePage(new Main());
					
				}
				


				public void handleResult(SoapObject data) {
					String sessionId = data.getProperty("sessionId").toString();
					((InsuranceUI)UI.getCurrent()).setSessionId(sessionId);
					((InsuranceUI)UI.getCurrent()).setInsidePage(new CommodityInsuranceTab());
					
				}



				@Override
				public void handleError(String statusCode) {
					Notification.show("User name/password is not found", Type.ERROR_MESSAGE);
					
				}

			};
			
			new CallSOAPAction(m, "login",callback );
			
		}
	};

	private PasswordField password;

	private TextField email;
	
	private Label utilLabel;
	
	public LoginPage(){
		setHeight(120, Unit.PIXELS);
		setWidth(300, Unit.PIXELS);
		createContents();
	}

	private void createContents() {
		FormLayout form = new FormLayout();
		
		email = new TextField("Email");
		password = new PasswordField("Password");
		
		email.setWidth(60, Unit.PERCENTAGE);
		password.setWidth(60, Unit.PERCENTAGE);
		
		utilLabel = new Label();
		
		form.addComponent(utilLabel);
		form.addComponent(email);
		form.addComponent(password);
		form.setWidth(null);
		
		//----------------------------------------------
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setHeight(null);
		buttonLayout.setWidth(209, Unit.PIXELS);
		buttonLayout.setSpacing(true);
		Button login = new Button("Login");
		login.setWidth(null);
		buttonLayout.addComponent(login);
		//--------------------------------------------------		
		login.addStyleName(ValoTheme.BUTTON_PRIMARY);
		//--------------------------------------------------
		form.addComponent(login);
		addComponent(form);

		setExpandRatio(form, 1.0f);

		
		//--------------------------------------------------
		
		login.addClickListener(loginListener);

	}


}
