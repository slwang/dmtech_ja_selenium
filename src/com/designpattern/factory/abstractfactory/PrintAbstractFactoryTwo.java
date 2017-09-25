package com.designpattern.factory.abstractfactory;

import com.designpattern.factory.simplefactory.PrintInterface;
import com.designpattern.factory.simplefactory.PrintTwo;

public class PrintAbstractFactoryTwo  implements Provider{

	@Override
	public PrintInterface produce() {
		 return new PrintTwo();
	}

}
