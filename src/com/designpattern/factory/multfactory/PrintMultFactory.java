package com.designpattern.factory.multfactory;

import com.designpattern.factory.simplefactory.PrintInterface;
import com.designpattern.factory.simplefactory.PrintOne;
import com.designpattern.factory.simplefactory.PrintTwo;

/**
 * 多工厂类
 * 多个工厂方法模式，是对普通工厂方法模式的改进，多个工厂方法模式就是提供多个工厂方法，分别创建对象。
 * @author Administrator
 *
 */
public class PrintMultFactory {
	 public PrintInterface produceOne() {  
         return new PrintOne();        
 }

 public PrintInterface produceTwo() {  
     return new PrintTwo();  
 }
 
	
}
