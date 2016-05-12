package cn.vlabs.rest.server;

import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.protocal.Envelope;
import cn.vlabs.rest.protocal.MessageErrors;

public class Predefined {
	public static final Envelope AUTHORIZE_FAILED;

	public static final Envelope MISSING_HEAD;

	public static final Envelope PARSE_ERROR;

	static {
		PARSE_ERROR = createErrorEnvelope(ServiceException.ERROR_PARSE_MESSAGE,
				"Can't read request message.");

		MISSING_HEAD = createErrorEnvelope(ServiceException.ERROR_MISSING_HEAD,
				"Message head is required.");
		AUTHORIZE_FAILED = createErrorEnvelope(
				ServiceException.ERROR_ACCESS_FORBIDDEN, "Authorize failed.");
	}

	public static Envelope fromException(ServiceException e) {
		Envelope response = new Envelope();
		response.setErrors(new MessageErrors(e.getCode(), e.getMessage()));
		return response;
	}
	public static Envelope serviceNotFound(String service){
		Envelope envelope = createErrorEnvelope(
				ServiceException.ERROR_SERVICE_NOT_FOUND,
				"Service("+service+")you requested not found.");
		return envelope;
	}
	public static Envelope internalExceptionEnvelope(String message) {
		Envelope response = new Envelope();
		response.setErrors(new MessageErrors(
				ServiceException.ERROR_INTERNAL_ERROR, message));
		return response;
	}
	private static Envelope createErrorEnvelope(int code, String message) {
		Envelope envelope = new Envelope();
		envelope.setErrors(new MessageErrors(code, message));
		return envelope;
	}

	private Predefined() {
	};
}
