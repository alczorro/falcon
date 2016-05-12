package cn.vlabs.rest.protocal;

import cn.vlabs.rest.ProtocalException;
import cn.vlabs.rest.ServiceException;

public class MessageErrors {
	public MessageErrors() {

	}

	public MessageErrors(int code, String message) {
		this.code = code;
		this.message = message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	public ServiceException toException(){
		if (code<ServiceException.ERROR_CUSTOM_START){
			return new ProtocalException(code, message);
		}else{
			return new ServiceException(code, message);
		}
	}
	private int code;

	private String message;
}
