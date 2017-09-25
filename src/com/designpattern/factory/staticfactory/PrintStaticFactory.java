package com.designpattern.factory.staticfactory;

import com.designpattern.factory.simplefactory.PrintInterface;
import com.designpattern.factory.simplefactory.PrintOne;
import com.designpattern.factory.simplefactory.PrintTwo;

/**
 * 静态
 * 静态工厂方法模式，将上面的多个工厂方法模式里的方法置为静态的，不需要创建实例，直接调用即可。
 * @author Administrator
 *
 */
public class PrintStaticFactory {
	 public static  PrintInterface produceOne() {  
         return new PrintOne();        
 }

 public  static PrintInterface produceTwo() {  
     return new PrintTwo();  
 }
 
	
}
