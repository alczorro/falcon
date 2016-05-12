package cn.vlabs.rest.server;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import cn.vlabs.rest.Constant;
import cn.vlabs.rest.server.dispatcher.DispatcherFactory;

public class ServiceServlet extends HttpServlet {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor of the object.
	 */
	public ServiceServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		if (dispatcher!=null){
			dispatcher.destroy();
			dispatcher=null;
		}
	}

	public void init() throws ServletException {
		charset = getServletContext().getInitParameter("charset");
		if (charset==null){
			charset = Constant.DEFAULT_CHARSET;
		}
		
		String prefix = getServletContext().getRealPath("/");
		String configFile = prefix + getInitParameter("config");
		
		String version = getInitParameter("version");
		try {
			dispatcher = DispatcherFactory.getDispatcher(getServletContext(), version, configFile);
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		request.setCharacterEncoding(charset);
		dispatcher.doService(request, response);
	}
	private String charset;
	private Dispatcher dispatcher;
}
