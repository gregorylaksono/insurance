package com.act.main.window;

import java.util.LinkedHashMap;

import org.ksoap2.serialization.SoapObject;
import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.Point;

import com.act.insurance.InsuranceUI;
import com.act.main.CommodityInsuranceTab;
import com.act.util.CallSOAPAction;
import com.act.util.Factory;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.vaadin.data.Item;
import com.vaadin.data.Property.ValueChangeEvent;
import com.vaadin.data.Property.ValueChangeListener;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.ComboBox;
import com.vaadin.ui.Component;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.OptionGroup;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.themes.ValoTheme;

public class RulesPage extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7695353677456492619L;
//	private static final String AREA_ID = "areaid";
	private static final String AREA_NAME = "areaname";
	private Table areaTable;
	private CommodityInsuranceTab commodityInsurance;

//	private LMap leafletMap;
	public RulesPage(CommodityInsuranceTab commodityInsurance){
		this.commodityInsurance = commodityInsurance;
		setDraggable(false);
		setClosable(true);
		createContents();
		center();
		setHeight(600, Unit.PIXELS);
		setWidth(600, Unit.PIXELS);
	}

	private void createContents() {
		VerticalLayout layout = Factory.getVerticalLayoutTemplateFull();
		Component map = createMap();
		layout.addComponent(map);
		setContent(layout);
	}

	private Component createMap() {
		VerticalLayout layout = new VerticalLayout();
		ComboBox cb = new ComboBox();
		OptionGroup areaOptions = new OptionGroup();
		areaOptions.addStyleName(ValoTheme.OPTIONGROUP_HORIZONTAL);
		areaOptions.addItem("a");
		areaOptions.addItem("c");
		areaOptions.setItemCaption("a", "Area");
		areaOptions.setItemCaption("c", "Country");
		areaOptions.setNullSelectionAllowed(false);
		layout.addComponent(areaOptions);
		layout.addComponent(cb);
		
		HorizontalLayout buttonLayout = new HorizontalLayout();
		buttonLayout.setSpacing(true);
		Button selectButton = new Button("Select");
		Button cancelButton = new Button("Close");
		buttonLayout.addComponent(selectButton);
		buttonLayout.addComponent(cancelButton);
		buttonLayout.setExpandRatio(selectButton, 1.0f);
		buttonLayout.setExpandRatio(cancelButton, 9.0f);
		buttonLayout.setComponentAlignment(selectButton, Alignment.TOP_RIGHT);
		buttonLayout.setComponentAlignment(cancelButton, Alignment.TOP_RIGHT);
		
		areaOptions.addValueChangeListener(new AreaOptionsChangeListener(layout, cb));
		cb.addValueChangeListener(new AreaChangeListener(areaOptions));

		layout.addComponent(buttonLayout);
		
		areaOptions.setHeight(null);
		cb.setHeight(null);
		buttonLayout.setHeight(100, Unit.PERCENTAGE);
		
		layout.setExpandRatio(areaOptions, 0.0f);
		layout.setExpandRatio(cb, 0.0f);
		layout.setExpandRatio(buttonLayout, 1.0f);
		
		return layout;
	}

//	private Component createMap() {
//		GoogleMap googleMap = new GoogleMap(null, null, "english");
//		googleMap.addMarker("NOT DRAGGABLE: Indonesia", new LatLon(
//				-6.159218, 106.824628), false, null);
//        googleMap.setMinZoom(1);
//        googleMap.setMaxZoom(16);
//        googleMap.setCenter(new LatLon(-6.159218, 106.824628));
//        googleMap.setSizeFull();
//		return googleMap;
//	}
	
