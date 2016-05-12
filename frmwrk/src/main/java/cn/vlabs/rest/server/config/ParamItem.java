package cn.vlabs.rest.server.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ParamItem {
	@XStreamAlias("param-name")
	private String name;

	@XStreamAlias("param-value")
	private String value;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
	public ParamItem(){}
}
