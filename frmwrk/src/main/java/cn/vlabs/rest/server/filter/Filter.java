package cn.vlabs.rest.server.filter;

import java.lang.reflect.Method;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.protocal.Envelope;

public interface Filter {
	void init(RestContext context);
	void destroy();
	Envelope doFilter(Method method, Object[] params, RequestContext context, RestSession session);
}
