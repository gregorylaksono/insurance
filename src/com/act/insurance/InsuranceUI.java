package com.act.insurance;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;

import org.json.simple.JSONObject;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapPrimitive;
import org.vaadin.artur.icepush.ICEPush;
import org.vaadin.artur.icepush.ICEPushServlet;

import com.act.insurance.model.User;
import com.act.login.LoginPage;
import com.act.util.CallSOAPAction;
import com.act.util.CallJSONAction.IJSonCallbackListener;
import com.act.util.CallSOAPAction.ISOAPResultCallBack;
import com.vaadin.annotations.Theme;
import com.vaadin.annotations.VaadinServletConfiguration;
import com.vaadin.server.VaadinRequest;
import com.vaadin.server.VaadinServlet;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Component;
import com.vaadin.ui.Layout;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;

@SuppressWarnings("serial")
@Theme("insurance")
public class InsuranceUI extends UI {

	@WebServlet(value = "/*", asyncSupported = true)
	@VaadinServletConfiguration(productionMode = true, ui = InsuranceUI.class, widgetset = "com.act.insurance.widgetset.InsuranceWidgetset")
	public static class Servlet extends VaadinServlet  {
		
	}
	
	private ICEPush pusher = new ICEPush();
	private User user;
	private VerticalLayout layout;
	private Component header;
	private List<String> commodityList = new ArrayList();
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

	private String sessionId = null;
	private List<String> currencyList;

	@Override
	protected void init(VaadinRequest request) {
		layout = new VerticalLayout();
		layout.setHeight(100, Unit.PERCENTAGE);
		
		addExtension(pusher);
		
		LoginPage loginPage = new LoginPage();
		layout.addComponent(loginPage);
		layout.setComponentAlignment(loginPage, Alignment.MIDDLE_CENTER);

		setContent(layout);
		
	}
	



	public ICEPush getPusher() {
		return pusher;
	}




	public void setPusher(ICEPush pusher) {
		this.pusher = pusher;
	}




	public User getUser() {
		return user;
}
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}


	public void setUser(User user) {
		this.user = user;
	}
	public void setInsidePage(Component page){
		layout.removeAllComponents();
		if(header!= null){
			layout.addComponent(header);
			layout.setExpandRatio(header, 0.0f);
		}
		layout.addComponent(page);
		layout.setExpandRatio(page, 1.0f);
	}
	
	public void setHeader(Component header){
		this.header = header;
	}


	public static SimpleDateFormat getSdf() {
		return sdf;
	}

	public List<String> getCommodityList() {
		return commodityList;
	}


	public void setCommodityList(List<String> commodityList) {
		this.commodityList = commodityList;
	}
	public void initCurrencies(){
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("sessionId", user.getSessionId());
		ISOAPResultCallBack callback = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data, String StatusCode) {
				List<String> list = new ArrayList();
				for(int i = 0; i<data.getPropertyCount(); i++){
					SoapObject raw = (SoapObject) data.getProperty(i);
					SoapPrimitive curr = (SoapPrimitive) raw.getProperty("cur_3lc");
					list.add(curr.toString());
				}
				
				((InsuranceUI)UI.getCurrent()).setCurrencyList(list);
			}
			
			@Override
			public void handleError(String StatusCode) {

				
			}
		};
		new CallSOAPAction(param, "getCurrencies", callback);
		
	}
	
	protected void setCurrencyList(List<String> list) {
		this.currencyList = list;
	}
	
	public List getCurrencyList(){
		return this.currencyList;
	}

	public void initCommodities() {
		String sessionId = ((InsuranceUI)UI.getCurrent()).getUser().getSessionId();
		String match = "*";
		LinkedHashMap<String, Object> param = new LinkedHashMap<String, Object>();
		param.put("sessionId", sessionId);
		
		ISOAPResultCallBack callback = new ISOAPResultCallBack() {
			
			@Override
			public void handleResult(SoapObject data, String StatusCode) {
				List commodityList = new ArrayList();
				for(int i=0; i<data.getPropertyCount(); i++){
					String value  =  data.getProperty(i).toString();
					String[] args = value.split("\\|");
					commodityList.add(value);
				}
				((InsuranceUI)UI.getCurrent()).setCommodityList(commodityList);
				
			}
			
			@Override
			public void handleError(String StatusCode) {
				System.out.println(StatusCode);
				
			}
		};
		
		IJSonCallbackListener jsonCallback = new IJSonCallbackListener() {
			
			@Override
			public void handleData(Map<String, Object> values) {
				List commodityList = new ArrayList();
				List datas = (List) values.get("data");
				for(Object d : datas){
					JSONObject obj = (JSONObject) d;
					String value = (String) obj.get("$");
					
					String[] args = value.split("\\|");
					commodityList.add(value);
				}
				((InsuranceUI)UI.getCurrent()).setCommodityList(commodityList);
				
				
			}
		};
		new CallSOAPAction(param, "getCommodityByCreatorId", callback);
//		new CallJSONAction("getCommodityByCreatorId", param, jsonCallback);

	}
	
}