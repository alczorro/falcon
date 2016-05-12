package cn.vlabs.rest.examples;

import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.apache.log4j.PropertyConfigurator;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.rest.ServiceContext;
import cn.vlabs.rest.ServiceException;

public class SystemServiceTest {
	private ServiceContext context;
	private SystemService service;
	private FileService fileService;
	@Before
	public void setUp() throws Exception {
		ServiceContext.setMaxConnection(5, 10);
		PropertyConfigurator.configure("WebRoot/WEB-INF/log4j.properties");
		context = new ServiceContext(
				"http://localhost/framework/ServiceServlet");
		context.setKeepAlive(true);
		service = new SystemService(context);
		fileService = new FileService(context);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testMultiThread() throws ServiceException, InterruptedException {
		MultiThreadRunner runner = new MultiThreadRunner(1000,
				new RunnableFactory() {
					public Runnable createRunnable(int index) {
						return new JobRunner(service);
					}
				});
		long startTime = System.currentTimeMillis();
		try {
			runner.start();
			System.out
					.println((System.currentTimeMillis() - startTime) / 3000.0);
		} finally {
			context.close();
		}
	}

	private static class JobRunner implements Runnable {
		private SystemService service;

		public JobRunner(SystemService service) {
			this.service = service;
		}

		public void run() {
			for (int i = 0; i < 3; i++) {
				try {
					assertEquals("ABC", service.echo("ABC"));
				} catch (ServiceException e) {
					e.printStackTrace();
				}
			}
		}
	}

	@Test
	public void testEcho() throws ServiceException, InterruptedException {
		assertEquals("中文", service.echo("中文"));
		assertEquals("true", service.echo("true"));
	}

	@Test
	public void testPerformance() throws ServiceException {
		long before, after;
		int count = 3000;
		try {
			before = System.currentTimeMillis();
			for (int i = 0; i < count; i++) {
				service.echo("ABC");
			}
			after = System.currentTimeMillis();
		} finally {
			context.close();
		}
		System.out.println("the average consumed time is "
				+ ((after - before) / (float) count));
	}

	@Test
	public void testGetFrameWorkInfo() throws ServiceException {
		String version = service.getFrameWorkInfo();
		System.out.println("Framework Version: " + version);
	}

	@Test
	public void testUploadFile() throws ServiceException, IOException{
		String result = fileService.upload("注意", "test/upload/1.txt");
		assertEquals("注意不是所有的付出都有回报！", result);
	}
	@Test
	public void testDownloadFile() throws ServiceException, IOException{
		String result = fileService.download();
		assertEquals("不是所有的付出都有回报！", result);
	}
	@Test
	public void testSession() throws ServiceException{
		assertEquals("abc",service.testSession("abc"));
	}
	@Test
	public void testAdd() throws ServiceException{
		assertEquals(5,service.add(2,3));
	}
}
