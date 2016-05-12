/**
 * 
 */
package cn.vlabs.rest;

/**
 * @author lvly
 * @since 2013-4-8
 */
public class HttpsAbnormalExcpetion extends RuntimeException{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6198825300604408993L;

	public HttpsAbnormalExcpetion(String message ,Exception e){
		super(message,e);
	}

}
