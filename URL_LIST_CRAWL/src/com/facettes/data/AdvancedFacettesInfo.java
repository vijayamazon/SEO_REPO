package com.facettes.data;

public class AdvancedFacettesInfo {
	private int id;
	private String url;
	private String facetteName;
	private String facetteValue;
	private int facetteCount;
	private String products_size;
	private boolean is_opened;
	private String opened_facette_url="";
	public boolean isIs_opened() {
		return is_opened;
	}
	public void setIs_opened(boolean is_opened) {
		this.is_opened = is_opened;
	}
	public String getOpened_facette_url() {
		return opened_facette_url;
	}
	public void setOpened_facette_url(String opened_facette_url) {
		this.opened_facette_url = opened_facette_url;
	}
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public String getFacetteName() {
		return facetteName;
	}
	public void setFacetteName(String facetteName) {
		this.facetteName = facetteName;
	}
	public String getFacetteValue() {
		return facetteValue;
	}
	public void setFacetteValue(String facetteValue) {
		this.facetteValue = facetteValue;
	}
	public int getFacetteCount() {
		return facetteCount;
	}
	public void setFacetteCount(int facetteCount) {
		this.facetteCount = facetteCount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getProducts_size() {
		return products_size;
	}
	public void setProducts_size(String products_size) {
		this.products_size = products_size;
	}
}
