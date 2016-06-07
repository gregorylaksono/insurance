package com.act.main;

import com.act.insurance.InsuranceUI;
import com.act.insurance.model.User;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

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
		
		Label name = new Label("<b>"+user.getFirstname()+" "+user.getFamilyname()+"</b>");
		Label username = new Label("<i>"+user.getUsername()+"</i>");
		username.setHeight(null);
		name.setHeight(100, Unit.PERCENTAGE);
		
		name.setContentMode(ContentMode.HTML);
		username.setContentMode(ContentMode.HTML);
		
		layout.addComponent(name);
		layout.addComponent(username);

		//----------------------------------------				
		addComponent(layout);
		setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
	}

}
