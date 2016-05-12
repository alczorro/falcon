package cn.vlabs.rest.examples.action;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import cn.vlabs.rest.RestSession;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.ServiceWithInputStream;

public class UploadFile extends ServiceWithInputStream {
	public Object doAction(RestSession session, Object request) throws ServiceException {
		InputStream in =this.getStream().getInputStream();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			try {
				String line = reader.readLine();
				reader.close();
				return request+line;
			} catch (IOException e) {
				System.out.println("Error while reading file");
			}
		} catch (UnsupportedEncodingException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return "";
	}

}