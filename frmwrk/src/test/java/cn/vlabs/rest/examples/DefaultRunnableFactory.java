package cn.vlabs.rest.examples;

public class DefaultRunnableFactory implements RunnableFactory {
	private Class jobClass ;
	public DefaultRunnableFactory(Class jobClass){
		this.jobClass=jobClass;
	}
	public Runnable createRunnable(int index) {
		try {
			return (Runnable)jobClass.newInstance();
		} catch (InstantiationException e) {
			throw new IllegalArgumentException(jobClass+"'s default constructor is required.");
		} catch (IllegalAccessException e) {
			throw new IllegalArgumentException("Can't access class "+jobClass+"'s default constructor.");
		}
	}

}
