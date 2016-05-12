package cn.vlabs.rest.examples.annotation;

import java.lang.reflect.Method;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.protocal.Envelope;
import cn.vlabs.rest.server.Predefined;
import cn.vlabs.rest.server.filter.Filter;
import cn.vlabs.rest.server.filter.RequestContext;
import cn.vlabs.rest.server.filter.RestContext;

public class AllFilter implements Filter{
	private String service;

	public void destroy() {
		
	}

	public void init(RestContext context) {
		service = context.getParameter("block");
	}

	public Envelope doFilter(Method method, Object[] params, RequestContext context, RestSession session) {
		if (service.equals(context.getFullServiceName())){
			System.out.println(context.getFullServiceName());
			System.out.println("Blocked");
			return Predefined.serviceNotFound(context.getFullServiceName());
		}
		return null;
	}
}
