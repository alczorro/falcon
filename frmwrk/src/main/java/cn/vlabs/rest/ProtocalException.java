package cn.vlabs.rest;

public class ProtocalException extends ServiceException {
	public ProtocalException(int code, String message) {
		super(code, message);
	}

	private static final long serialVersionUID = 7494063977663392840L;

}
