package cn.vlabs.rest.server.filter;

public interface RequestContext {
	String getRemoteAddr();
	String getRemoteHost();
	int getLocalPort();
	int getRemotePort();
	String getServiceName();
	String getMethodName();
	void setService(String service);
	String getFullServiceName();
}
