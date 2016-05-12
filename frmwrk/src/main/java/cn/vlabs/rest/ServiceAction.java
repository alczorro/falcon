package cn.vlabs.rest;


public interface ServiceAction {
	Object doAction(RestSession session, Object request)throws ServiceException;
}
