package com.maestrano.connec;

public class CnWebsite extends CnBaseObject {
	private String url;
	private String url2;
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this._pcs.firePropertyChange("url", this.url, url);
		this.url = url;
	}
	public String getUrl2() {
		return url2;
	}
	public void setUrl2(String url2) {
		this._pcs.firePropertyChange("url2", this.url2, url2);
		this.url2 = url2;
	}
}
