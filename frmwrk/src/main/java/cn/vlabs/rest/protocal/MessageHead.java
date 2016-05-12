package cn.vlabs.rest.protocal;

public class MessageHead {
	public void setAppName(String appName) {
		this.appName = appName;
	}

	public String getAppName() {
		return appName;
	}

	public void setAppPassword(String appPassword) {
		this.appPassword = appPassword;
	}

	public String getAppPassword() {
		return appPassword;
	}

	public void setService(String service) {
		this.service = service;
	}

	public String getService() {
		return service;
	}

	public void setHasStream(boolean hasStream) {
		this.hasStream = hasStream;
	}

	public boolean isHasStream() {
		return hasStream;
	}

	private String service;

	private String appName;

	private String appPassword;
	
	private boolean hasStream;
}
