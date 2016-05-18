package com.act.main.window;

import org.vaadin.addon.leaflet.LMap;
import org.vaadin.addon.leaflet.LMarker;
import org.vaadin.addon.leaflet.LOpenStreetMapLayer;
import org.vaadin.addon.leaflet.LPolygon;
import org.vaadin.addon.leaflet.LPolyline;
import org.vaadin.addon.leaflet.LeafletClickEvent;
import org.vaadin.addon.leaflet.LeafletClickListener;
import org.vaadin.addon.leaflet.shared.Point;

import com.act.util.Factory;
import com.vaadin.tapio.googlemaps.GoogleMap;
import com.vaadin.tapio.googlemaps.client.LatLon;
import com.vaadin.ui.Component;
import com.vaadin.ui.Notification;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;

public class RulesPage extends Window{

	/**
	 * 
	 */
	private static final long serialVersionUID = 7695353677456492619L;

//	private LMap leafletMap;
	public RulesPage(){
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
		GoogleMap googleMap = new GoogleMap(null, null, "english");
		googleMap.addMarker("NOT DRAGGABLE: Indonesia", new LatLon(
				-6.159218, 106.824628), false, null);
        googleMap.setMinZoom(1);
        googleMap.setMaxZoom(16);
        googleMap.setCenter(new LatLon(-6.159218, 106.824628));
        googleMap.setSizeFull();
		return googleMap;
	}
	
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
}
