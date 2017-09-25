package com.designpattern.factory.abstractfactory;

import com.designpattern.factory.simplefactory.PrintInterface;

public class PrintAbstractFactoryTest {

	public static void main(String[] args) {
		Provider provider =  new PrintAbstractFactoryOne();
		PrintInterface p = provider.produce();
		p.print();
	}

}
