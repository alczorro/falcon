package cn.vlabs.rest.stream;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.fileupload.FileItem;

public class FormFile implements IResource {
	public FormFile(FileItem file){
		this.file=file;
	}

	public long getLength() {
		return file.getSize();
	}

	public String getMimeType() {
		return file.getContentType();
	}


	public String getFilename() {
		return file.getName();
	}
	/**
	 * 获得返回的输入流。
	 * @return 这个输入流只能打开一次，而且必须由调用者关闭。
	 */
	public InputStream getInputStream() {
		try {
			return file.getInputStream();
		} catch (IOException e) {
			return null;
		}
	}

	private FileItem file ;
}
