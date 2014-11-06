package com.maestrano.connec;

public class CnAddress extends CnBaseObject {
	private String line1;
	private String line2;
	private String city = null;
	private String region;
	private String postalCode;
	private String country;
	
	public CnAddress() {
		super();
	}
	
	public String getLine1() {
		return line1;
	}
	public void setLine1(String line1) {
		this._pcs.firePropertyChange("line1", this.line1, line1);
		this.line1 = line1;
	}
	public String getLine2() {
		return line2;
	}
	public void setLine2(String line2) {
		this._pcs.firePropertyChange("line2", this.line2, line2);
		this.line2 = line2;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this._pcs.firePropertyChange("city", this.city, city);
		this.city = city;
	}
	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this._pcs.firePropertyChange("region", this.region, region);
		this.region = region;
	}
	public String getPostalCode() {
		return postalCode;
	}
	public void setPostalCode(String postalCode) {
		this._pcs.firePropertyChange("postalCode", this.postalCode, postalCode);
		this.postalCode = postalCode;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this._pcs.firePropertyChange("country", this.country, country);
		this.country = country;
	}
}
