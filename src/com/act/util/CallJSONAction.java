package com.act.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class CallJSONAction {
	private static String url = "http://localhost:8080/WEBSERVICE/services/MobileService/";
	private String action;
	private IJSonCallbackListener callback;
	private Map<String, Object> param;
	public CallJSONAction(String action, Map<String, Object> param,IJSonCallbackListener callback){
		this.action = action;
		this.param = param;
		this.callback = callback;
		try {
			parse();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void parse() throws Exception {
		String sUrl = this.url+action+"?response=json";
		for(Map.Entry<String, Object> m : param.entrySet()){
			String key = m.getKey();
			String value = (String) m.getValue();
			sUrl = sUrl+"&"+key+"="+value;
		}
		URL url = new URL(sUrl);
		BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
		
		JSONParser parser= new JSONParser();
		Object object = parser.parse(in);
		
		JSONObject array = (JSONObject) object; 
		Map<String, Object> result = new HashMap<String, Object>();
		List dataResult = new ArrayList();
		for(Object keys : array.keySet()){
			String key = (String) keys;
			JSONObject value = (JSONObject) array.get(key);
			JSONObject ret = (JSONObject) value.get("ns:return");
			JSONObject data = (JSONObject) ret.get("ax23:data");
			for(Object dataKeys : data.keySet()){
				String dataKey = (String) dataKeys;
				Object dataValue = data.get(dataKey); 
				if(dataValue instanceof JSONArray){
					JSONArray r= (JSONArray) dataValue;
					
					for(int i =0; i<r.size(); i++){
						Object d = r.get(i);
						dataResult.add(d);
					}
				}
				result.put("data", dataResult);
			}
		}
		callback.handleData(result);
		
		

		
	}
	
	public interface IJSonCallbackListener{
		public void handleData(Map<String, Object> values);
	}

}
