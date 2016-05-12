package cn.vlabs.rest.examples.annotation;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;

import cn.vlabs.rest.server.annotation.RestMethod;
import cn.vlabs.rest.stream.IResource;
import cn.vlabs.rest.stream.StreamInfo;

public class FileExample {
	@RestMethod("readFile")
	public String readFile(String request, IResource resource) {
		InputStream in = resource.getInputStream();
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(in, "utf-8"));
			try {
				String line = reader.readLine();
				reader.close();
				return request + line;
			} catch (IOException e) {
				System.err.println("Error while reading file");
			}
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace(System.err);
		}
		return "";
	}
	@RestMethod("writeFile")
	public IResource writeFile() throws FileNotFoundException{
		File f = new File("c:\\1.txt");
		StreamInfo stream = new StreamInfo();
		stream.setFilename("c:\\1.txt");
		stream.setLength(f.length());
		stream.setMimeType("plain/text");
		stream.setInputStream(new FileInputStream(f));
		return stream;
	}
}
