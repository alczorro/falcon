package cn.vlabs.rest.server.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamImplicit;

@XStreamAlias("ServiceConfig")
public class ServiceConfig {
	@XStreamImplicit(itemFieldName = "service")
	private ArrayList<ServiceItem> services;

	@XStreamImplicit(itemFieldName = "filter")
	private ArrayList<FilterItem> filters;

	@XStreamImplicit(itemFieldName = "filter-mapping")
	private ArrayList<FilterMappingConfig> filterMappings;

	public static ServiceConfig fromConfig(String file) throws IOException{
		InputStreamReader reader =new InputStreamReader(new FileInputStream(file), "utf-8");
		try{
			XStream xstream = new XStream();
			xstream.processAnnotations(new Class[]{
					ServiceConfig.class,
					ServiceItem.class,
					ParamItem.class,
					FilterMappingConfig.class,
					FilterItem.class});
			xstream.autodetectAnnotations(true);
			return (ServiceConfig)xstream.fromXML(reader);
		}finally{
			reader.close();
		}
	}

	public ArrayList<ServiceItem> getServices() {
		return services;
	}

	public ArrayList<FilterItem> getFilters() {
		return filters;
	}

	public ArrayList<FilterMappingConfig> getFilterMappings() {
		return filterMappings;
	}
	
	public ServiceConfig(){}
}
