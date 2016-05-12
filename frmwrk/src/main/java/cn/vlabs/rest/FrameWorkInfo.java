package cn.vlabs.rest;

public class FrameWorkInfo {
	public FrameWorkInfo(){
		apiType="";
		apiVersion="";
		frameVersion="";
	}

	public FrameWorkInfo(String apiType, String apiVersion, String frameVersion){
		this.apiType=apiType;
		this.apiVersion=apiVersion;
		this.frameVersion=frameVersion;
	}
	public void setApiType(String apiType) {
		this.apiType = apiType;
	}
	public String getApiType() {
		return apiType;
	}

	public void setApiVersion(String apiVersion) {
		this.apiVersion = apiVersion;
	}

	public String getApiVersion() {
		return apiVersion;
	}

	public void setFrameVersion(String frameversion) {
		this.frameVersion = frameversion;
	}

	public String getFrameVersion() {
		return frameVersion;
	}
	private String apiType;
	private String apiVersion;
	private String frameVersion;
}
