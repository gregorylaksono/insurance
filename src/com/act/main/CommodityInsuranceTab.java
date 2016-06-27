package com.act.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.ksoap2.serialization.SoapObject;
import org.vaadin.dialogs.ConfirmDialog;

import com.act.insurance.InsuranceUI;
import com.act.insurance.model.User;
import com.act.main.window.RulesPage;
import com.act.util.CallSOAPAction;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.act.util.Factory;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeListener;
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
import com.vaadin.ui.CheckBox;
import com.vaadin.ui.themes.ValoTheme;

public class CommodityInsuranceTab extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3746970325988455242L;
	private static final String ITEM_VALID_FROM_VALUE = "validfrom";
	private static final String ITEM_RATE_VALUE = "rateval";
	private static final String ITEM_RATE_UNIT_VALUE = "unit";
	private static final String ITEM_CURR_VALUE = "curr";
	private static final String ITEM_MIN_VALUE = "min";
	private static final String ITEM_LIST_ID = "item";
	private static final String ITEM_COUNTRY_AREA_ID = "carea";
	private static final String ITEM_COM_ID = "id";
	private static final String ITEM_LIST_NAME = "name";
	private static final String ITEM_VALID_TO_VALUE = "validuntil";
	private static final String ITEM_COUNTRY_AREA_ENABLE = "country_area_enable";
	private static final String ITEM_COUNTRY_AREA_TYPE = "country_area_type";
	private static final String ITEM_COUNTRY_AREA_BUTTON = "country_remove_button";
	private Long comInsId = null;
	private Map<String,String> ruleList = new HashMap();
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
	
	private ClickListener deleteInsuranceListener = new ClickListener(){

		@Override
		public void buttonClick(ClickEvent event) {
			final Object value = insuranceList.getValue();
			if(value == null){
				Notification.show("Please select an insurance", Type.ERROR_MESSAGE);
				return;
			}
			ConfirmDialog.show(UI.getCurrent(), "Please Confirm:", "Delete this insurance?",
			        "Delete", "Cancel", new ConfirmDialog.Listener() {
					
			            public void onClose(ConfirmDialog dialog) {
			                if (dialog.isConfirmed()) {
			                	User user  = ((InsuranceUI)UI.getCurrent()).getUser();
			                	LinkedHashMap<String, Object> param = new LinkedHashMap<>();
			                	param.put("sessionId", user.getSessionId());
			                	param.put("insuranceId", value);
			                	ISOAPResultCallBack callback = new ISOAPResultCallBack() {
									
									@Override
									public void handleResult(SoapObject data, String StatusCode) {
										insuranceList.removeItem(insuranceList.getValue());
										VerticalLayout root = Factory.getVerticalLayoutTemplateFull();
										commodityPanel.setContent(root);
									}
									
									@Override
									public void handleError(String StatusCode) {
										Notification.show("Error", "Insurance cannot be deleted because of certain reasons", Type.ERROR_MESSAGE);
									}
								};
			    				new CallSOAPAction(param, "deleteCommodityInsurance", callback);
			                } else {
			                    
			                }
			            }
			        });
		}
		
	};
	private Panel commodityPanel;
	private Table insuranceList;
	private Table countryList;
		
	public CommodityInsuranceTab(){
		setSizeFull();
//		MarginInfo margin = new MarginInfo(true);
//		margin.setMargins(false, true, true, true);
//		setMargin(margin);
		createContents();
	}

	private void createContents() {
		Component commodityComponent = createCommodityComponent();
		addComponent(commodityComponent);		
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

	private void loadCountryAreaList(final Table countryList, Object itemId) {
		
		User user = ((InsuranceUI)UI.getCurrent()).getUser();
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("sessionId",user.getSessionId());
		param.put("commodityInsuranceId", itemId.toString());
		
		ISOAPResultCallBack callback = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data, String statusCode) {
				
				for(int i=0; i<data.getPropertyCount(); i++){
					final String d = data.getProperty(i).toString();
					if(!d.isEmpty()){
						//"a|"+ar.getAr_name()+"|"+base.getRAr().getArCode()+"|"+base.getRAr().getArBool();
						String[] args = d.split("\\|");
						String area = args[0];
						String name = args[1];
						String code = args[2];
						String isEnable = args[3];
						
						insertItemToCountryList(d, name, area, isEnable);
						
						String id = area+"|"+code+"|"+isEnable+"|";
						ruleList.put(code, id);
					}
				}
			}
			
			@Override
			public void handleError(String statusCode) {
				
				
			}
		};
		
		new CallSOAPAction(param, "getRulesByCommodityInsurance", callback);
	}
	
	protected void insertItemToCountryList(final String id,final String name,final String area, String isEnable) {
		CheckBox cb = new CheckBox();
		cb.setValue(isEnable.equals("1"));
		cb.addValueChangeListener(new ValueChangeListener() {
			
			@Override
			public void valueChange(com.vaadin.data.Property.ValueChangeEvent event) {
				String isIncl = (boolean) event.getProperty().getValue() ? "1":"0";
				//c|AMERICAN SAMOA|AS|1  / a|JAPAN/KOREA|0316|1|n
				String[] args = id.split("\\|",-1); 
				String val = ruleList.get(args[2]);
				String[] array = val.split("\\|");
				String code = array[1];
				String value = null;
				if(array.length > 4){
					String flag = array[4];
					if(flag.equals("e")){
						value = area+"|"+code+"|"+isIncl+"|"+"e";											
					}
					else{
						value = area+"|"+code+"|"+isIncl+"|"+"n";
					}
				}else{
					value = area+"|"+code+"|"+isIncl+"|"+"e";
				}
				ruleList.put(code, value);
				
			}
		});
		
		Button remove = new Button("Remove");
		remove.addStyleName(ValoTheme.BUTTON_TINY);
		remove.addStyleName(ValoTheme.BUTTON_DANGER);
		remove.addClickListener(new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				String args[] = id.split("\\|");
				//c|AMERICAN SAMOA|AS|1
				String val = ruleList.get(args[2]);
				String[] array = val.split("\\|");
				String code = array[1];
				String isIncl = array[2];
				String access  = null;
				if(array.length > 3){
					access = array[3];					
				}
				
				String value = null;
				if(access != null && access.equals("n")){
					ruleList.remove(id);
				}else {
					value = area+"|"+code+"|"+isIncl+"|"+"d";
					ruleList.put(code, value);
				}
				countryList.removeItem(id);
			}
			
		});
		
		Item item = countryList.addItem(id);
		item.getItemProperty(ITEM_COUNTRY_AREA_ID).setValue(name);
		item.getItemProperty(ITEM_COUNTRY_AREA_ENABLE).setValue(cb);
		item.getItemProperty(ITEM_COUNTRY_AREA_TYPE).setValue(area.equals("a") ? "Area":"Country");
		item.getItemProperty(ITEM_COUNTRY_AREA_BUTTON).setValue(remove);
		
		
	}

	private VerticalLayout createCommodityFormDetail(Object itemId){
		VerticalLayout root = new VerticalLayout();
		root.setMargin(true);
		root.setSizeFull();
		//---------------------------------------
		HorizontalLayout buttonLayout = Factory.getHorizontalLayoutTemplateFull();
		buttonLayout.setHeight(null);
		buttonLayout.setSpacing(true);
		//---------------------------------------	
		FormLayout form = new FormLayout();
		form.setWidth(100, Unit.PERCENTAGE);
		form.setHeight(100, Unit.PERCENTAGE);
		//---------------------------------------
		countryList = new Table();
		countryList.setHeight(200, Unit.PIXELS);
		countryList.setWidth(350, Unit.PIXELS);
		
		countryList.addContainerProperty(this.ITEM_COUNTRY_AREA_ID , String.class, null);
		countryList.addContainerProperty(this.ITEM_COUNTRY_AREA_ENABLE , Component.class, null);
		countryList.addContainerProperty(this.ITEM_COUNTRY_AREA_TYPE , String.class, null);
		countryList.addContainerProperty(this.ITEM_COUNTRY_AREA_BUTTON , Component.class, null);
		
		countryList.setColumnHeader(this.ITEM_COUNTRY_AREA_ID, "Country/Area");
		countryList.setColumnHeader(this.ITEM_COUNTRY_AREA_ENABLE, "Include");
		countryList.setColumnHeader(this.ITEM_COUNTRY_AREA_TYPE, "Type");
		countryList.setColumnHeader(this.ITEM_COUNTRY_AREA_BUTTON, "Remove");
		
		countryList.setColumnAlignment(this.ITEM_COUNTRY_AREA_ENABLE, Table.ALIGN_CENTER);
		countryList.setColumnAlignment(this.ITEM_COUNTRY_AREA_TYPE, Table.ALIGN_CENTER);
		countryList.setColumnAlignment(this.ITEM_COUNTRY_AREA_BUTTON, Table.ALIGN_CENTER);
		countryList.setColumnReorderingAllowed(false);
		if(itemId != null){
			loadCountryAreaList(countryList,itemId);			
		}
		//---------------------------------------
		final ComboBox comNameText = new ComboBox("Commodity");
		final TextField minValueText = new TextField("Min. value");
		final ComboBox currencyText = new ComboBox("Currency");
		final OptionGroup calculationOptions = new OptionGroup("Calculation base unit");
		final TextField rateText = new TextField("Rate");
		final DateField validFromDate = new DateField("Valid from");
		final DateField validUntilDate = new DateField("Valid until");
		Button ruleButton = new Button("Add rules...");
		//---------------------------------------		
		comNameText.setRequired(true);
		minValueText.setRequired(true);
		currencyText.setRequired(true);
		calculationOptions.setRequired(true);
		rateText.setRequired(true);
		//---------------------------------------
		initComboBox(comNameText);
		initCurrency(currencyText);
		//---------------------------------------
		calculationOptions.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		calculationOptions.addItem("x");
		calculationOptions.addItem("t");
		calculationOptions.addItem("v");
		calculationOptions.addItem("k");
		calculationOptions.setItemCaption("x", "Total value");
		calculationOptions.setItemCaption("t", "Transport value");
		calculationOptions.setItemCaption("v", "Item value");
		calculationOptions.setItemCaption("k", "Weight value");
		calculationOptions.setNullSelectionAllowed(false);
		//---------------------------------------
		comNameText.setWidth(200, Unit.PIXELS);
		minValueText.setWidth(200, Unit.PIXELS);
		currencyText.setWidth(200, Unit.PIXELS);
		rateText.setWidth(200, Unit.PIXELS);
		validFromDate.setWidth(230, Unit.PIXELS);
		validUntilDate.setWidth(230, Unit.PIXELS);
		//---------------------------------------
		rateText.addStyleName(ValoTheme.TEXTAREA_ALIGN_RIGHT);
		minValueText.addStyleName(ValoTheme.TEXTAREA_ALIGN_RIGHT);
		//---------------------------------------
		validFromDate.setDateFormat("dd.MM.yy");
		validUntilDate.setDateFormat("dd.MM.yy");
		//---------------------------------------
		form.addComponent(comNameText);
		form.addComponent(minValueText);
		form.addComponent(currencyText);
		form.addComponent(rateText);
		form.addComponent(calculationOptions);
		form.addComponent(validFromDate);
		form.addComponent(validUntilDate);
		form.addComponent(countryList);
		form.addComponent(ruleButton);
		//---------------------------------------
		
		String buttonCaption = null;
		if(itemId==null){
			buttonCaption = "Save";
			comInsId=null;
		}else{
			buttonCaption = "Update";
			
			String comInsId = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_LIST_ID).getValue();
			String comId = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_COM_ID).getValue();
			String min = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_MIN_VALUE).getValue();
			String currency = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_CURR_VALUE).getValue();
			String rateUnit = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_RATE_UNIT_VALUE).getValue();
			String validFrom = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_VALID_FROM_VALUE).getValue();
			String validTo = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_VALID_TO_VALUE).getValue();
			String rate = (String) insuranceList.getItem(itemId).getItemProperty(ITEM_RATE_VALUE).getValue();
			
			rateText.setValue(rate);
			comNameText.select(comId);
			minValueText.setValue(min);
			currencyText.select(currency);
			calculationOptions.select(rateUnit);
			
			this.comInsId = Long.parseLong(comInsId);
			try{
				Date from = ((InsuranceUI)UI.getCurrent()).getSdf().parse(validFrom);
				Date to = ((InsuranceUI)UI.getCurrent()).getSdf().parse(validTo);
				
				validFromDate.setValue(from);
				validUntilDate.setValue(to);
			}catch(Exception e){
				e.printStackTrace();
			}
			
		}
		Button updateButton = new Button(buttonCaption);
		Button discardButton = new Button("Discard");
		updateButton.addStyleName(ValoTheme.BUTTON_PRIMARY);
		buttonLayout.addComponent(updateButton);
		buttonLayout.addComponent(discardButton);
		
		buttonLayout.setComponentAlignment(updateButton, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(discardButton, Alignment.MIDDLE_RIGHT);
		
		buttonLayout.setExpandRatio(discardButton, 0.0f);
		buttonLayout.setExpandRatio(updateButton, 1.0f);
		//---------------------------------------
		updateButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				//comNameText, minValueText, currencyText, options, rateText, validDate
				String commId = (String) comNameText.getValue();
				String minValue = minValueText.getValue();
				String currency = (String) currencyText.getValue();
				String rateValue = rateText.getValue();
				String calculationBase = (String) calculationOptions.getValue();
				Date fromValidDateValue = validFromDate.getValue();
				Date toValidDateValue = validUntilDate.getValue();
				User user = ((InsuranceUI)UI.getCurrent()).getUser();
				String[] rules = getListAsArray();
				String validFrom = null;
				String validTo = null;
				DateFormat format = new SimpleDateFormat("M/d/yyyy");
				if(fromValidDateValue != null){
					validFrom = format.format(fromValidDateValue);					
				}
				if(toValidDateValue != null){
					validTo = format.format(toValidDateValue);					
				}
				
				LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
				param.put("sessionId", user.getSessionId());
				param.put("comInsId", CommodityInsuranceTab.this.comInsId);
				param.put("comId", commId);
				param.put("addId", user.getAddId());
				param.put("rate", rateValue);
				param.put("rateUnit", calculationBase);
				param.put("currency", currency);
				param.put("min", minValue);
				param.put("from", validFrom);
				param.put("to", validTo);
				param.put("rules",rules);
				
				ISOAPResultCallBack callback = new ISOAPResultCallBack() {
					
					@Override
					public void handleResult(SoapObject data, String statusCode) {
						initList(insuranceList);
						Notification.show("Changes is saved", Type.TRAY_NOTIFICATION);
					}
					
					@Override
					public void handleError(String statusCode) {

						
					}
				};
				
				new CallSOAPAction(param, "saveCommodityInsurance", callback);
				
			}
		});
		ruleButton.addClickListener(new ClickListener() {
			
			@Override
			public void buttonClick(ClickEvent event) {
				Window w = new RulesPage(CommodityInsuranceTab.this);
				UI.getCurrent().addWindow(w);
			}
		});
		
		//---------------------------------------
		root.addComponent(form);
		root.addComponent(buttonLayout);
		
		root.setExpandRatio(form, 1.0f);
		root.setExpandRatio(buttonLayout, 0.0f);
		return root;
	}

	protected String[] getListAsArray() {
		int index = 0;
		String[] result = new String[ruleList.size()];
		for(Map.Entry<String, String> m : ruleList.entrySet()){
			result[index] = m.getValue();
			index++;
		}
		return result;
	}

	private void initCurrency(ComboBox currencyText) {
		currencyText.addItem("USD");
		currencyText.addItem("EUR");
		currencyText.addItem("Rp");
		
	}

	private void initComboBox(ComboBox comNameText) {
		List<String> comList = ((InsuranceUI)UI.getCurrent()).getCommodityList();
		for(String value : comList){
			String[] args = value.split("\\|");
			comNameText.addItem(args[1]);
			comNameText.setItemCaption(args[1], args[2]);
		}
		
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
		insuranceList = new Table();
		insuranceList.setSelectable(true);
		//minValueText, currencyText, rateUnit, rateText, validDate
		insuranceList.addContainerProperty(ITEM_LIST_ID, String.class, null);
		insuranceList.addContainerProperty(ITEM_LIST_NAME, String.class, null);
		insuranceList.addContainerProperty(ITEM_COM_ID, String.class, null);
		insuranceList.addContainerProperty(ITEM_MIN_VALUE, String.class, null);
		insuranceList.addContainerProperty(ITEM_CURR_VALUE, String.class, null);
		insuranceList.addContainerProperty(ITEM_RATE_UNIT_VALUE, String.class, null);
		insuranceList.addContainerProperty(ITEM_RATE_VALUE, String.class, null);
		insuranceList.addContainerProperty(ITEM_VALID_FROM_VALUE, String.class, null);
		insuranceList.addContainerProperty(ITEM_VALID_TO_VALUE, String.class, null);
		insuranceList.addItemClickListener(commoditySelectListener );
		initList(insuranceList);
		insuranceList.setWidth(100, Unit.PERCENTAGE);
		insuranceList.setHeight(100, Unit.PERCENTAGE);
		insuranceList.setVisibleColumns(ITEM_LIST_NAME);
		//----------------------------------------
		Button addNew = new Button("Add new...");
		addNew.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addNew.addClickListener(newCommodityInsuranceListener);
		//----------------------------------------
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		//----------------------------------------
		Button delete = new Button("Delete");
		delete.addStyleName(ValoTheme.BUTTON_DANGER);
		delete.addClickListener(deleteInsuranceListener );
		//----------------------------------------
		buttonLayout.setSpacing(true);
		//----------------------------------------
		buttonLayout.addComponent(addNew);
		buttonLayout.addComponent(delete);
		//----------------------------------------
		buttonLayout.setComponentAlignment(addNew, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(delete, Alignment.MIDDLE_RIGHT);
		//----------------------------------------
		buttonLayout.setExpandRatio(addNew, 1.0f);
		buttonLayout.setExpandRatio(delete, 0.0f);
		//----------------------------------------
		layout.addComponent(searchText);
		layout.addComponent(insuranceList);
		layout.addComponent(buttonLayout);
		//----------------------------------------
		layout.setExpandRatio(searchText, 0.0f);
		layout.setExpandRatio(buttonLayout, 0.0f);
		layout.setExpandRatio(insuranceList, 1.0f);
		//----------------------------------------
		
		panel.setContent(layout);
		return panel;
	}

	private void initList(Table list) {
		list.removeAllItems();
		list.addItemClickListener(commoditySelectListener);
		
		String sessionId = ((InsuranceUI)UI.getCurrent()).getUser().getSessionId();
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("sessionId", sessionId);

		ISOAPResultCallBack callBack = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data,  String StatusCode) {
				for(int i=0; i<data.getPropertyCount(); i++){
					SoapObject parent = (SoapObject) data.getProperty(i);
					
					String comId = parent.getProperty("commId").toString();
					String commodityName = parent.getProperty("commodityName").toString();
					String currency = parent.getProperty("currency").toString();
					String insuranceId = parent.getProperty("insuranceId").toString();
					String min = parent.getProperty("min").toString();
					String rate = parent.getProperty("rate").toString();
					String unit = parent.getProperty("unit").toString();
					String validFrom = parent.getProperty("validFrom") == null ? "":parent.getProperty("validFrom").toString();
					String validUntil = parent.getProperty("validUntil") == null ? "":parent.getProperty("validUntil").toString();

					Item item = insuranceList.addItem(insuranceId);
					item.getItemProperty(ITEM_LIST_ID).setValue(insuranceId);
					item.getItemProperty(ITEM_VALID_FROM_VALUE).setValue(validFrom);
					item.getItemProperty(ITEM_VALID_TO_VALUE).setValue(validUntil);
					item.getItemProperty(ITEM_RATE_VALUE).setValue(rate);
					item.getItemProperty(ITEM_RATE_UNIT_VALUE).setValue(unit);
					item.getItemProperty(ITEM_CURR_VALUE).setValue(currency);
					item.getItemProperty(ITEM_MIN_VALUE).setValue(min);
//					item.getItemProperty(ITEM_COUNTRY_AREA_ID).setValue();
					item.getItemProperty(ITEM_COM_ID).setValue(comId);
					item.getItemProperty(ITEM_LIST_NAME).setValue(commodityName);
					insuranceList.setItemCaption(insuranceId, commodityName);
				}

			}
			
			@Override
			public void handleError( String StatusCode) {
				
			}
		};
		
		new CallSOAPAction(param, "getAllInsuranceCommodityByAddId", callBack);

	}

	public void reloadRuleTable(String value, String isEnable) {
		String[] args = value.split("\\|");
		//area+"|"+name+"|"+code+"|"+"1";
		//c|AMERICAN SAMOA|AS|1
		insertItemToCountryList(value, args[1], args[0], isEnable);

		String area = args[0];
		String code = args[2];
		String mode = args[4];
		String value2 = area +"|"+code+"|"+isEnable+"|"+mode; 
		ruleList.put(args[2], value2);
	}
	
	private String[] getValueFromTable(){
		List<String> s = new ArrayList();
		for(Object i: countryList.getItemIds()){
			Item item = countryList.getItem(i);
			String code = (String) item.getItemProperty(ITEM_COUNTRY_AREA_ID).getValue();
			String isInclusive = (String) item.getItemProperty(ITEM_COUNTRY_AREA_ENABLE).getValue();
			String type= (String) item.getItemProperty(ITEM_COUNTRY_AREA_TYPE).getValue();
			String result = null;
			
			
			
//			item.getItemProperty(ITEM_COUNTRY_AREA_ID).setValue(name);
//			item.getItemProperty().setValue(cb);
//			item.getItemProperty().setValue(area.equals("a") ? "Area":"Country");
//			item.getItemProperty().setValue(remove)
		}
		return s.toArray(new String[s.size()]);
	}


}

