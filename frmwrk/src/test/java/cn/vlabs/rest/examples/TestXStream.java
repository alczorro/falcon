package cn.vlabs.rest.examples;

import static org.junit.Assert.assertNotNull;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.converters.ConversionException;

public class TestXStream {
	private XStream stream =new XStream();
	private String xml;
	private int count=1000;
	public TestXStream(){
		stream = new XStream();
		stream.processAnnotations(Person.class);
		Person p = new Person();
		p.name="Xiejj";
		p.age=100;
		xml = stream.toXML(p);
	}
	
	public void run(){
		for (int i=0;i<10;i++){
			MultiThreadRunner runner = new MultiThreadRunner(count, new RunnableFactory(){
				public Runnable createRunnable(int index) {
					return new Reader(index);
				}
			});
			runner.start();
		}
		System.out.println("All finished.");		
	}
	private class Reader implements Runnable{
		private int seq;
		public Reader(int seq){
			this.seq=seq;
		}
		public void run() {
			for (int i=0;i<10;i++){
				try {
					try {
						Thread.sleep((int)(Math.random()*3));
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
					Object obj = stream.fromXML(xml);
					assertNotNull("从Stream中返回的Object不应该为空", obj);
				}catch (ConversionException e){
					e.getCause().printStackTrace();
					System.err.println(e);
					System.err.println(xml);
				}catch(Throwable e){
					System.err.println(e.getClass());
					System.err.println(e.getMessage());
					System.err.println("Reader "+seq+" failed at "+(i+1)+"'s fromXML calls");
				}
			}
		}
	}
	
	private static class Person{
		public String name;
		public int age;
		public String toString(){
			return "MyName is "+name+", age is "+age;
		}
	}
	public static void main(String[] args){
		TestXStream tester = new TestXStream();
		tester.run();
	}
}
