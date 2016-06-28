package com.act.main;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.ksoap2.serialization.SoapObject;

import com.act.insurance.InsuranceUI;
import com.act.main.window.RulesPage;
import com.act.util.CallJSONAction;
import com.act.util.CallJSONAction.IJSonCallbackListener;
import com.act.util.CallSOAPAction;
import com.act.util.Factory;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.vaadin.data.Container.Filter;
import com.vaadin.data.Container.Filterable;
import com.vaadin.data.Item;
import com.vaadin.data.util.filter.Like;
import com.vaadin.event.ItemClickEvent;
import com.vaadin.event.FieldEvents.TextChangeEvent;
import com.vaadin.event.FieldEvents.TextChangeListener;
import com.vaadin.event.ItemClickEvent.ItemClickListener;
import com.vaadin.server.Sizeable.Unit;
import com.vaadin.shared.ui.MarginInfo;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.DateField;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.Button.ClickEvent;
import com.vaadin.ui.Button.ClickListener;
import com.vaadin.ui.Notification.Type;
import com.vaadin.ui.Table.ColumnHeaderMode;
import com.vaadin.ui.themes.ValoTheme;

public class CommodityTab extends VerticalLayout {

	/**
	 * 
	 */
	private static final long serialVersionUID = 747292357566822421L;
	private final Logger logger = Logger.getLogger(CommodityTab.class);
	private Panel commodityPanel;
	
	protected static final String COMMODITY_LIST_PARENT = "parent";
	protected static final String COMMODITY_LIST_ID = "commodity";
	protected static final String COMMODITY_LIST_NAME = "name";
	protected static final String COMMODITY_LIST_VAD = "vad";
	
	private boolean isComNew = false;
	
	private List<String> commodityList = new ArrayList();
	protected String parentSelected;
	protected String nameSelected;
	protected String idSelected;
	protected String vadSelected;
	
	private ItemClickListener commoditySelectListener = new ItemClickListener() {
		
		@Override
		public void itemClick(ItemClickEvent event) {
			isComNew = false;
			Item item = event.getItem();
			parentSelected = (String) item.getItemProperty(COMMODITY_LIST_PARENT).getValue();
			nameSelected = (String) item.getItemProperty(COMMODITY_LIST_NAME).getValue();
			idSelected = (String) item.getItemProperty(COMMODITY_LIST_ID).getValue();
			vadSelected = (String) item.getItemProperty(COMMODITY_LIST_VAD).getValue();

			commodityPanel.setContent(createCommodityForm(parentSelected, nameSelected));
			
		}
	};
	private ClickListener newCommodityListener = new ClickListener(){

		@Override
		public void buttonClick(ClickEvent event) {
			isComNew = true;
			Component newComComponent = createCommodityForm(null, null);
			commodityPanel.setContent(newComComponent);
		}

		
	};
	private Table list;
	
	public CommodityTab(){
		setSizeFull();
		createContents();
	}

