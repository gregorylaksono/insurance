package com.act.main;

import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;

import com.act.insurance.InsuranceUI;
import com.act.insurance.model.User;
import com.act.util.CallSOAPAction;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.vaadin.server.Page;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.server.VaadinSession;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class UserHeaderComponent extends VerticalLayout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2617454644086299947L;
	private User user;

	public UserHeaderComponent(){
		this.user = ((InsuranceUI)UI.getCurrent()).getUser();
		createContents();
		
	}

	private void createContents() {
		MarginInfo info = new MarginInfo(true);
		info.setMargins(false, true, false, false);
		setMargin(info);
		setWidth(100, Unit.PERCENTAGE);
		setHeight(null);
		//----------------------------------------				
		VerticalLayout layout = new VerticalLayout();
		layout.setWidth(null);
		layout.setHeight(50, Unit.PIXELS);
		layout.setSpacing(false);
		
		Label name = new Label("<b>"+user.getAp_3lc()+"</b>");
		Label username = new Label("<i>"+user.getUserId()+"</i>");
		username.setHeight(null);
		name.setHeight(100, Unit.PERCENTAGE);
		
		Button signOut = new Button("Sign out");
		signOut.addStyleName(ValoTheme.BUTTON_BORDERLESS_COLORED);
		signOut.addStyleName("no-padding-left");
		signOut.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				User user = ((InsuranceUI)UI.getCurrent()).getUser();
				LinkedHashMap<String, Object> param = new LinkedHashMap<>();
				param.put("sessionId", user.getSessionId());
				ISOAPResultCallBack callback = new ISOAPResultCallBack() {
					
					@Override
					public void handleResult(SoapObject data, String StatusCode) {
						
					}
					
					@Override
					public void handleError(String StatusCode) {
						
					}
				};
				new CallSOAPAction(param, "logout", callback);
				VaadinSession.getCurrent().close();
				Page.getCurrent().reload();
			}
		});
		
		name.setContentMode(ContentMode.HTML);
		username.setContentMode(ContentMode.HTML);
		
		layout.addComponent(name);
		layout.addComponent(username);
		layout.addComponent(signOut);

		//----------------------------------------				
		addComponent(layout);
		setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
	}

}
