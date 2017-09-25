package com.wsl.utils;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ReflectionUtil {
	public static Object newInstance(String className, Class[] argsClass, Object[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class classType = Class.forName(className);
		Constructor constructor = classType.getDeclaredConstructor(argsClass);
		constructor.setAccessible(true);
		return constructor.newInstance(args);
	}
	
	public static Object newInstance(String className, Object[] args) throws ClassNotFoundException, NoSuchMethodException, SecurityException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		Class[] argsClass = new Class[args.length];
		for(int i = 0; i < args.length; i++){
			argsClass[i] = args[i].getClass();
		}
		
		Class classType = Class.forName(className);
		Constructor constructor = classType.getDeclaredConstructor(argsClass);
		constructor.setAccessible(true);
		return constructor.newInstance(args);
	}
}
