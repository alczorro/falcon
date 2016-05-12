package cn.vlabs.rest.server.dispatcher;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import javax.servlet.ServletContext;

import cn.vlabs.rest.server.Capability;
import cn.vlabs.rest.server.config.ParamItem;
import cn.vlabs.rest.server.filter.RestContext;

public class RestContextImpl implements RestContext {
	private ServletContext context;

	private HashMap<String, LinkedList<String>> params;

	public RestContextImpl(ServletContext context,
			ArrayList<ParamItem> paramItems) {
		this.context = context;
		params = new HashMap<String, LinkedList<String>>();
		for (ParamItem item : paramItems) {
			LinkedList<String> values = params.get(item.getName());
			if (values == null) {
				values = new LinkedList<String>();
				params.put(item.getName(), values);
			}
			values.add(item.getValue());
		}
	}

	public String getFramkeworkVersion() {
		return Capability.FrameVersoin;
	}

	public String getParameter(String param) {
		if (param != null) {
			LinkedList<String> values = params.get(param);
			if (values != null && values.size() > 0) {
				return values.get(0);
			}
		}
		return null;
	}

	public String[] getParameters(String param) {
		if (param != null) {
			LinkedList<String> values = params.get(param);
			if (values != null && values.size() > 0) {
				return values.toArray(new String[0]);
			}
		}
		return null;
	}

	public String getRealPath(String path) {
		return context.getRealPath(path);
	}
}
