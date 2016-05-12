package cn.vlabs.rest.protocal;

public class Envelope {
	public void setHead(MessageHead head) {
		this.head = head;
	}

	public MessageHead getHead() {
		return head;
	}

	public void setErrors(MessageErrors errors) {
		this.errors = errors;
	}

	public MessageErrors getErrors() {
		return errors;
	}

	public void setBody(Object body) {
		this.body = body;
	}

	public Object getBody() {
		return body;
	}

	private MessageHead head;

	private MessageErrors errors;

	private Object body;
	public static final int SC_HAS_ERROR=203;
	public static final Envelope EMPTY= new Envelope();
}
