package cn.vlabs.rest.server.config;

import java.util.ArrayList;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

public class FilterItem {
	@XStreamAlias("name")
	private String name;

	@XStreamAlias("class")
	private String clazz;

	@XStreamImplicit(itemFieldName = "init-param")
	private ArrayList<ParamItem> initparams;
	
	public FilterItem(){}

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setClazz(String clazz) {
		this.clazz = clazz;
	}

	public String getClazz() {
		return clazz;
	}

	public void setInitParams(ArrayList<ParamItem> initparams) {
		this.initparams = initparams;
	}

	public ArrayList<ParamItem> getInitParams() {
		return initparams;
	}
}
