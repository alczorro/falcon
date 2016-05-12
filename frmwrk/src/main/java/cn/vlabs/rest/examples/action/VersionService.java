package cn.vlabs.rest.examples.action;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceAction;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.server.Capability;

public class VersionService implements ServiceAction{
	public Object doAction(RestSession session, Object request) throws ServiceException {
		return Capability.FrameVersoin;
	}

}
