package cn.vlabs.rest.server.dispatcher;

import java.io.IOException;

import javax.servlet.ServletContext;

import cn.vlabs.rest.server.Dispatcher;
import cn.vlabs.rest.server.dispatcher.annotation.AnnotationBasedDispatcher;
import cn.vlabs.rest.server.dispatcher.claasic.ServiceDispatcher;

public class DispatcherFactory {
	public static Dispatcher getDispatcher(ServletContext context, String version, String configFile) throws IOException{
		if ("1.0".equals(version)){
			return new AnnotationBasedDispatcher(context, configFile);
		}else{
			return new ServiceDispatcher(configFile);
		}
	}
}
