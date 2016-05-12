package cn.vlabs.rest.examples;

import cn.vlabs.rest.ServiceContext;



public class TestHelper {
	public static ServiceContext getContext(){
		if (context==null){
			context = new ServiceContext("http://localhost/framework/ServiceServlet");
		}
		return context;
	}
	private static ServiceContext context=null;
}
