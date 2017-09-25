package com.designpattern.factory.simplefactory;

/**普通工厂模式就是建立一个工厂类，对实现了同一接口的一些类进行实例的创建。
 * 简单工厂测试类
 * @author Administrator
 *
 */
public class PrintFactoryTest {
	public static void main(String[] args){
		PrintFactory pf = new PrintFactory();
		PrintInterface pi = pf.produce("Two");
		pi.print();
	}

}
