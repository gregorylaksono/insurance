package com.act.main;

import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Label;
import com.vaadin.ui.VerticalLayout;

public class UserHeaderComponent extends VerticalLayout{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2617454644086299947L;

	public UserHeaderComponent(){
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
		layout.setSpacing(true);
		
		Label name = new Label("<b>Gregory Laksono</b>");
		Label username = new Label("<i>greg.laksono@gmail.com</i>");
		Label company = new Label("<i>ACT</i>");
		
		name.setContentMode(ContentMode.HTML);
		username.setContentMode(ContentMode.HTML);
		company.setContentMode(ContentMode.HTML);
		
		layout.addComponent(name);
		layout.addComponent(username);
		layout.addComponent(company);
		//----------------------------------------				
		addComponent(layout);
		setComponentAlignment(layout, Alignment.TOP_RIGHT);
		
	}

}
