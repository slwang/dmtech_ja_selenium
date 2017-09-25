package com.wsl.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.core.Response.Status;

import org.junit.Assert;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chanjet.csp.rest.controller.qa.AppUserCommon;
import com.chanjet.csp.rest.controller.qa.EnvConstants;
import com.chanjet.csp.rest.controller.qa.HttpCommon;

public class RuntimeUtil {
  private static String version = null;
	public static String runCmd(String command) throws IOException,
			InterruptedException {
		StringBuffer returnStringBuffer = new StringBuffer();
		String[] cmd = new String[] { "cmd.exe", "/C", command };
		Runtime run = Runtime.getRuntime();
		Process process = run.exec(cmd);

		int returnValue = process.waitFor();
		System.out.println(returnValue);

		if (returnValue == 0) {
			InputStream is = process.getInputStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				returnStringBuffer.append(line);
				returnStringBuffer.append("\n");
			}
		}
		else{
			InputStream is = process.getErrorStream();
			BufferedReader reader = new BufferedReader(
					new InputStreamReader(is));
			String line = null;
			while ((line = reader.readLine()) != null) {
				System.out.println(line);
				returnStringBuffer.append(line);
				returnStringBuffer.append("\n");
			}
		}
		
		return returnStringBuffer.toString();
	}

	public static void main(String[] args) throws IOException,
			InterruptedException {
		String result = runCmd("..\\geronimo-tomcat7-javaee6-web-3.0.1\\bin\\deploy.bat -user system -password manager start com.chanjet.csp/myEnterpriseId1-com.chanapp.cspdemo.testapp/1.0/war");
//		String result = runCmd("dir");
		System.out.println(result);
	}
	
	public static void stopTestapp(){
    stopOrStartApp("testapp", true);
  }
  
  public static void stopTestapp2(){
    stopOrStartApp("testapp2", true);
  }
  
  public static void startTestapp(){
    stopOrStartApp("testapp", false);
  }
  
  public static void startTestapp2(){
    stopOrStartApp("testapp2", false);
  }
  
  private static void stopOrStartApp(String appName, boolean isStop){
    if(!isStop){
      restartEss();
    }
    String winTemplate = "..\\geronimo-tomcat7-javaee6-web-3.0.1\\bin\\deploy.bat -user system -password manager %s com.chanjet.csp/myEnterpriseId1-com.chanapp.cspdemo.%s/1.0/war";
    String otherTemplate = "../geronimo-tomcat7-javaee6-web-3.0.1/bin/deploy -user system -password manager %s com.chanjet.csp/myEnterpriseId1-com.chanapp.cspdemo.%s/1.0/war";
    String cmd = null;
    String[] envs = null;
    if(isWinOS()){
      List<String> envList = new ArrayList<String>();
      Map<String,String> envMaps = System.getenv();
      // The main purpose is to remove CLASS_PATH, to prevent the error in bat: input line is too long.
      for(String key: envMaps.keySet()){
        switch(key){
        case "PATH":
        case "JAVA_HOME":
          envList.add(key+"="+envMaps.get(key));  
        }
      }
      envs = envList.toArray(new String[envList.size()]);
      cmd = String.format("cmd /C " +winTemplate, isStop?"stop":"start", appName);
    }else{
      cmd = String.format(otherTemplate, isStop?"stop":"start", appName);
    }
    try{
      System.out.println("NOTE: "+ (isStop?"stop":"start") + " " + appName +" ......");
      Process process = Runtime.getRuntime().exec(cmd, envs);
      int returnValue = process.waitFor();
      
      if (returnValue == 0) {
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(is, "GBK"));
        String line = null;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
      }
      else{
        InputStream is = process.getErrorStream();
        BufferedReader reader = new BufferedReader(
            new InputStreamReader(is,"GBK"));
        String line = null;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
      }
      
      System.out.println("NOTE: "+ (isStop?"stop":"start") + " " + appName + (returnValue == 0?" success !":" failed !"));
    }catch(Exception e){
      Assert.fail("Run  \"" + cmd + "\" failed:" + e.getMessage());
    }
  }
  
  private static void restartEss(){
    int responseCode = 0;
    String responseString = "";
    if(version == null){
      //get csp version from /services/1.0/app/versions
      AppUserCommon appManager;
      try {
        appManager = new AppUserCommon(
          "enterprise.ccs.properties", "/enterprise", EnvConstants.isvtestUsername, EnvConstants.isvtestPassword);
        appManager.generateAppLoginToken();
        
        HttpURLConnection conn = appManager.doGet("/services/1.0/app/versions", null);
        
        responseCode = HttpCommon.getResponseCode(conn);
        responseString = HttpCommon.getResponseString(conn);
      } catch (IOException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      
      if(responseCode != Status.OK.getStatusCode()){  
        Assert.fail("Restart ESS failed, code: "+ responseCode + ", " + responseString);
      }
      JSONObject result = JSON.parseObject(responseString);
     
      if(!result.containsKey("cspVersion")){
        Assert.fail("Not found cspVersion in : "+ responseString);
      }
      version = result.getString("cspVersion");
    }
    
    String winTemplate =
        "..\\geronimo-tomcat7-javaee6-web-3.0.1\\bin\\deploy.bat -user system -password manager %s com.chanjet.csp/csp-ess-api-service/%s/war";
    String otherTemplate =
        "../geronimo-tomcat7-javaee6-web-3.0.1/bin/deploy -user system -password manager %s com.chanjet.csp/csp-ess-api-service/%s/war";
    String cmd = null;
    String[] envs = null;
    if (isWinOS()) {
      List<String> envList = new ArrayList<String>();
      Map<String, String> envMaps = System.getenv();
      // The main purpose is to remove CLASS_PATH, to prevent the error in
      // bat: input line is too long.
      for (String key : envMaps.keySet()) {
        switch (key) {
        case "PATH":
        case "JAVA_HOME":
          envList.add(key + "=" + envMaps.get(key));
        }
      }
      envs = envList.toArray(new String[envList.size()]);
      cmd = String.format("cmd /C " + winTemplate, "restart", version);
    } else {
      cmd = String.format(otherTemplate, "restart", version);
    }
    try {
      System.out.println("NOTE: restarting ESS ......");
      Process process = Runtime.getRuntime().exec(cmd, envs);
      int returnValue = process.waitFor();

      if (returnValue == 0) {
        InputStream is = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
        String line = null;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
      } else {
        InputStream is = process.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is, "GBK"));
        String line = null;
        while ((line = reader.readLine()) != null) {
          System.out.println(line);
        }
      }

      System.out.println(
        "NOTE: restart ESS"  + (returnValue == 0 ? " success !" : " failed !"));
    } catch (Exception e) {
      Assert.fail("Run  \"" + cmd + "\" failed:" + e.getMessage());
    }
  }
  
  private static boolean isWinOS(){
    return System.getProperty("os.name").contains("Windows");
  }
}
