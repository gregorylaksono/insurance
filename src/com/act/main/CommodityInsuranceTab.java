package com.act.main;

import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;

import com.act.insurance.InsuranceUI;
import com.act.main.window.RulesPage;
import com.act.util.CallSOAPAction;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.act.util.Factory;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.shared.ui.label.ContentMode;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.themes.ValoTheme;

public class CommodityInsuranceTab extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3746970325988455242L;
	private String ITEM_LIST_ID = "item";
	private String ITEM_COUNTRY_AREA_ID = "carea";
	private ItemClickListener commoditySelectListener = new ItemClickListener(){

		@Override
		public void itemClick(ItemClickEvent event) {
			Object itemId = event.getItemId();
			if(itemId == null)return;
			commodityPanel.setContent(createCommodityFormDetail(itemId));
		}
		
	};
	private ClickListener newCommodityInsuranceListener = new ClickListener(){

		@Override
		public void buttonClick(ClickEvent event) {
			commodityPanel.setContent(createCommodityFormDetail(null));
		}
		
	};
	private Panel commodityPanel;
	
	public CommodityInsuranceTab(){
		setSizeFull();
//		MarginInfo margin = new MarginInfo(true);
//		margin.setMargins(false, true, true, true);
//		setMargin(margin);
		createContents();
	}

	private void createContents() {
		
		Component userProfile = createUserComponent();
		((InsuranceUI)UI.getCurrent()).setHeader(userProfile);
		//--------------------------------------------
		Component commodityComponent = createCommodityComponent();
		
		addComponent(userProfile);
		addComponent(commodityComponent);
		
		setExpandRatio(userProfile, 0.0f);
		setExpandRatio(commodityComponent, 1.0f);
	}

	private Component createCommodityComponent() {
		HorizontalLayout layout = Factory.getHorizontalLayoutTemplateFull();
		MarginInfo mInfo = new MarginInfo(true);
		mInfo.setMargins(true, false, true, false);
		layout.setSpacing(true);
		layout.setMargin(mInfo);
		//---------------------------------------
		Component commodityList = createCommodityList();
		//---------------------------------------
		Component commodityDetail = createCommodityPanel();
		//---------------------------------------
		layout.addComponent(commodityList);
		layout.addComponent(commodityDetail);
		layout.setComponentAlignment(commodityList, Alignment.TOP_LEFT);
		layout.setComponentAlignment(commodityDetail, Alignment.TOP_RIGHT);
		layout.setExpandRatio(commodityList, 0.3f);
		layout.setExpandRatio(commodityDetail, 0.7f);
		return layout;
	}

	private Component createCommodityPanel() {
		commodityPanel = new Panel("Info");
		commodityPanel.setWidth(100, Unit.PERCENTAGE);
		commodityPanel.setHeight(100, Unit.PERCENTAGE);
		//---------------------------------------
		VerticalLayout root = Factory.getVerticalLayoutTemplateFull();
		commodityPanel.setContent(root);

		return commodityPanel;
	}

	private void loadCountryAreaList(Table countryList) {
		
	}
	
	private VerticalLayout createCommodityFormDetail(Object itemId){
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		//---------------------------------------
		HorizontalLayout main = Factory.getHorizontalLayoutTemplateFull();
		main.setSpacing(true);
		//---------------------------------------
		HorizontalLayout buttonLayout = Factory.getHorizontalLayoutTemplateFull();
		buttonLayout.setHeight(null);
		buttonLayout.setSpacing(true);
		//---------------------------------------	
		FormLayout form = new FormLayout();
		form.setWidth(100, Unit.PERCENTAGE);
		//---------------------------------------
		Table countryList = new Table();
		countryList.setHeight(200, Unit.PIXELS);
		countryList.setWidth(300, Unit.PIXELS);
		countryList.addContainerProperty(this.ITEM_COUNTRY_AREA_ID , String.class, null);
		countryList.setColumnHeader(this.ITEM_COUNTRY_AREA_ID, "Country/Area");
		countryList.setColumnReorderingAllowed(false);
		
		loadCountryAreaList(countryList);
		//---------------------------------------
		ComboBox comNameText = new ComboBox("Commodity");
		TextField minValueText = new TextField("Min. value");
		ComboBox currencyText = new ComboBox("Currency");
		OptionGroup options = new OptionGroup("Rate unit");
		TextField rateText = new TextField("Rate");
		DateField validDate = new DateField("Valid until");
		//---------------------------------------
		options.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		options.addItem("Fix rate");
		options.addItem("Percentage %");
		//---------------------------------------
		comNameText.setWidth(100, Unit.PERCENTAGE);
		minValueText.setWidth(100, Unit.PERCENTAGE);
		currencyText.setWidth(100, Unit.PERCENTAGE);
		rateText.setWidth(100, Unit.PERCENTAGE);
		validDate.setWidth(130, Unit.PIXELS);
		//---------------------------------------
		rateText.addStyleName(ValoTheme.TEXTAREA_ALIGN_RIGHT);
		//---------------------------------------
		validDate.setDateFormat("dd.MM.yy");
		//---------------------------------------
		main.addComponent(form);
		main.addComponent(countryList);
		main.setExpandRatio(form, 1.0f);
		main.setExpandRatio(countryList, 0.0f);
		
		form.addComponent(comNameText);
		form.addComponent(minValueText);
		form.addComponent(currencyText);
		form.addComponent(options);
		form.addComponent(rateText);
		form.addComponent(validDate);
		//---------------------------------------
		
		String buttonCaption = null;
		if(itemId==null){
			buttonCaption = "Save";
		}
		Button updateButton = new Button(buttonCaption);
		Button ruleButton = new Button("Rules...");
		Button discardButton = new Button("Discard");
		updateButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonLayout.addComponent(updateButton);
		buttonLayout.addComponent(ruleButton);
		buttonLayout.addComponent(discardButton);
		
		buttonLayout.setComponentAlignment(updateButton, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(ruleButton, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(discardButton, Alignment.MIDDLE_RIGHT);
		
		buttonLayout.setExpandRatio(ruleButton, 0.0f);
		buttonLayout.setExpandRatio(discardButton, 0.0f);
		buttonLayout.setExpandRatio(updateButton, 1.0f);
		//---------------------------------------
		updateButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Notification.show("Changes is saved", Type.TRAY_NOTIFICATION);
				
			}
		});
		ruleButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window w = new RulesPage();
				UI.getCurrent().addWindow(w);
			}
		});
		
		//---------------------------------------
		root.addComponent(main);
		root.addComponent(buttonLayout);
		
		root.setExpandRatio(main, 1.0f);
		root.setExpandRatio(buttonLayout, 0.0f);
		return root;
	}

	private Component createCommodityList() {
		Panel panel = new Panel("Commodity insurance list");
		panel.setWidth(100, Unit.PERCENTAGE);
		panel.setHeight(100, Unit.PERCENTAGE);
		//----------------------------------------
		VerticalLayout layout = Factory.getVerticalLayoutTemplateFull();
		layout.setSpacing(true);
		layout.setMargin(true);
		//----------------------------------------
		TextField searchText = new TextField();
		searchText.setWidth(100, Unit.PERCENTAGE);
		searchText.setHeight(null);
		searchText.setInputPrompt("Search commodity");
		//----------------------------------------
		Table list = new Table();
		list.setSelectable(true);
		list.addContainerProperty(ITEM_LIST_ID, String.class, null);
		list.addItemClickListener(commoditySelectListener );
		initList(list);
		list.setWidth(100, Unit.PERCENTAGE);
		list.setHeight(100, Unit.PERCENTAGE);
		list.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		//----------------------------------------
		Button addNew = new Button("Add new...");
		addNew.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addNew.addClickListener(newCommodityInsuranceListener);
		//----------------------------------------
		layout.addComponent(searchText);
		layout.addComponent(list);
		layout.addComponent(addNew);
		layout.setExpandRatio(searchText, 0.0f);
		layout.setExpandRatio(addNew, 0.0f);
		layout.setExpandRatio(list, 1.0f);
		//----------------------------------------
		
		panel.setContent(layout);
		return panel;
	}

	private void initList(Table list) {
		String sessionId = ((InsuranceUI)UI.getCurrent()).getSessionId();
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("sessionId", sessionId);

		ISOAPResultCallBack callBack = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data) {
				for(int i=0; i<data.getPropertyCount(); i++){
					data.getAttribute("");
				}

			}
			
			@Override
			public void handleError() {
				
			}
		};
		
		new CallSOAPAction(param, "getAllInsuranceCommodityByAddId", callBack);

	}

	private Component createUserComponent() {
		VerticalLayout main = new VerticalLayout();
		MarginInfo info = new MarginInfo(true);
		info.setMargins(false, true, false, false);
		main.setMargin(info);
		main.setWidth(100, Unit.PERCENTAGE);
		main.setHeight(null);
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
		main.addComponent(layout);
		main.setComponentAlignment(layout, Alignment.TOP_RIGHT);
		return main;
	}

}
