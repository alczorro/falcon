package cn.vlabs.rest.server.dispatcher;

import javax.servlet.http.HttpSession;

import cn.vlabs.rest.FrameWorkInfo;
import cn.vlabs.rest.RestSession;

public class RestSessionImpl implements RestSession {
	public RestSessionImpl(HttpSession session) {
		this.session=session;
	}
	public Object getAttribute(String name){
		return session.getAttribute(name);
	}
	public void setAttribute(String name, Object val){
		session.setAttribute(name, val);
	}
	public FrameWorkInfo getFrameWorkInfo(){
		return info;
	}
	public static void setFrameWorkInfo(FrameWorkInfo info){
		RestSessionImpl.info=info;
	}
	private HttpSession session;
	private static FrameWorkInfo info;
}
