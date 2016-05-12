package cn.vlabs.rest.stream;

import java.io.InputStream;

public class StreamInfo implements IResource {
	public void setLength(long length) {
		this.length = length;
	}

	/* (non-Javadoc)
	 * @see cn.vlabs.rest.stream.IResource#getLength()
	 */
	public long getLength() {
		return length;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	/* (non-Javadoc)
	 * @see cn.vlabs.rest.stream.IResource#getMimeType()
	 */
	public String getMimeType() {
		return mimeType;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	/* (non-Javadoc)
	 * @see cn.vlabs.rest.stream.IResource#getFilename()
	 */
	public String getFilename() {
		return filename;
	}

	public void setInputStream(InputStream inputStream) {
		this.inputStream = inputStream;
	}
	/* (non-Javadoc)
	 * @see cn.vlabs.rest.stream.IResource#getInputStream()
	 */
	public InputStream getInputStream() {
		return inputStream;
	}

	private String mimeType;

	private String filename;

	private long length;

	private InputStream inputStream;

}
