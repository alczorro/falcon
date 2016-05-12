package cn.vlabs.rest.stream;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import org.apache.commons.httpclient.methods.multipart.PartSource;

public class StringBufferPartSource implements PartSource{
	public StringBufferPartSource(String value){
		try {
			bytes = value.getBytes("UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		length = bytes.length;
	}
	public InputStream createInputStream() throws IOException {
		return new ByteArrayInputStream(bytes);
	}

	public String getFileName() {
		return "request.xml";
	}

	public long getLength() {
		return length;
	}
	private byte[] bytes;
	private long length;
}
