package cn.vlabs.rest.server.dispatcher.annotation;

import javax.servlet.http.HttpServletRequest;

import cn.vlabs.rest.server.filter.RequestContext;

public class HttpRequestContext implements RequestContext {
	private HttpServletRequest request;
	private String serviceName;
	private String methodName;
	private String fullServiceName;
	public HttpRequestContext(HttpServletRequest request){
		this.request = request;
	}
	public void setService(String fullServiceName){
		int dotIndex = fullServiceName.indexOf('.');
		if (dotIndex!=-1){
			serviceName = fullServiceName.substring(0, dotIndex);
			methodName= fullServiceName.substring(dotIndex + 1);
		}else{
			serviceName=fullServiceName;
			methodName =null;
		}
		this.fullServiceName=fullServiceName;
	}
	public int getLocalPort() {
		return request.getLocalPort();
	}

	public String getRemoteAddr() {
		if (request.getHeader("x-forwarded-for") == null) {  
			 return request.getRemoteAddr();  
		}  
		return request.getHeader("x-forwarded-for");  
	}

	public String getRemoteHost() {
		return request.getRemoteHost();
	}

	public int getRemotePort() {
		return request.getRemotePort();
	}
	public String getMethodName() {
		return methodName;
	}
	public String getServiceName() {
		return serviceName;
	}
	public String getFullServiceName() {
		return fullServiceName;
	}
}
