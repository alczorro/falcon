package cn.vlabs.rest.server;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface Dispatcher {
	void doService(HttpServletRequest httpRequest, HttpServletResponse httpResponse) throws IOException;
	void destroy();
}
