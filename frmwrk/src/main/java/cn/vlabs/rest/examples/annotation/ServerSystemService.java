package cn.vlabs.rest.examples.annotation;

import javax.servlet.ServletContext;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.server.Capability;
import cn.vlabs.rest.server.annotation.Destroy;
import cn.vlabs.rest.server.annotation.Init;
import cn.vlabs.rest.server.annotation.RestMethod;

public class ServerSystemService {
	@Init
	public void init(ServletContext context){
//		System.out.println("Init method is called. Context path ="+context.getContextPath());
	}
	
	@Destroy
	public void destroy(){
//		System.out.println("destroy method is called.");
	}
	@RestMethod("echo")
	public String echo(String value){
		return value;
	}
	@RestMethod("version")
	public String getVersion(){
		return Capability.FrameVersoin;
	}
	@RestMethod("testSession")
	public String getMethod(RestSession session, String message){
		System.out.println(session.toString());
		return message;
	}
	@RestMethod("add")
	public int add(int a, int b){
		return a+b;
	}
}
