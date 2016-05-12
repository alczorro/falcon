package cn.vlabs.rest.server.dispatcher.annotation;

import java.lang.reflect.Method;

import cn.vlabs.rest.RestSession;

public class MethodInvoker {
	private Method method;
	private boolean sessionRequired;
	private String methodName;

	public MethodInvoker(String methodName, Method method) {
		this.method = method;
		Class<?>[] paramTypes = method.getParameterTypes();
		if (paramTypes != null && paramTypes.length > 0) {
			sessionRequired = ClassUtil.hasInertface(paramTypes[0],
					RestSession.class);
		} else {
			sessionRequired = false;
		}
		this.methodName = methodName;
	}

	public Method getMethod() {
		return method;
	}

	public boolean isSessionRequired() {
		return sessionRequired;
	}

	public String getMethodName() {
		return this.methodName;
	}
}
