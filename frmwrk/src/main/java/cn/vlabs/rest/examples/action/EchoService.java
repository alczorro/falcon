package cn.vlabs.rest.examples.action;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceAction;
import cn.vlabs.rest.ServiceException;

public class EchoService implements ServiceAction {

	public Object doAction(RestSession session, Object request) throws ServiceException {
		return request;
	}

}
