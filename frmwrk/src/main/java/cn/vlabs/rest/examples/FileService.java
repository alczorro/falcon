package cn.vlabs.rest.examples;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import cn.vlabs.rest.IFileSaver;
import cn.vlabs.rest.ServiceClient;
import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;
import cn.vlabs.rest.stream.StreamInfo;

public class FileService {
	private ServiceClient client;
	public FileService(ServiceContext context){
		client = new ServiceClient(context);
	}
	
	public String upload(String message, String file) throws ServiceException, IOException{
		StreamInfo stream = new StreamInfo();
		File f = new File(file);
		stream.setFilename(f.getName());
		stream.setInputStream(new FileInputStream(f));
		stream.setLength(f.length());
		String result =(String)client.sendService("FileTest.readFile", message, stream);
		stream.getInputStream().close();
		return result;
	}
	
	public String download() throws ServiceException{
		DemoSaver saver = new DemoSaver(){
			private String firstLine;
			public void save(String filename, InputStream in) {
				try {
					BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
					firstLine = reader.readLine();
				} catch (UnsupportedEncodingException e) {
					firstLine=null;
				} catch (IOException e) {
					firstLine=null;
				}
			}
			public String getFirstLine(){
				return firstLine;
			}
		};
		client.sendService("FileTest.writeFile", null, saver);
		return saver.getFirstLine();
		
	}
	private static class DemoSaver implements IFileSaver{
		private String firstLine;
		public void save(String filename, InputStream in) {
			try {
				BufferedReader reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
				firstLine = reader.readLine();
			} catch (UnsupportedEncodingException e) {
				firstLine=null;
			} catch (IOException e) {
				firstLine=null;
			}
		}
		public String getFirstLine(){
			return firstLine;
		}
	};
}
