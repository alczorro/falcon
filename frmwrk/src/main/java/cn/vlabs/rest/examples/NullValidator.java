package cn.vlabs.rest.examples;

import cn.vlabs.rest.AuthValidator;

public class NullValidator implements AuthValidator {
	public boolean validAuthorize(String appname, String apppassword) {
		return true;
	}
}
