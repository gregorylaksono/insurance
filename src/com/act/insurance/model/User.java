package com.act.insurance.model;

import java.io.Serializable;

public class User implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6320786168146061010L;
	
	private String sessionId;
	private String ca_id;
	private String ap_3lc;
	private String userId;
	private Integer addId;
	public String getSessionId() {
		return sessionId;
	}
	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}
	public Integer getAddId() {
		return addId;
	}
	public void setAddId(Integer addId) {
		this.addId = addId;
	}
	public String getCa_id() {
		return ca_id;
	}
	public void setCa_id(String ca_id) {
		this.ca_id = ca_id;
	}
	public String getAp_3lc() {
		return ap_3lc;
	}
	public void setAp_3lc(String ap_3lc) {
		this.ap_3lc = ap_3lc;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}

	

}
