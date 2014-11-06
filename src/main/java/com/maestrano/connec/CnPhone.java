package com.maestrano.connec;

public class CnPhone extends CnBaseObject {
	private String landline;
	private String landline2;
	private String mobile;
	private String mobile2;
	private String fax;
	private String fax2;
	private String pager;
	private String pager2;
	
	public String getLandline() {
		return landline;
	}
	public void setLandline(String landline) {
		this._pcs.firePropertyChange("landline", this.landline, landline);
		this.landline = landline;
	}
	public String getLandline2() {
		return landline2;
	}
	public void setLandline2(String landline2) {
		this._pcs.firePropertyChange("landline2", this.landline2, landline2);
		this.landline2 = landline2;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this._pcs.firePropertyChange("mobile", this.mobile, mobile);
		this.mobile = mobile;
	}
	public String getMobile2() {
		return mobile2;
	}
	public void setMobile2(String mobile2) {
		this._pcs.firePropertyChange("mobile2", this.mobile2, mobile2);
		this.mobile2 = mobile2;
	}
	public String getFax() {
		return fax;
	}
	public void setFax(String fax) {
		this._pcs.firePropertyChange("fax", this.fax, fax);
		this.fax = fax;
	}
	public String getFax2() {
		return fax2;
	}
	public void setFax2(String fax2) {
		this._pcs.firePropertyChange("fax2", this.fax2, fax2);
		this.fax2 = fax2;
	}
	public String getPager() {
		return pager;
	}
	public void setPager(String pager) {
		this._pcs.firePropertyChange("pager", this.pager, pager);
		this.pager = pager;
	}
	public String getPager2() {
		return pager2;
	}
	public void setPager2(String pager2) {
		this._pcs.firePropertyChange("pager2", this.pager2, pager2);
		this.pager2 = pager2;
	}
	
	
}
