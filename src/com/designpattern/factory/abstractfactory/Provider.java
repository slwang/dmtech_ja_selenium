package com.designpattern.factory.abstractfactory;

import com.designpattern.factory.simplefactory.PrintInterface;

/**抽象工厂模式就是创建多个工厂类，这样一旦需要增加新的功能，直接增加新的工厂类就可以了，不需要修改之前的代码。
 * PrintInterface  PrintOne   PrintTwo 代码不变
 * @author Administrator
 *
 */
public interface Provider {
	public  PrintInterface produce();
}
