package cn.vlabs.rest;


public interface RestSession {
	public Object getAttribute(String name);
	public void setAttribute(String name, Object val);
}
