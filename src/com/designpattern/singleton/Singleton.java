package com.designpattern.singleton;

/**
 * 单例模式有以下特点：
　　1、单例类只能有一个实例。
　　2、单例类必须自己创建自己的唯一实例。
　　3、单例类必须给所有其他对象提供这一实例。


 * //懒汉式单例类.在第一次调用的时候实例化自己 
 * @author wsl
 *
 */
public class Singleton {
	private Singleton(){};
	private static Singleton single = null;

	//静态工厂方法
	private static Singleton getInstance(){
		if (single == null){
			single = new Singleton();
		}
		return single;
	}


	/*
	 * 在getInstance方法上加同步
	 * */
	/*public static synchronized Singleton getInstance() {  
        if (single == null) {    
            single = new Singleton();  
        }    
       return single;  
	}  */
	
	/**
	 * 双重检查锁定
	 * @return
	 */
	/*public static Singleton getInstance() {  
        if (singleton == null) {    
            synchronized (Singleton.class) {    
               if (singleton == null) {    
                  singleton = new Singleton();   
               }    
            }    
        }    
        return singleton;   
    }  
*/
	
	
	/**
	 * 静态内部类
	 * 这种比上面1、2都好一些，既实现了线程安全，又避免了同步带来的性能影响。
	 * @author Administrator
	 *
	 */
	/*public class Singleton {    
	    private static class LazyHolder {    
	       private static final Singleton INSTANCE = new Singleton();    
	    }    
	    private Singleton (){}    
	    public static final Singleton getInstance() {    
	       return LazyHolder.INSTANCE;    
	    }    
	}    */
	
}
