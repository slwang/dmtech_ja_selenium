package com.wsl.utils.http;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 * http基础类
 * @author Administrator
 */
public class HttpCommon {
  public static final int HTTP_TIMEOUT = 30 * 1000;

  public static HttpURLConnection doGet(String urlString, Map<String, String> getParams) throws IOException {
    return _doHttp(urlString, "GET", null, getParams, null);
  }

  public static HttpURLConnection doGet(String urlString, Map<String, String> headers, Map<String, String> getParams)
      throws IOException {
    return _doHttp(urlString, "GET", headers, getParams, null);
  }

  public static HttpURLConnection doPost(String urlString, Map<String, String> getParams,
      Map<String, String> bodyParams) throws IOException {
    String postBody = generatorHttpParams(bodyParams);
    return _doHttp(urlString, "POST", null, getParams, postBody);
  }

  public static HttpURLConnection doPost(String urlString, Map<String, String> headers, Map<String, String> getParams,
      Map<String, String> bodyParams) throws IOException {
    String postBody = generatorHttpParams(bodyParams);
    return _doHttp(urlString, "POST", headers, getParams, postBody);
  }

  public static HttpURLConnection doPost(String urlString, Map<String, String> headers, Map<String, String> getParams,
      String postBody) throws IOException {
    return _doHttp(urlString, "POST", headers, getParams, postBody);
  }

  public static HttpURLConnection doPut(String urlString, Map<String, String> getParams, Map<String, String> bodyParams)
      throws IOException {
    String postBody = generatorHttpParams(bodyParams);
    return _doHttp(urlString, "PUT", null, getParams, postBody);
  }

  public static HttpURLConnection doPut(String urlString, Map<String, String> headers, Map<String, String> getParams,
      Map<String, String> bodyParams) throws IOException {
    String postBody = generatorHttpParams(bodyParams);
    return _doHttp(urlString, "PUT", headers, getParams, postBody);
  }

  public static HttpURLConnection doPut(String urlString, Map<String, String> headers, Map<String, String> getParams,
      String postBody) throws IOException {
    return _doHttp(urlString, "PUT", headers, getParams, postBody);
  }

  public static HttpURLConnection doDelete(String urlString, Map<String, String> getParams) throws IOException {
    return _doHttp(urlString, "DELETE", null, getParams, null);
  }

  public static HttpURLConnection doDelete(String urlString, Map<String, String> headers, Map<String, String> getParams)
      throws IOException {
    return _doHttp(urlString, "DELETE", headers, getParams, null);
  }

  public static HttpURLConnection doHttp(String urlString, String httpMethod, Map<String, String> getParams,
      Map<String, String> bodyParams) throws IOException {
    String postBody = generatorHttpParams(bodyParams);
    return _doHttp(urlString, httpMethod, null, getParams, postBody);
  }

  public static HttpURLConnection doHttp(String urlString, String httpMethod, Map<String, String> headers,
      Map<String, String> getParams, Map<String, String> bodyParams) throws IOException {
    String postBody = generatorHttpParams(bodyParams);
    return _doHttp(urlString, httpMethod, headers, getParams, postBody);
  }

  public static HttpURLConnection doHttp(String urlString, String httpMethod, Map<String, String> headers,
      Map<String, String> getParams, String postBody) throws IOException {
    return _doHttp(urlString, httpMethod, headers, getParams, postBody);
  }

  public static String getResponseString(HttpURLConnection conn) throws IOException {
    String text = "";
    InputStream is = null;
    InputStreamReader sr = null;
    BufferedReader br = null;
    try {
      if (conn.getResponseCode() > 200) {
        is = conn.getErrorStream();
      } else {
        is = conn.getInputStream();
      }

      if(is==null)
      {
    	    return null;
      }
      
      sr = new InputStreamReader(is);
      br = new BufferedReader(sr);
      String line = br.readLine();
      while (line != null) {
        text += line;
        line = br.readLine();
      }
    } finally {
      if (is != null) {
        is.close();
      }
      if (sr != null) {
        sr.close();
      }
    }
    return text;
  }

  public static void disconnect(HttpURLConnection conn) {
    conn.disconnect();
  }

  public static int getResponseCode(HttpURLConnection conn) throws IOException {
    return conn.getResponseCode();
  }

  public static String getHeaderField(HttpURLConnection conn, String name) {
    return conn.getHeaderField(name);
  }

  public static String generatorHttpParams(Map<String, String> params) {
    if (params == null) {
      return null;
    }
    StringBuffer paramsBuf = new StringBuffer();
    Iterator<Entry<String, String>> iterator = params.entrySet().iterator();
    while (iterator.hasNext()) {
      Entry<String, String> entry = iterator.next();
      String key = entry.getKey();
      String value = entry.getValue();
      paramsBuf.append(key + "=" + URLEncoder.encode(value));
      if (iterator.hasNext()) {
        paramsBuf.append("&");
      }
    }
    return paramsBuf.toString();
  }

  private static HttpURLConnection _doHttp(String urlString, String httpMethod, Map<String, String> headers,
      Map<String, String> getParams, String postBody) throws IOException {
    String getParamsString = generatorHttpParams(getParams);
    URL url;
    if (getParamsString != null && !getParamsString.equals("")) {
      url = new URL(urlString + "?" + getParamsString);
    } else {
      url = new URL(urlString);
    }

    HttpURLConnection conn = null;
    int retryTime = 0;
    while(true){
      boolean success = true; 
      try {
        conn = _doHttp(httpMethod, headers, postBody, url);
        getResponseCode(conn);
      } catch (IOException e) {
        retryTime ++;
        success = false;
        if(retryTime ==3){
          throw e;
        }
        System.out.println("Retry connection: "+url);
        System.out.println("  "+ e.getClass().getName() + ": "+ e.getMessage());
      }
      if(success){
        break;
      }
    }
    
    return conn;
  }

  private static HttpURLConnection _doHttp(String httpMethod, Map<String, String> headers, String postBody, URL url)
      throws IOException, ProtocolException {
    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
    if (headers != null) {
      Iterator<Entry<String, String>> iterator = headers.entrySet().iterator();
      while (iterator.hasNext()) {
        Entry<String, String> entry = iterator.next();
        String key = entry.getKey();
        String value = entry.getValue();
        conn.setRequestProperty(key, value);
      }
    }
    conn.setRequestMethod(httpMethod);
    conn.setInstanceFollowRedirects(false);
    conn.setDoOutput(true);
    conn.setConnectTimeout(HTTP_TIMEOUT);
    conn.setUseCaches(false);
    if (postBody != null) {
      OutputStream outStream = conn.getOutputStream();
      outStream.write(postBody.getBytes());
      outStream.flush();
      outStream.close();
    } else {
      conn.connect();
    }
    return conn;
  }
}
