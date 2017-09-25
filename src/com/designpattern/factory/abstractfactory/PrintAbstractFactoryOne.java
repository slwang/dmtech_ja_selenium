package com.designpattern.factory.abstractfactory;

import com.designpattern.factory.simplefactory.PrintInterface;
import com.designpattern.factory.simplefactory.PrintOne;

public class PrintAbstractFactoryOne implements Provider{

	@Override
	public PrintInterface produce() {
		 return new PrintOne();
	}

}
