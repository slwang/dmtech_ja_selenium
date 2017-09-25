package com.m.test;

import java.util.HashMap;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.HasTouchScreen;
import org.openqa.selenium.interactions.TouchScreen;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteTouchScreen;

public class MTest {
	public static void main(String arg[]){
		System.out.println("M App AutoTest");
		String proPath = System.getProperty("user.dir");
		System.setProperty("webdriver.chrome.driver",
				proPath + "/chromedriver2.25/chromedriver.exe");

		Map<String, String> mobileEmulation = new HashMap<String, String>();    
		mobileEmulation.put("deviceName", "Apple iPhone 6");    
		Map<String, Object> chromeOptions = new HashMap<String, Object>();       
		chromeOptions.put("mobileEmulation", mobileEmulation);       
		DesiredCapabilities capabilities = DesiredCapabilities.chrome();         
		capabilities.setCapability(ChromeOptions.CAPABILITY, chromeOptions);   
		

		ChromeDriver driver=new MobileChromeDriver(capabilities); 
	}

	public class MobileChromeDriver extends ChromeDriver implements HasTouchScreen {  
		private RemoteTouchScreen touch;  

		public MobileChromeDriver(Capabilities capabilities) {  
			super(capabilities);  
			touch = new RemoteTouchScreen(getExecuteMethod());  
		}  

		public TouchScreen getTouch() {  
			return touch;  
		}  
	}  

}
