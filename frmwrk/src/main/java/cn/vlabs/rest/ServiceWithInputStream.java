package cn.vlabs.rest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vlabs.rest.stream.IResource;

public abstract class ServiceWithInputStream implements ServiceAction {
	/**
	 * 设置服务的Response对象
	 * @param response
	 */
	public void setResponse(HttpServletResponse response) {
		this.response = response;
	}
	/**
	 * 需要按照自己格式输出服务，可以从这里获取Response对象.
	 * @return
	 */
	protected HttpServletResponse getResponse() {
		return response;
	}
	/**
	 * 设置服务需要用到的Stream对象。这个只有在服务框架接收到有文件附件时才调用。
	 * @param stream
	 */
	public void setStream(IResource stream) {
		this.stream = stream;
	}

	/**
	 * 需要处理附带文件的服务，可以调用该接口。
	 * @return
	 */
	protected IResource getStream() {
		return stream;
	}

	public void setRequest(HttpServletRequest request) {
		this.request = request;
	}
	public HttpServletRequest getRequest() {
		return request;
	}

	private IResource stream;

	private HttpServletResponse response;
	private HttpServletRequest request;
}