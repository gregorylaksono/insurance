package com.act.main;

import com.vaadin.ui.TabSheet;
import com.vaadin.ui.VerticalLayout;

public class Main extends VerticalLayout {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3746970325988455242L;

	public Main(){
		setSizeFull();
		createContents();
	}

	private void createContents() {
		TabSheet main = new TabSheet();
		main.setSizeFull();
		main.addTab(new CommodityInsuranceTab(),"Insurance");
		main.addTab(new CommodityTab(),"Commodity");
		
		addComponent(main);
		main.addTab(new CommodityInsuranceTab(),"Insurance");
		
	}
}
