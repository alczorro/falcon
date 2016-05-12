package cn.vlabs.rest.examples;


import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import cn.vlabs.rest.ServiceException;

public class UploadStubTest {

	@Before
	public void setUp() throws Exception {
		stub = new UploadStub(TestHelper.getContext());
	}

	@After
	public void tearDown() throws Exception {
	}
	@Test
	public void testUpload() throws ServiceException, IOException {
		String result = stub.upload("注意", "test/upload/1.txt");
		assertEquals("注意不是所有的付出都有回报！", result);
	}
	private UploadStub stub;
}