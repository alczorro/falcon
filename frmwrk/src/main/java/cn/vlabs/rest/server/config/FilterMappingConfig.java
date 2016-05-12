package cn.vlabs.rest.server.config;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class FilterMappingConfig {
	@XStreamAlias("filter")
	private String filterName;

	@XStreamImplicit(itemFieldName = "service")
	private ArrayList<String> services;

	public void setFilterName(String filterName) {
		this.filterName = filterName;
	}

	public String getFilterName() {
		return filterName;
	}

	public ArrayList<String> getServices() {
		return services;
	}
	public FilterMappingConfig(){}
}
