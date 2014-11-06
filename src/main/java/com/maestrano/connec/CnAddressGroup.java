package com.maestrano.connec;

public class CnAddressGroup extends CnBaseObject {
	private CnAddress billing;
	private CnAddress billing2;
	private CnAddress shipping;
	private CnAddress shipping2;
	
	public void activateListeners() {
		if (this.billing != null) this.billing.setParentListener("billing", this._pcs);
		if (this.billing2 != null) this.billing2.setParentListener("billing2", this._pcs);
		if (this.shipping != null) this.shipping.setParentListener("shipping", this._pcs);
		if (this.shipping2 != null) this.shipping2.setParentListener("shipping2", this._pcs);
	}
	
	public CnAddress getBilling() {
		return billing;
	}
	public void setBilling(CnAddress billing) {
		this.billing.unsetParentListener();
		this.billing = billing;
		this.billing.activateListeners("phone", this._pcs);
	}
	public CnAddress getBilling2() {
		return billing2;
	}
	public void setBilling2(CnAddress billing2) {		
		this.billing2.unsetParentListener();
		this.billing2 = billing2;
		this.billing2.activateListeners("billing2", this._pcs);
	}
	public CnAddress getShipping() {
		return shipping;
	}
	public void setShipping(CnAddress shipping) {
		this.shipping.unsetParentListener();
		this.shipping = shipping;
		this.shipping.activateListeners("shipping", this._pcs);
	}
	public CnAddress getShipping2() {
		return shipping2;
	}
	public void setShipping2(CnAddress shipping2) {
		this.shipping2.unsetParentListener();
		this.shipping2 = shipping;
		this.shipping2.activateListeners("shipping2", this._pcs);
	}
	
	
}