	private void createContents() {

		//--------------------------------------------
		Component commodityComponent = createCommodityComponent();

		addComponent(commodityComponent);
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
		list = new Table();
		list.setSelectable(true);
		list.addContainerProperty(COMMODITY_LIST_ID, String.class, null);
		list.addContainerProperty(COMMODITY_LIST_NAME, String.class, null);
		list.addContainerProperty(COMMODITY_LIST_PARENT, String.class, null);
		list.addContainerProperty(COMMODITY_LIST_VAD, String.class, null);
		
		searchText.addTextChangeListener(new TextChangeListener() {
			Filter filter = null;
			@Override
			public void textChange(TextChangeEvent event) {
				Filterable f = (Filterable)list.getContainerDataSource();
				// Remove old filter
                if (filter != null)
                    f.removeContainerFilter(filter);
                filter = new Like(COMMODITY_LIST_NAME, "%"+event.getText());
                f.addContainerFilter(filter);
			}
		});
		
		list.setVisibleColumns(COMMODITY_LIST_NAME);
		list.addItemClickListener(commoditySelectListener );
		initList();
		list.setWidth(100, Unit.PERCENTAGE);
		list.setHeight(100, Unit.PERCENTAGE);
		list.setColumnHeaderMode(ColumnHeaderMode.HIDDEN);
		//----------------------------------------
		Button addNew = new Button("Add new...");
		addNew.addStyleName(ValoTheme.BUTTON_PRIMARY);
		addNew.addClickListener(newCommodityListener);
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

	private void initList() {
		list.removeAllItems();
		List<String> comList = ((InsuranceUI)UI.getCurrent()).getCommodityList();
		String val = null;
		int index = 0;
		try{			
			for(String value : comList){
				val = value;
				
				String[] args = value.split("\\|");
				Item item = list.addItem(args[1]);
				item.getItemProperty(COMMODITY_LIST_ID).setValue(args[1]);
				item.getItemProperty(COMMODITY_LIST_NAME).setValue(args[2]);
				item.getItemProperty(COMMODITY_LIST_PARENT).setValue(args[4]);
				item.getItemProperty(COMMODITY_LIST_VAD).setValue(args[5]);
				commodityList.add(value);
				index++;
			}
		}catch(Exception e){
			logger.error("Error at index:"+index+", and value: "+val);
		}

	}
	
	private Component createCommodityForm(String selected, String name) {
		VerticalLayout layout = new VerticalLayout();
		layout.setSizeFull();
		layout.setMargin(true);
		FormLayout form = new FormLayout();
		
		final ComboBox parentComboBox = new ComboBox("Parent");
		final TextField nameTextField = new TextField("Name");
		
		nameTextField.setRequired(true);
		
		parentComboBox.setWidth(200, Unit.PIXELS);
		nameTextField.setWidth(200, Unit.PIXELS);
		
		initParentCombo(parentComboBox, selected);
		nameTextField.setWidth(100, Unit.PERCENTAGE);
		parentComboBox.setWidth(100, Unit.PERCENTAGE);
		
		form.addComponent(parentComboBox);
		form.addComponent(nameTextField);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		Button save = new Button("Save");
		Button clear = new Button("Discard");
		
		if(name!=null){
			nameTextField.setValue(name);
			save.setCaption("Update");
		}
		
		save.addStyleName(ValoTheme.BUTTON_PRIMARY);

		buttonLayout.setWidth(100, Unit.PERCENTAGE);
		buttonLayout.setHeight(null);
		buttonLayout.addComponent(save);
		buttonLayout.addComponent(clear);
		buttonLayout.setExpandRatio(save, 1.0f);
		buttonLayout.setExpandRatio(clear, 0.0f);
		buttonLayout.setComponentAlignment(save, Alignment.MIDDLE_RIGHT);
		buttonLayout.setComponentAlignment(clear, Alignment.MIDDLE_RIGHT);
		layout.addComponent(form);
		layout.addComponent(buttonLayout);
		layout.setExpandRatio(form, 1.0f);
		layout.setExpandRatio(buttonLayout, 0.0f);
		
		ClickListener commoditySaveListener = new ClickListener(){

			@Override
			public void buttonClick(ClickEvent event) {
				String parentId = (String) parentComboBox.getValue();
				String comName = nameTextField.getValue().trim();
				if(comName.isEmpty()){
					Notification.show("Please input commodity name", Type.ERROR_MESSAGE);return;
				}
				
				if(parentId == null) parentId = "0";
				if(isComNew)saveNewCommodity(parentId,comName);
				else updateCommodity(parentId,comName);
				
			}
			
		};
		
		save.addClickListener(commoditySaveListener);
		
		return layout;
	}

	protected void updateCommodity(String parentId, String comName) {
		LinkedHashMap<String, Object> param = new LinkedHashMap();
		param.put("sessionId", ((InsuranceUI)UI.getCurrent()).getUser().getSessionId());
		param.put("comId",idSelected);
		param.put("comName",comName);
		param.put("awbName",comName);
		param.put("parent",parentId);
		param.put("annId","0");
		param.put("vadId",vadSelected);
		ISOAPResultCallBack callback = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data, String StatusCode) {
				if(StatusCode.equals(CallSOAPAction.SUCCESS_CODE)){
					Notification.show("Commodity is updated", Type.HUMANIZED_MESSAGE);
					initList();				
					VerticalLayout root = Factory.getVerticalLayoutTemplateFull();
					commodityPanel.setContent(root);
				}
			}
			
			@Override
			public void handleError( String StatusCode) {

				
			}
		};
		new CallSOAPAction(param, "updateCommodity", callback);
		
	}

	protected void saveNewCommodity(String parentId, String comName) {
		LinkedHashMap<String, Object> param = new LinkedHashMap();
		param.put("sessionId", ((InsuranceUI)UI.getCurrent()).getUser().getSessionId());
		param.put("parentId",parentId);
		param.put("comName",comName);
		ISOAPResultCallBack callback = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data, String StatusCode) {
				if(StatusCode.equals(CallSOAPAction.SUCCESS_CODE)){
					initList();			
					VerticalLayout root = Factory.getVerticalLayoutTemplateFull();
					commodityPanel.setContent(root);
				}
			}
			
			@Override
			public void handleError( String StatusCode) {

				
			}
		};
		new CallSOAPAction(param, "createCommodity", callback);
		
	}

	private void initParentCombo(ComboBox parentComboBox, String selected) {
		String parentSelectId = null;
		parentComboBox.addItem("0");
		parentComboBox.setItemCaption("0", "root");
		
		for(String s : commodityList){
			String[] args = s.split("\\|");
			String caption = args[2];
			String id = args[1];
			parentComboBox.addItem(id);
			parentComboBox.setItemCaption(args[1], caption);
			if(selected != null && id.equals(selected))parentSelectId = id;
		}
		if(parentSelectId!=null) {
			parentComboBox.select(parentSelectId);
		}else{
			parentComboBox.select("0");
		}
		
	}
}
