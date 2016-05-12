package cn.vlabs.rest;

import java.io.InputStream;

public interface IFileSaver {
	/**
	 * 文件保存接口
	 * @param filename 被保存的文件的名称
	 * @param in 输入流
	 */
	void save(String filename, InputStream in);
}