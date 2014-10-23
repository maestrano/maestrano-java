package com.maestrano.testhelpers;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionContext;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;

@SuppressWarnings("deprecation")
public class HttpSessionStub implements HttpSession {
	private Map<String,Object> attributes;
	
	public HttpSessionStub() {
		this.attributes = new HashMap<String,Object>();
	}
	
	public void setMnoSessionTo(Map<String,String> map) {
		// Encode session
        Gson gson = new Gson();
        String sessStr = gson.toJson(map);
        sessStr = DatatypeConverter.printBase64Binary(sessStr.getBytes());
        
        // Finally store the maestrano session
        this.setAttribute("maestrano", sessStr);
	}
	
	public Object getAttribute(String arg0) {
		return attributes.get(arg0);
	}

	@SuppressWarnings("rawtypes")
	public Enumeration getAttributeNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getCreationTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public String getId() {
		// TODO Auto-generated method stub
		return null;
	}

	public long getLastAccessedTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	public int getMaxInactiveInterval() {
		// TODO Auto-generated method stub
		return 0;
	}

	public ServletContext getServletContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public HttpSessionContext getSessionContext() {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getValue(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	public String[] getValueNames() {
		// TODO Auto-generated method stub
		return null;
	}

	public void invalidate() {
		// TODO Auto-generated method stub
		
	}

	public boolean isNew() {
		// TODO Auto-generated method stub
		return false;
	}

	public void putValue(String arg0, Object arg1) {
		// TODO Auto-generated method stub
		
	}

	public void removeAttribute(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void removeValue(String arg0) {
		// TODO Auto-generated method stub
		
	}

	public void setAttribute(String arg0, Object arg1) {
		attributes.put(arg0,arg1);
	}

	public void setMaxInactiveInterval(int arg0) {
		// TODO Auto-generated method stub
		
	}
}
