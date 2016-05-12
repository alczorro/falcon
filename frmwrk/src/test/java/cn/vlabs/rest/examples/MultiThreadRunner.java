package cn.vlabs.rest.examples;

import java.util.concurrent.Semaphore;

public class MultiThreadRunner {
	private Semaphore semaphore;

	private Thread[] threads;

	private class RunnableWrapper implements Runnable {
		private Runnable cmd;

		public RunnableWrapper(Runnable cmd) {
			this.cmd = cmd;
		}

		public void run() {
			try {
				cmd.run();
			} finally {
				semaphore.release();
			}
		}
	}
	public MultiThreadRunner(int threadCount, Class jobClass){
		this(threadCount, new DefaultRunnableFactory(jobClass));
	}
	public MultiThreadRunner(int threadCount, RunnableFactory factory) {
		threads = new Thread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			RunnableWrapper wrapper = new RunnableWrapper(factory
					.createRunnable(i));
			threads[i] = new Thread(wrapper);
		}
		semaphore = new Semaphore(threadCount, true);
		try {
			semaphore.acquire(threadCount);
		} catch (InterruptedException e) {
			System.err.println("init runner failed due to "+e.getMessage());
		}
	}
	public void start(){
		for (Thread thread:threads){
			thread.start();
		}
		int finished = 0;
		while (finished<threads.length){
			try {
				semaphore.acquire();
				finished++;
			} catch (InterruptedException e) {
			}
		}
	}
}
