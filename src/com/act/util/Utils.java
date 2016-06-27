package com.act.util;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Utils {
	public static String checkNull(Object s){
		if(s == null)return "";
		else return s.toString();
	}
	
	public static String getWebserviceURL(){
		String url = load("webservice.url");
		return url;
	}

	private static String load(String string) {
		Properties prop = new Properties();
		String value = null;
		try {
			InputStream input = Utils.class.getResourceAsStream("/config.properties");;
			prop.load(input);
			value = prop.getProperty(string);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		};
		return value;
	}
	
//	public static void main(String[] args){
//		System.out.println(getWebserviceURL());
//	}
}
