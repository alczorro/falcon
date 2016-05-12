package cn.vlabs.rest;

public class ServiceException extends Exception {
	private static final long serialVersionUID = 1L;
	public ServiceException(int code, Exception e){
		super(e);
		this.code = code;
	}
	public ServiceException(int code, String message) {
		super(message);
		this.code = code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public int getCode() {
		return code;
	}

	private int code;

	public static final int ERROR_CUSTOM_START = 400;

	public static final int ERROR_HTTP_CONNECT_FAIL = 5;

	public static final int ERROR_ACCESS_FORBIDDEN = 4;

	public static final int ERROR_SERVICE_NOT_FOUND = 3;

	public static final int ERROR_MISSING_HEAD = 2;

	public static final int ERROR_PARSE_MESSAGE = 1;

	public static final int ERROR_DATABASE_FAILED = 6;
	public static final int ERROR_INTERNAL_ERROR=7;
}
