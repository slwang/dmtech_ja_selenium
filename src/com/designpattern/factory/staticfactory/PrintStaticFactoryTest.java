package com.designpattern.factory.staticfactory;

import com.designpattern.factory.simplefactory.PrintInterface;

/**普通工厂模式就是建立一个工厂类，对实现了同一接口的一些类进行实例的创建。
 * 简单工厂测试类
 * @author Administrator
 *
 */
public class PrintStaticFactoryTest {
	public static void main(String[] args){
		//PrintFactory pf = new PrintFactory();
		PrintInterface pi = PrintStaticFactory.produceOne();
		pi.print();
		pi = PrintStaticFactory.produceTwo();
		pi.print();
	}

}
