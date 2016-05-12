package cn.vlabs.rest;

public abstract class AbstractService implements ClosableService {
	public AbstractService(ServiceContext context){
		this.context=context;
	}
	
	public void close() {
		context.close();
	}

	public ServiceContext getContext() {
		return context;
	}
	private ServiceContext context;
}
