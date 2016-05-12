package cn.vlabs.rest.server.config;

import com.thoughtworks.xstream.annotations.XStreamAlias;

public class ServiceItem {
	@XStreamAlias("name")
	private String name;

	@XStreamAlias("class")
	private String clazz;

	public ServiceItem(String name, String clazz) {
		this.name = name;
		this.clazz = clazz;
	}

	public String getName() {
		return name;
	}

	public String getClazz() {
		return clazz;
	}
	public ServiceItem(){}
}
