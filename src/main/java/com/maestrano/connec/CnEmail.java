package com.maestrano.connec;

public class CnEmail extends CnBaseObject {
	private String address;
	private String address2;
	
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this._pcs.firePropertyChange("address", this.address, address);
		this.address = address;
	}
	public String getAddress2() {
		return address2;
	}
	public void setAddress2(String address2) {
		this._pcs.firePropertyChange("address2", this.address2, address2);
		this.address2 = address2;
	}
}
