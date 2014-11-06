package com.maestrano.connec;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class CnBaseObject implements PropertyChangeListener {
	public PropertyChangeSupport _parentPcs;
	protected String _parentPcsAttr;
	protected PropertyChangeSupport _pcs = new PropertyChangeSupport(this);
	
	protected Map<String,Object> changedAttributes;
	protected Map<String,Object> orginalAttributes;
	
	public CnBaseObject() {
		changedAttributes = new HashMap<String,Object>();
		orginalAttributes = new HashMap<String,Object>();
		_pcs.addPropertyChangeListener(this);
	}
	
	public void propertyChange(PropertyChangeEvent evt) {
		changeAttribute(evt.getPropertyName(), evt.getOldValue(), evt.getNewValue());
		
		if (_parentPcs != null && _parentPcsAttr != null) {
			String prop = _parentPcsAttr + "." + evt.getPropertyName();
			_parentPcs.firePropertyChange(prop, evt.getOldValue(), evt.getNewValue());
		}	
	}
	
	public void activateListeners(String parentAttrName, PropertyChangeSupport parentPcs) {
		this.setParentListener(parentAttrName, parentPcs);
		this.activateListeners();
	}
	
	public void activateListeners() {}
	
	public void setParentListener(String attrName, PropertyChangeSupport pcs) {
		this._parentPcs = pcs;
		this._parentPcsAttr = attrName;
	}
	
	public void unsetParentListener() {
		this._parentPcs = null;
		this._parentPcsAttr = null;
	}
	
	public Boolean isChanged() {
		return !this.changedAttributes.isEmpty();
	}
	
	public Map<String,Object> getChangedAttributes() {
		return changedAttributes;
	}
	
	protected void changeAttribute(String attrName, Object oldVal, Object newVal) {
		try {
			
			//Field f = this.getClass().getDeclaredField(attrName);
			//Object currentVal = f.get(this);
			//f.set(this,value);
			
			if (this.orginalAttributes.get(attrName) == null) {
				this.orginalAttributes.put(attrName, oldVal);
			}
			
			if (this.orginalAttributes.get(attrName) != null 
					&& this.orginalAttributes.get(attrName).equals(newVal)) {
				this.changedAttributes.remove(attrName);
				this.orginalAttributes.remove(attrName);
			} else {
				this.changedAttributes.put(attrName, newVal);
			}
		
		} catch (Exception e) {}
	}
	
	protected void merge(Object obj) {
		Field[] fs = this.getClass().getDeclaredFields();
		
		for (Field f : fs) {
			try {
				f.set(this,f.get(obj));
			} catch (Exception e) {}
		}
	}
}
