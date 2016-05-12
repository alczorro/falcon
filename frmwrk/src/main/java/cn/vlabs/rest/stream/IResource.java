package cn.vlabs.rest.stream;

import java.io.InputStream;

public interface IResource {

	long getLength();

	String getMimeType();

	String getFilename();

	InputStream getInputStream();
}