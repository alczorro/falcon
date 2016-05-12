package cn.vlabs.rest.server.dispatcher;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;

import cn.vlabs.rest.protocal.Envelope;
import cn.vlabs.rest.server.Dispatcher;
import cn.vlabs.rest.server.Predefined;

public abstract class BaseDispatcher implements Dispatcher {
	private Logger log=Logger.getLogger(BaseDispatcher.class);
	protected Envelope requestXMLNotFound(HttpServletRequest request){
		log.error("Error parse request, due to input xml(named in 'RequestXML') is not found.");
		log.error(dumpRequest(request));
		return Predefined.PARSE_ERROR;
	}
	private String dumpRequest(HttpServletRequest request){
		StringBuilder buff = new StringBuilder();
		buff.append("Parameter Map:\n");
		Enumeration<String> iter = request.getParameterNames();
		while (iter.hasMoreElements()){
			String key = (String)iter.nextElement();
			String[] values = request.getParameterValues(key);
			buff.append(key).append(":");
			buff.append('[');
			if (values!=null){
				boolean first =true;
				for (String value:values){
					if (!first){
						buff.append(",{"+value+"}");
					}else{
						buff.append("{"+value+"}");
						first=false;
					}
				}
			}
			buff.append("]\n");
		}
		return buff.toString();
	}
}
