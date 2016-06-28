package com.act.util;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

public class CallSOAPAction {
	private String method_name;
	private LinkedHashMap<String, Object> param;
	private String url = Utils.getWebserviceURL();
	private ISOAPResultCallBack callBack;
	public static final String NAMESPACE = "http://service.act.de";
	public static final String SUCCESS_CODE = "00:success";
	private final Logger logger = Logger.getLogger(CallSOAPAction.class);
	public CallSOAPAction(LinkedHashMap<String, Object> param, String method_name, ISOAPResultCallBack callback){
		this.param = param;
		this.method_name = method_name;
		this.callBack = callback;
		
		doCall();
	}

	private void doCall() {
		SoapObject repaymentReq = new SoapObject(NAMESPACE,
				method_name);
		for(Map.Entry<String, Object> m : param.entrySet()){
			Object o = m.getValue();
			if(o instanceof String[]){
				String[] arr = (String[]) o;
				for(String s : arr){
					repaymentReq.addProperty(m.getKey(), s);	
				}
			}else{
				repaymentReq.addProperty(m.getKey(), o);				
			}
		}
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);

		envelope.setOutputSoapObject(repaymentReq);
		envelope.dotNet = true;

		try {
			HttpTransportSE httpTransport = new HttpTransportSE(url);
			String statusCode = null;
			logger.info("For service "+method_name);
			httpTransport.call("http://service.act.de/"+method_name, envelope);
			Object o = envelope.bodyIn;
			
			if(o instanceof SoapObject){
				SoapObject getCommodityResponse = (SoapObject) o;
				if (getCommodityResponse != null) {
					SoapObject ActernityResponse = (SoapObject) getCommodityResponse
							.getProperty(0);
					
					statusCode = ActernityResponse.getProperty("code")
							.toString();
					if (statusCode.equalsIgnoreCase(SUCCESS_CODE)) {
						SoapObject data = (SoapObject) ActernityResponse
								.getProperty("data");
						callBack.handleResult(data,statusCode);					
					}else{
						callBack.handleError(statusCode);
					}
				}
			}else{
				callBack.handleError(statusCode);
			}

		} catch (IOException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		} catch (XmlPullParserException e) {
			e.printStackTrace();
			logger.error(e.getMessage());
		}finally{
			
		}
	}
	
	public interface ISOAPResultCallBack{
		public void handleResult(SoapObject data, String StatusCode);
		public void handleError(String StatusCode);
	}
	
}
