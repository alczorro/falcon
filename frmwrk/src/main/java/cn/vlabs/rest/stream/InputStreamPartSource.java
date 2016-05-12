package cn.vlabs.rest.stream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.httpclient.methods.multipart.PartSource;


public class InputStreamPartSource implements PartSource{
	public InputStreamPartSource(IResource info) {
		this.info = info;
	}

	public InputStream createInputStream() throws IOException {
		return info.getInputStream();
	}

	public String getFileName() {
		return info.getFilename();
	}

	public long getLength() {
		return info.getLength();
	}

	private IResource info;

}
