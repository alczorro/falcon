package cn.vlabs.rest.server.filter;

public interface RestContext {
	String getParameter(String param);
	String[] getParameters(String param);
	String getFramkeworkVersion();
	String getRealPath(String path);
}
