package com.designpattern.factory.multfactory;

import com.designpattern.factory.simplefactory.PrintInterface;

/**普通工厂模式就是建立一个工厂类，对实现了同一接口的一些类进行实例的创建。
 * 简单工厂测试类
 * @author Administrator
 *
 */
public class PrintMultFactoryTest {
	public static void main(String[] args){
		PrintMultFactory pf = new PrintMultFactory();
		PrintInterface pi = pf.produceOne();
		pi.print();
		pi = pf.produceTwo();
		pi.print();
	}

}
