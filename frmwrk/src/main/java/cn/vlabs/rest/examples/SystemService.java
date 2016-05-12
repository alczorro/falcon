package cn.vlabs.rest.examples;

import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;

public class SystemService {
	public SystemService(ServiceContext context){
		client = new ServiceClient(context);
	}
	
	public String echo(String msg) throws ServiceException{
		return (String) client.sendService("System.echo", msg);
	}
	public String getFrameWorkInfo() throws ServiceException{
		return (String) client.sendService("System.version",null);
	}
	public String testSession(String message) throws ServiceException{
		return (String)client.sendService("System.testSession", message);
	}
	public int add(int i, int j) throws ServiceException {
		return ((Integer)client.sendService("System.add", new Object[]{i,j})).intValue();
	}
	private ServiceClient client;
}