//	private Component createMap() {
//		leafletMap = new LMap();
//		leafletMap.setCenter(-6.159218, 106.824628);
//		leafletMap.setZoomLevel(1);
//		leafletMap.addBaseLayer(new LOpenStreetMapLayer(),"OSM");
//	
//		LMarker marker = new LMarker(-0.789275,113.921327);
//		marker.setPopup("Indonesia");
//		leafletMap.addComponent(marker);
//		
//		return leafletMap;
//	}
	private class AreaChangeListener implements ValueChangeListener{
		
	/**
	 * 
	 */
	private static final long serialVersionUID = -5983168635524796662L;
	private OptionGroup group;
	public AreaChangeListener(OptionGroup group) {
		this.group = group;
	}
	@Override
	public void valueChange(ValueChangeEvent event) {
		String selectedValue = (String) group.getValue();
		if(selectedValue.equals("a")){
			String areaCode = (String) event.getProperty().getValue();
			LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
			param.put("sessionId", ((InsuranceUI)UI.getCurrent()).getUser().getSessionId());
			param.put("areaCode",areaCode);
			ISOAPResultCallBack callback = new ISOAPResultCallBack(){

				@Override
				public void handleResult(SoapObject data, String statusCode) {

					for(int i=0; i<data.getPropertyCount(); i++){
						SoapObject s = (SoapObject) data.getProperty(i);
						String name = (String) s.getProperty("co_name");
						String code = (String) s.getProperty("co_2lc");
						Item item = areaTable.addItem(code);
						item.getItemProperty(AREA_NAME).setValue(name);
					}
				}

				@Override
				public void handleError(String statusCode) {

					
				}
				
			};
			new CallSOAPAction(param, "getAllCountries", callback);
		}
		
	}
		
	}
	private class AreaOptionsChangeListener implements ValueChangeListener{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		private VerticalLayout parent;
		private ComboBox cb;

		AreaOptionsChangeListener(VerticalLayout parent, ComboBox cb){
			this.parent = parent;
			this.cb = cb;
		}

		@Override
		public void valueChange(ValueChangeEvent event) {
			String id = (String) event.getProperty().getValue();
			cb.removeAllItems();
			listComponent(cb, id);
			if(id.equals("a")){
				areaTable = new Table();
				areaTable.setHeight(200, Unit.PIXELS);
//				areaList.addContainerProperty(AREA_ID,String.class,null);
				areaTable.addContainerProperty(AREA_NAME,String.class,null);
				parent.addComponent(areaTable,2);
				parent.setExpandRatio(areaTable, 0.0f);
			}else if(id.equals("c")){
				
			}
			
		}
	}

	public void listComponent(final Component areaList, String mode) {
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("sessionId", ((InsuranceUI)UI.getCurrent()).getUser().getSessionId());
		final ComboBox cb = (ComboBox) areaList;
		if(mode.equals("a")){
			ISOAPResultCallBack callback = new ISOAPResultCallBack() {
				
				@Override
				public void handleResult(SoapObject data, String statusCode) {
					for(int i=0; i<data.getPropertyCount(); i++){
						SoapObject raw = (SoapObject) data.getProperty(i);
						String name = raw.getProperty("ar_name").toString();
						String code = raw.getProperty("ar_code").toString();
						
						cb.addItem(code);
						cb.setItemCaption(code, name);
					}
				}
				
				@Override
				public void handleError(String statusCode) {
					
				}
			};
			new CallSOAPAction(param, "getAllAreas", callback);
		}
		else if(areaList instanceof ComboBox){
			ISOAPResultCallBack callback = new ISOAPResultCallBack() {

				@Override
				public void handleResult(SoapObject data, String statusCode) {
					for(int i=0; i<data.getPropertyCount(); i++){
						SoapObject raw = (SoapObject) data.getProperty(i);
						String name = raw.getProperty("co_name").toString();
						String code = raw.getProperty("co_2lc").toString();
						cb.addItem(code);
						cb.setItemCaption(code, name);
					}
					
				}

				@Override
				public void handleError(String statusCode) {
				
					
				}
				
			};
			new CallSOAPAction(param, "getAllCountries", callback);
			
		}
		
	}

}
