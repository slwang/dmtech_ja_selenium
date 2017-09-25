package com.designpattern.factory.simplefactory;

/**
 * 简单工厂类
 * @author Administrator
 *普通工厂模式就是建立一个工厂类，对实现了同一接口的一些类进行实例的创建。
 */
public class PrintFactory {
	 public PrintInterface produce(String type) {  
	        if ("One".equals(type)) {  
	            return new PrintOne();  
	        } else if ("Two".equals(type)) {  
	            return new PrintTwo();  
	        } else {  
	            System.out.println("没有要找的类型");  
	            return null;  
	        }  
	    }
}
