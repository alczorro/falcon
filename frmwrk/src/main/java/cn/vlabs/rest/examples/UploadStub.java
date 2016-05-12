package cn.vlabs.rest.examples;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.stream.StreamInfo;

public class UploadStub {
	public UploadStub(ServiceContext context){
		this.context=context;
	}
	public String upload(String message, String file) throws ServiceException, IOException{
		ServiceClient client = new ServiceClient(context);
		StreamInfo stream = new StreamInfo();
		File f = new File(file);
		stream.setFilename(f.getName());
		stream.setInputStream(new FileInputStream(f));
		stream.setLength(f.length());
		String result =(String)client.sendService("System.upload", message, stream);
		stream.getInputStream().close();
		return result;
	}
	private ServiceContext context;
}
