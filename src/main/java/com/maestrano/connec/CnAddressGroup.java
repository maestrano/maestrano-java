package com.maestrano.connec;

public class CnAddressGroup extends ConnecObject {
	private CnAddress billing;
	private CnAddress billing2;
	private CnAddress shipping;
	private CnAddress shipping2;
	
	public CnAddress getBilling() {
		return billing;
	}
	public void setBilling(CnAddress billing) {
		this.billing = billing;
	}
	public CnAddress getBilling2() {
		return billing2;
	}
	public void setBilling2(CnAddress billing2) {		
		this.billing2 = billing2;
	}
	public CnAddress getShipping() {
		return shipping;
	}
	public void setShipping(CnAddress shipping) {
		this.shipping = shipping;
	}
	public CnAddress getShipping2() {
		return shipping2;
	}
	public void setShipping2(CnAddress shipping2) {
		this.shipping2 = shipping;
	}
	
	
}
