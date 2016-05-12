package cn.vlabs.rest;

import org.apache.commons.httpclient.HttpException;

public class ConnectException extends ServiceException {
	public ConnectException(int code, HttpException e, String url) {
		super(code, e.getMessage());
		this.url=url;
	}
	public ConnectException(int code, Exception e, String url){
		super(code, e);
		this.url = url;
	}
	public ConnectException(int code, String message, String url) {
		super(code, message);
		this.url =url;
	}
	public String toString(){
		return "Error ocurred while access url "+url+". caused by \n"+getMessage();
	}
	private String url;
	private static final long serialVersionUID = 1L;

}
