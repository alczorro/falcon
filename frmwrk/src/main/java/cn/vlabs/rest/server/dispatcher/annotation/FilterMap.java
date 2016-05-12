package cn.vlabs.rest.server.dispatcher.annotation;

import java.util.ArrayList;
import java.util.regex.Pattern;

public class FilterMap {
	private ArrayList<Pattern> patterns;
	private ArrayList<String> regexs;
	private String filterName;
	public FilterMap(ArrayList<String> regexs, String filterName){
		this.regexs=regexs;
		this.filterName=filterName;
		if (regexs!=null){
			patterns = new ArrayList<Pattern>();
			for (String regex:regexs){
				if (regex!=null){
					regex = regex.trim();
					if (regex.length()!=0){
						regex = regex.replaceAll("\\*", ".*");
						patterns.add(Pattern.compile(regex));
					}
				}
			}
		}
	}
	
	public String toString(){
		StringBuilder builder = new StringBuilder();
		builder.append("filter:"+filterName+"\n");
		if (regexs!=null){
			for (String regex:regexs){
				builder.append("service:"+regex+"\n");
			}
		}
		return builder.toString();
	}
	public String getFilterName(){
		return this.filterName;
	}
	
	public boolean match(String serviceName){
		if (serviceName!=null && patterns!=null){
			for (Pattern pattern:patterns){
				if (pattern.matcher(serviceName).matches()){
					return true;
				}
			}
		}
		return false;
	}
}
