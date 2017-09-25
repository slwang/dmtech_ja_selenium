package com.chanjet.test.httpproxy;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class HttpProxy extends Thread {
  static public String LINE_SEP = "\r\n";
  static public int CONNECT_RETRIES = 5;
  static public int CONNECT_PAUSE = 5;
  static public int TIMEOUT = 30000;
  static public int BUFSIZ = 1024;
  static public boolean logging = false;
  static public OutputStream log = null;
  // 传入数据用的Socket
  private Socket socket;
  // 上级代理服务器，可选
  private String parent = null;
  private Integer parentPort = -1;
  private Md5Tool md5 = null;
  private static Map<String, Integer> callTimes = new HashMap<String, Integer>(); //同一类请求记录请求次数;
  private static boolean isDummy = false;
  private static String testMethod = null;
  
  public static boolean isDummy() {
    return isDummy;
  }

  public static void setDummy(boolean isDummy) {
    HttpProxy.isDummy = isDummy;
  }

  // 在给定Socket上创建一个代理线程。
  public HttpProxy(Socket s) {
    socket = s;
    start();
  }

  private void writeLog(int c, boolean output) throws IOException {
    if(!logging && !output) return;
    log.write(c);
  }

  private void writeLog(byte[] bytes, int offset, int len, boolean output) throws IOException {
    if(!logging && !output) return;
    for (int i = 0; i < len; i++)
      writeLog((int) bytes[offset + i], true);
  }
  
  private void writeLog(String msg, boolean output) throws IOException {
    if(logging || output){
      byte[] bytes = msg.getBytes();
      writeLog(bytes,0, bytes.length, true);
    }
  }

  // 执行操作的线程
  public void run() {
    String line;
    String host;
    int port = 80;
    try {
      md5=Md5Tool.getInstance();
      socket.setSoTimeout(TIMEOUT);
      InputStream is = socket.getInputStream();
      
      writeLog(LINE_SEP + LINE_SEP + "************NEW REQUEST**********" + LINE_SEP, false);
      writeLog("request input stream ..."+LINE_SEP, false);
      try {
        // 获取请求行的内容
        line = "";
        host = "";
        int state = 0;
        boolean space;
        while (true) {
          int c = is.read();
          if (c == -1) break;
          writeLog(c, false);
          space = Character.isWhitespace((char) c);
          switch (state) {
          case 0: //remove space
            if (space) continue;
            state = 1;
          case 1: //method
            if (space) {
              state = 2;
              continue;
            }
            line = line + (char) c;
            break;
          case 2: 
            if (space) continue; // 跳过多个空白字符
            state = 3;
          case 3: //url
            if (space) {
              state = 4;  //proxy request
              // 只取出主机名称部分
              String host0 = host;
              int n;
              n = host.indexOf("//");
              if (n != -1) host = host.substring(n + 2);
              n = host.indexOf('/');
              if (n != -1) host = host.substring(0, n);
              // 分析可能存在的端口号
              n = host.indexOf(":");
              if (n != -1) {
                port = Integer.parseInt(host.substring(n + 1));
                host = host.substring(0, n);
              }
              if(setProxyServer(host0, host, port, socket)){
                return;
              }
              host = parent;
              port = parentPort;
              if(isDummy)
                doDummyProxy(is, host, port, line, host0);
              else
                doProxy(is, host, port, line, host0);
              return;
            }
            host = host + (char) c;
            break;
          }
        }
      } catch (IOException e) {
        e.printStackTrace();
      }

    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        socket.close();
      } catch (Exception e1) {
      }
    }
  }
  
  private void doDummyProxy(InputStream srcInputStream, String host, Integer port, String method, String url) throws IOException{
    
    writeLog(LINE_SEP + method + " " + host + url + " " + LINE_SEP, true);
    
    md5Url(method, url);  
    
    Map<String, String> headers = __readHeaders(srcInputStream);
    if(headers.containsKey("Content-Length")){
      int bodyLen = Integer.parseInt(headers.get("Content-Length"));
      __readRequestStream(bodyLen, srcInputStream, null); 
    }
    
    OutputStream srcOutputStream = socket.getOutputStream();
    
    String key = md5.getMD5();
    Integer index = HttpProxy.getCallTimeAndAdd(key);
    
    Map<String, List<String>> headerFields = DummyUtil.getResponseHeader(testMethod, key, index, md5.getDynamicUrl());
    System.out.println(String.format("\n Get %s %s key=%s, index=%d, dynamicUrl=%s", "head", testMethod, key, index, md5.getDynamicUrl()));
    __writeHeaders(srcOutputStream, headerFields);
    InputStream tgtInputStream = DummyUtil.getResponseContent(testMethod, key, index, md5.getDynamicUrl());
    __writeResponseStreamFromDummy(tgtInputStream, srcOutputStream);
    System.out.println(String.format("\n Get %s %s key=%s, index=%d, dynamicUrl=%s", "content", testMethod, key, index, md5.getDynamicUrl()));
    
    tgtInputStream.close();
  }
  
  private static Integer getCallTimeAndAdd(String key){
    String k = testMethod+key;
    if(!callTimes.containsKey(k)){
      callTimes.put(k, 0);
    }
    callTimes.put(k, callTimes.get(k)+1);
    return callTimes.get(k);
  }
  
  private void md5Url(String method, String url){
    /*
     对于上传文件：oss.chanapp.chanjet.com/ufile/, oss.chanapp.chanjet.com/image/
     和下载文件：sto.chanapp.chanjet.com， 由于url通常以时间为变量，因此只以文件内容为md5
    */
    String dynamicUrl = null;
    if((parent.equals("oss.chanapp.chanjet.com") && (url.startsWith("/ufile/")) || url.startsWith("/image/"))){
      dynamicUrl = url.substring(6);
    }
    if((parent.equals("oss.chanapp.chanjet.com") && (url.startsWith("/convert/")))){
      dynamicUrl = url.substring(8);
    }else if(parent.startsWith("sto.chanapp.chanjet.com")){
      dynamicUrl = url;
      if(dynamicUrl.startsWith("/image/")){
        dynamicUrl = url.substring(6);
      }
    }
    if(dynamicUrl != null){
      md5.setDynamicUrl(dynamicUrl);
      return;
    }
    md5.update(method+" "+url);  
    
    System.out.println("url -> md5: ");
    System.out.println("|"+method+" "+url+"| -> ");
  }

  
  private void doProxy(InputStream srcInputStream, String host, Integer port, String method, String url)
      throws InterruptedException, IOException {
    
    writeLog(LINE_SEP + method + " " + host + url + " " + LINE_SEP, true);
    
    md5Url(method, url);  
    
    Map<String, String> headers = __readHeaders(srcInputStream);
    int bodyLen = 0;
    
    if(headers.containsKey("Content-Length")){
      bodyLen = Integer.parseInt(headers.get("Content-Length"));
      headers.remove("Content-Length");
    }
    
    int retry = CONNECT_RETRIES;
    HttpURLConnection conn = null;
    while (retry-- != 0) {
      try {
        URL u = new URL("http://"+host+":"+port+url); 
        
        //java.text.DateFormat cal = java.text.DateFormat.getDateTimeInstance();
        //writeLog(LINE_SEP+"=======================", false);
        //writeLog(LINE_SEP + cal.format(new java.util.Date()) + " - " + method + " "+ u.toString() + " " + LINE_SEP, true);
        conn = (HttpURLConnection)u.openConnection();
        conn.setDoOutput(true);  
        conn.setDoInput(true);
        conn.setUseCaches(false);
        conn.setConnectTimeout(TIMEOUT);
        conn.setRequestMethod(method);
        for(Entry<String,String> entry : headers.entrySet()){
          conn.setRequestProperty(entry.getKey(), entry.getValue());  
        }
        //fix 404 error for some cia api
        if((
              url.contains("/api/v1/subscribe/authroizeUser") ||
              url.contains("/api/v1/user/checkAppSubscribe") ||
              url.contains("/api/v1/user/app/cancelAppManager") || 
              url.contains("/api/v1/subscribe/unauthroizeUser") ||
              url.contains("/api/v1/org/cancelOrgManager") ||
              url.contains("/api/v1/org/orgManager") ||
              url.contains("/api/v1/user/app/appManager") 
            ) && method.equals("POST")){
          conn.setRequestProperty("Accept", "text/plain");
        }else if(url.contains("/internal_api/internal_logout") && method.equals("GET")){
          conn.setRequestProperty("Accept", "text/plain");
        }
        conn.setRequestProperty("Accept-Encoding", "deflate");
        conn.connect();
        break;
      } catch (Exception e) {
      }
      // 等待
      Thread.sleep(CONNECT_PAUSE);
    }
    if (conn == null) {
      writeLog("conntect to target server failed"+LINE_SEP, true);
      return;
    }
    pipe(srcInputStream, socket.getOutputStream(), conn, bodyLen);
  }
  
  private void pipe(InputStream srcInputStream, OutputStream srcOutputStream, HttpURLConnection conn, int bodyLen) throws IOException {
    
    OutputStream tgtOutputStream = null;
    InputStream tgtInputStream = null;
    
    try {
      if(bodyLen > 0){
        tgtOutputStream = conn.getOutputStream();
        __readRequestStream(bodyLen, srcInputStream, tgtOutputStream);
        tgtOutputStream.flush();
        tgtOutputStream.close();
        tgtOutputStream = null;
      }
      
      //writeLog(LINE_SEP+"======response stream======="+LINE_SEP, false);
      int resCode = conn.getResponseCode();
      bodyLen = 0;
      boolean chunked = false;
      Map<String, List<String>> headerFields = conn.getHeaderFields();
      
      String dummyKey = md5.getMD5();
      //System.out.println("\n--->"+dummyKey);
      Integer dummyIndex = HttpProxy.getCallTimeAndAdd(dummyKey);
      DummyUtil.saveRespponseHeader(testMethod, dummyKey, dummyIndex, headerFields, md5.getDynamicUrl());
      
      __writeHeaders(srcOutputStream, headerFields);
      if(headerFields.containsKey("Content-Length")){
        bodyLen = Integer.parseInt(headerFields.get("Content-Length").get(0));
      }else if(headerFields.containsKey("Transfer-Encoding") && 
          headerFields.get("Transfer-Encoding").get(0).equals("chunked")){
        chunked = true;
      }      
      
      tgtInputStream = (resCode == 200)?conn.getInputStream():conn.getErrorStream();
      if(tgtInputStream != null){
        OutputStream dummyOutputStream = DummyUtil.saveContentWithOutputStream(testMethod, dummyKey, dummyIndex, md5.getDynamicUrl());
        __writeResponseStream(tgtInputStream, srcOutputStream, dummyOutputStream, chunked, bodyLen);
        dummyOutputStream.close();
      }
    } catch (Exception e0) {
      e0.printStackTrace();
      writeLog("Pipe异常: " + e0.getMessage(), true);
    } finally{
      try {
        if(tgtOutputStream!=null) tgtOutputStream.close();
      } catch (Exception e2) {
      }
      try {
        if(tgtInputStream!=null) tgtInputStream.close();
      } catch(Exception e2) {
      }
    }
  }
  
  private Map<String,String> __readHeaders(InputStream srcInputStream) throws IOException{
    //read header from srcInputStream
    writeLog("======request stream headers======="+LINE_SEP, false);
    Map<String, String> headers = new HashMap<String,String>();
    String line = "";
    int lineBreakState = 0; //1: \\r, 2:\n
    while(true){
      int c = srcInputStream.read();
      if (c == -1) break;
      writeLog(c, false);
      if(c == '\r' || c=='\n'){
        if(lineBreakState == 0){
          int pos = line.indexOf(":");
          if(pos > 0){
            String key = line.substring(0, pos).trim();
            headers.put(key, line.substring(pos+1).trim());
          }else{
            //headers.put(null, line);
          }
          line = "";
        }else if((c=='\r' && lineBreakState > 0) || (c=='\n' && lineBreakState == 2)){
          //header ended
          if(c=='\r' && lineBreakState==2){
            //read /n
            srcInputStream.read();
          }
          break;
        }
        lineBreakState = (c=='\r')?1:2;
      }else{ //not /r /n
        lineBreakState = 0;
        line += (char)c;
      }
    }
    return headers;
  }
  
  private void __readRequestStream(int bodyLen, InputStream srcInputStream, OutputStream tgtOutputStream) throws IOException{
    writeLog("======request stream body======="+LINE_SEP, false);
    int count = -1;
    byte data[] = new byte[BUFSIZ];
    while(bodyLen > 0 && (count = srcInputStream.read(data,0,bodyLen<BUFSIZ?bodyLen:BUFSIZ)) != -1){
      writeLog(data, 0, count, false);
      if(tgtOutputStream != null){
        tgtOutputStream.write(data, 0, count);
      }
      md5.write(data, 0, count);
      bodyLen -= count;
    }
  }
  
  private void __writeHeaders(OutputStream srcOutputStream, Map<String, List<String>> headerFields) throws IOException{
    for(Entry<String, List<String>> entry : headerFields.entrySet()){
      for(String value : entry.getValue()){
        byte[] header = null;
        if(entry.getKey() == null){
          header = (value+LINE_SEP).getBytes();
        } else{
          String key = entry.getKey();
          header = String.format("%s: %s"+LINE_SEP, key, value).getBytes();
        }
        //writeLog(header, 0, header.length, false);
        srcOutputStream.write(header, 0, header.length);
      }
    }
    writeLog(LINE_SEP, false);
    srcOutputStream.write(LINE_SEP.getBytes(), 0, LINE_SEP.length());
  }
  
  private void __writeResponseStreamFromDummy(InputStream tgtInputStream, OutputStream srcOutputStream) throws IOException {
    int count;
    byte[] data = new byte[BUFSIZ];
    while((count = tgtInputStream.read(data,0,BUFSIZ)) != -1){
        writeLog(data, 0, count, false);
        srcOutputStream.write(data, 0, count);
    }
  }
  
  private void __writeResponseStream(InputStream tgtInputStream, OutputStream srcOutputStream, OutputStream dummyOutputStream, boolean chunked, int bodyLen) throws IOException{
    int count = -1;
    byte data[] = new byte[BUFSIZ];
    if(chunked){
      while(true){
        bodyLen = tgtInputStream.available();
        if(bodyLen == 0){
          byte[] tmp = String.format("%x"+LINE_SEP+LINE_SEP, 0).getBytes();
          writeLog(tmp, 0, tmp.length, false);
          srcOutputStream.write(tmp, 0, tmp.length);
          dummyOutputStream.write(tmp, 0, tmp.length);
          break;
        }
        byte[] tmp = String.format("%x"+LINE_SEP, bodyLen).getBytes();
        writeLog(tmp, 0, tmp.length, false);
        srcOutputStream.write(tmp, 0, tmp.length);
        dummyOutputStream.write(tmp, 0, tmp.length);
        while(bodyLen > 0 && (count = tgtInputStream.read(data,0,bodyLen<BUFSIZ?bodyLen:BUFSIZ)) != -1){
          writeLog(data, 0, count, false);
          srcOutputStream.write(data, 0, count);
          dummyOutputStream.write(data, 0, count);
          bodyLen -= count;
        }
        tmp = LINE_SEP.getBytes();
        writeLog(tmp, 0, tmp.length, false);
        srcOutputStream.write(tmp, 0, tmp.length);
        dummyOutputStream.write(tmp, 0, tmp.length);
      }
    }else{
      while(bodyLen > 0 && (count = tgtInputStream.read(data,0,bodyLen<BUFSIZ?bodyLen:BUFSIZ)) != -1){
        writeLog(data, 0, count, false);
        srcOutputStream.write(data, 0, count);
        dummyOutputStream.write(data, 0, count);
        bodyLen -= count;
      }
    }
  }
  
  /**
   * when url is command (start with /_cmd/), return true;
   * @param url
   * @param host
   * @param port
   * @param sock
   * @return
   * @throws IOException
   */
  private boolean setProxyServer(String url, String host, int port, Socket sock) throws IOException {
    if(url.startsWith("/_cmd/")){
      if(url.startsWith("/_cmd/cache/")){
        writeLog(LINE_SEP+"*********cache: "+ url + LINE_SEP, true);
        //url: /_cmd/cache/<key>?<cache string>
        int index = url.indexOf('?');
        String key = url.substring(0, index);
        String value = url.substring(index+1);
        index = getCallTimeAndAdd(key);
        
        String result = DummyUtil.getCache(testMethod, key, index, value, isDummy);
        
        OutputStream srcOutputStream = socket.getOutputStream();
        String msg = "HTTP/1.1 200 OK"+LINE_SEP;
        msg += "Content-Length: " + result.length() + LINE_SEP+LINE_SEP + result;
        byte[] bytes = msg.getBytes();
        srcOutputStream.write(bytes,0,bytes.length);
        srcOutputStream.close();
        
      }else{
        writeLog(LINE_SEP+"*********Get testMethod: "+url + LINE_SEP, true);
        testMethod = url;
        
        //clear exists cache
        if(!isDummy){
          DummyUtil.clearExistMethodCacheData(testMethod);
        }
        
        OutputStream srcOutputStream = socket.getOutputStream();
        byte[] msg = ("HTTP/1.1 204 No content"+LINE_SEP+LINE_SEP).getBytes();
        srcOutputStream.write(msg,0,msg.length);
        srcOutputStream.close();
      }
      return true;
    }
    parentPort = 80;
    parent = getHostFromUrl(url);
    return false;
  }
  
  private static String[] ciaUrls = {"/oauth/","/api/","/internal_api/","/api/","/special_api/"};
  private static String[] ossUrls = {"/folder/","/token/","/convert","/info/","/point/","/ufile/","/bucket/","/image/"};
  
  private static Map<String, String> hostCaches = new HashMap<String, String>();
  private String getHostFromUrl(String url){
    if(hostCaches.containsKey(url)){
      return hostCaches.get(url);
    }
    for(int i=0;i<ciaUrls.length;i++){
      if(url.startsWith(ciaUrls[i])){
        hostCaches.put(url, "cia.chanapp.chanjet.com");
        return "cia.chanapp.chanjet.com";
      }
    }
    for(int i=0;i<ossUrls.length;i++){
      if(url.startsWith(ossUrls[i])){
        hostCaches.put(url, "oss.chanapp.chanjet.com");
        return "oss.chanapp.chanjet.com";
      }
    }
    hostCaches.put(url, "sto.chanapp.chanjet.com");
    return "sto.chanapp.chanjet.com";
  }
  
  static public void startProxy(int port) {
    ServerSocket ssock;
    try {
      ssock = new ServerSocket(port);
      Socket s = null;
      while (true) {
        try {
          s = ssock.accept();
          new HttpProxy(s); // 创建HttpProxy
        } catch (Exception e) {
          if(s != null){    
            try {
              s.close();
            } catch (Exception ec) {
            }
          }
        }
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  static public void main(String args[]) throws IOException {
    
    if(args.length < 1){
      System.out.println("Must call with arguments: ");
      System.out.println("    -proxy|-dummy: run as proxy or run as dummy");
      System.out.println("    -log:  (optional) output debug info");
      return;
    }
    boolean runAsDummy = false;
    boolean log = false;
    for(String arg : args){
      if("-dummy".equals(arg)){
        runAsDummy = true;
      }else if("-proxy".equals(arg)){
        runAsDummy = false;
      }else if("-log".equals(arg)){
        log = true;
      }
    }
    
    int port = 10808;
    System.out.println("Start proxy at port "+port+(runAsDummy?" as dummy":"")+LINE_SEP);
    HttpProxy.log = System.out;
    HttpProxy.logging = log;
    HttpProxy.setDummy(runAsDummy);
    DummyUtil.setDummy(runAsDummy);
    HttpProxy.startProxy(port);
  }
}
