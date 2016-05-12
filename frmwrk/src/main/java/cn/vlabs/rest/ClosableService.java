package cn.vlabs.rest;

public interface ClosableService {
	/**
	 * 获取ServiceContext
	 * @return 包含的serviceContext。
	 */
	ServiceContext getContext();
	/**
	 * 关闭所有的http连接
	 *
	 */
	void close();
}