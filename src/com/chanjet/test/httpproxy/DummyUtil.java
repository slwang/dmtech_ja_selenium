package com.chanjet.test.httpproxy;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FileUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

public class DummyUtil {
  private static  Map<String,String> urlMaps = new HashMap<String,String>();
  
  public static class ContentOutputStream extends OutputStream{
    private Md5Tool md5Tool;
    private File tmpFile;
    private OutputStream os;
    private String testMethod;
    private String key;
    private Integer index;
    private String dynamicUrl;
    
    public ContentOutputStream(String testMethod, String key, Integer index, String dynamicUrl) throws IOException{
      tmpFile = File.createTempFile("cache", "tmp");
      os = FileUtils.openOutputStream(tmpFile);
      md5Tool = Md5Tool.getInstance();
      this.testMethod = testMethod;
      this.key = key;
      this.index = index;
      this.dynamicUrl = dynamicUrl;
    }

    @Override
    public void write(int b) throws IOException {
      os.write(b);
      byte[] bytes = new byte[1];
      bytes[0] = (byte)b;
      md5Tool.update(bytes,0,1);
    }

    @Override
    public void close() throws IOException {
      super.close();
      os.flush();
      os.close();
      saveFile(testMethod, key, index, "content", md5Tool.getMD5(), tmpFile, dynamicUrl);
      tmpFile.delete();
    }
  }
  
  private static boolean isDummy; //work as dummy if true. otherwise work as save data
  private static JSONObject cache; //{testMethod: {key: {index: {"header": "header md5", "content": "content mdt}}}}
  
  private static final File cacheDir = new File(System.getProperty("user.dir") + "/cache/");
  private static final File MAIN_FILE = FileUtils.getFile(cacheDir, "main.json"); 

  /**
   * set dummy status and initial
   * @param dummy
   * @throws IOException 
   */
  public static void setDummy(boolean dummy) throws IOException{
    isDummy = dummy;
    if(isDummy){
      if(!MAIN_FILE.exists()){
          throw new RuntimeException("main file not found at " + MAIN_FILE.getAbsolutePath());
      }
      String content = FileUtils.readFileToString(MAIN_FILE);
      cache = JSON.parseObject(content);
    }else{
      if(MAIN_FILE.exists()){
        String content = FileUtils.readFileToString(MAIN_FILE);
        cache = JSON.parseObject(content);
      }else{
        cache = new JSONObject();
        FileUtils.forceMkdir(cacheDir);
      }
    }
  }
  
  public static Map<String, List<String>> getResponseHeader(String testMethod, String key, Integer index, String dynamicUrl) {
    try{
      File file = getMd5FileName(testMethod, key, index, "head", dynamicUrl);
      String header = FileUtils.readFileToString(file);
      Map<String, List<String>> result = new HashMap<String, List<String>>();
      JSONObject jsonObj = JSON.parseObject(header);
      for(String h : jsonObj.keySet()){
        List<String> list = new ArrayList<String>();
        result.put(h, list);
        JSONArray jsarr = jsonObj.getJSONArray(h);
        for(int i=0;i<jsarr.size();i++){
          list.add(jsarr.getString(i));
        }
      }
      return result;
    }catch(Exception e){
      e.printStackTrace();
      throw new RuntimeException("cache head file not found");
    }
  }

  public static InputStream getResponseContent(String testMethod, String key, Integer index, String dynamicUrl) {
    try{
      File file = getMd5FileName(testMethod, key, index, "content", dynamicUrl);
      return FileUtils.openInputStream(file);
    }catch(Exception e){
      e.printStackTrace();
      throw new RuntimeException("cache content file not found");
    }
  }

  public static void saveRespponseHeader(String testMethod, String key, Integer index, Map<String, List<String>> header, String dynamicUrl) throws IOException {
    byte[] content = JSON.toJSONString(header).getBytes();
    File tmpFile = File.createTempFile("cache", "tmp");
    FileOutputStream os = null;
    String md5 = null;
    try{
      os = FileUtils.openOutputStream(tmpFile);
      Md5Tool md5Tool = Md5Tool.getInstance();
      md5Tool.update(content, 0, content.length);
      os.write(content, 0, content.length);
      os.flush();
      os.close();
      md5 = md5Tool.getMD5();
      os = null;
    }finally{
      if(os != null){
        os.close();
      }
    }
    saveFile(testMethod, key, index, "head", md5, tmpFile, dynamicUrl);
    tmpFile.deleteOnExit();
  }

  public static OutputStream saveContentWithOutputStream(String testMethod, String key, Integer index, String dynamicUrl) throws IOException {
    return new ContentOutputStream(testMethod, key, index, dynamicUrl);
  }
  
  private static void saveFile(String testMethod, String key, Integer index, String type, String md5, File tmpFile, String dynamicUrl) throws IOException{
    System.out.println(String.format("\n Get %s %s key=%s, index=%d, dynamicUrl=%s", type, testMethod, key, index, dynamicUrl));
    //System.out.println(String.format("%s file name : %s.%s ", type, md5, type));
    
    File file = FileUtils.getFile(cacheDir, md5+"."+type);
    if(!file.exists()){
      FileUtils.copyFile(tmpFile, file);
    }
    
    if(!cache.containsKey(testMethod))
    {
      cache.put(testMethod, new JSONObject());
    }
    JSONObject cacheMethod = cache.getJSONObject(testMethod);
    if(!cacheMethod.containsKey(key)){
      cacheMethod.put(key, new JSONObject());
    }
    JSONObject obj = cacheMethod.getJSONObject(key);
    String idx = (dynamicUrl!=null && key.equals("downloadfile"))?dynamicUrl:index.toString();
    if(!obj.containsKey(idx)){
      obj.put(idx, new JSONObject());
    }
    obj = obj.getJSONObject(idx);
    obj.put(type, md5);
    if(dynamicUrl != null){
      if(!obj.containsKey("url")){
        obj.put("url", new JSONArray());
      }
      if(!obj.getJSONArray("url").contains(dynamicUrl)){
        obj.getJSONArray("url").add(dynamicUrl);
      }
    }
    
    //save cache to MAIL_FILE
    saveCacheToFile();
  }

  private static void saveCacheToFile() throws IOException {
    FileOutputStream os = FileUtils.openOutputStream(MAIN_FILE);
    try{
      byte[] content = JSON.toJSONString(cache, true).getBytes();
      os.write(content, 0, content.length);
      os.flush();
      os.close();
      os = null;
    }finally{
      if(os!=null){
        os.close();
      }
    }
  }
  
  private static File getMd5FileName(String testMethod, String key, Integer index, String type, String dynamicUrl){
    //System.out.println(String.format("\n Get %s %s key=%s, index=%d, dynamicUrl=%s", type, testMethod, key, index, dynamicUrl));
    
    JSONObject obj = cache.getJSONObject(testMethod);
    if(obj == null){
      String msg = String.format("Error testMethod=%s of %s not found!", testMethod, type);
      System.out.println(msg);
      throw new RuntimeException(msg);
    }
    
    obj = obj.getJSONObject(key);
    if(obj == null){
      String msg = String.format("Error key=%s of %s not found!", key, type);
      System.out.println(msg);
      throw new RuntimeException(msg);
    }
    
    String indexKey = index.toString();
    if(dynamicUrl != null && key.equals("downloadfile")){
         //System.out.println(JSON.toJSONString(urlMaps, true));
         indexKey = urlMaps.get(dynamicUrl);
    }
    JSONObject obj2 = obj.getJSONObject(indexKey);
    if((dynamicUrl == null || !key.equals("downloadfile")) && obj2 == null){
      //cycle get...
      int max = 0;
      for(String idx : obj.keySet()){
        if(Integer.valueOf(idx) > max){
          max = Integer.valueOf(idx);
        }
      }
      index = (max<2)?1:(index % max);
      if(index == 0) index = max;
      System.out.println("-->change index to " + index);
      obj2 = obj.getJSONObject(index.toString());
    }
    if(obj2 == null){
      String msg = String.format("Error testMethod=%s, key=%s, index=%d, dynamicUrl=%s of %s not found!", testMethod, key, index, dynamicUrl, type);
      System.out.println(msg);
      throw new RuntimeException(msg);
    }
    
    if(dynamicUrl != null && !key.equals("downloadfile")){
        //上传, 将 dynamicUrl 作为key, 保存映射到原来的url
        urlMaps.put(dynamicUrl, obj2.getJSONArray("url").getString(0));
        if(!dynamicUrl.endsWith(".crop.jpg")){
          urlMaps.put(dynamicUrl+".crop.jpg", obj2.getJSONArray("url").getString(0)+".crop.jpg");
        }
    }
    
    String md5 = obj2.getString(type);
    //System.out.println(String.format("%s file name : %s.%s ", type, md5, type));
    return FileUtils.getFile(cacheDir,  md5+"."+type);
  }

  public static String getCache(String testMethod, String key, Integer index, String value, boolean isDummy) throws IOException {
    JSONObject obj = cache.getJSONObject("CacheData");
    if(obj == null){
      obj = new JSONObject();
      cache.put("CacheData", obj);
    }
    key = testMethod+":"+key;
    JSONObject obj2 = obj.getJSONObject(key);
    if(obj2 == null){
      obj2 = new JSONObject();
      obj.put(key, obj2);
    }
    if(isDummy){
      String v = obj2.getString(index.toString());
      if(v == null){
        v = value;
      }
      return v;
    }else{
      obj2.put(index.toString(), value);
      
      saveCacheToFile();
      return value;
    }
  }

  public static void clearExistMethodCacheData(String testMethod) throws IOException {
    //1. clear cahce data of testMethod
    // cache {testMethod:{key: {index: {"header": "header md5", "content": "content mdt}}}}
    if(cache.containsKey(testMethod)){
      //get files
      List<String> methodFiles = getFileNamesOfMethod(testMethod);
      //remove test method
      cache.remove(testMethod);
      //get all file in cache
      List<String> fileNames = new ArrayList<String>();
      for(String method : cache.keySet()){
        if("CacheData".equals(method)){
          continue;
        }
        fileNames.addAll(getFileNamesOfMethod(method));
      }
      //delete file in methodFiles but not in fileNames
      for(String fileName : methodFiles){
        if(fileNames.contains(fileName)){
          continue;
        }
        File file = FileUtils.getFile(cacheDir, fileName);
        if(file.exists()){
          file.delete();
        }
      }
    }
    
    //2. clear cache data key begin with testMehod:
    // cache {CacheData: {testMethod:xxx:{...}}
    JSONObject cacheData = cache.getJSONObject("CacheData");
    if(cacheData != null){
      String left = testMethod+":";
      for(String key : cacheData.keySet()){
        if(key != null && key.indexOf(left)==0){
          cacheData.remove(key);
        }
      }
    }
    //3.save cache to main.json
    saveCacheToFile();
  }
  
  private static List<String> getFileNamesOfMethod(String testMethod){
    List<String> fileNames = new ArrayList<String>();
    JSONObject methodObj = cache.getJSONObject(testMethod);
    if(methodObj != null){
      for(String key : methodObj.keySet()){
        JSONObject keyObj = methodObj.getJSONObject(key);
        if(keyObj != null){
          for(String index : keyObj.keySet()){
            JSONObject indexObj = keyObj.getJSONObject(index);
            if(indexObj != null){
              if(indexObj.containsKey("head")){
                fileNames.add(indexObj.getString("head")+".head");
              }
              if(indexObj.containsKey("content")){
                fileNames.add(indexObj.getString("content")+".content");
              }
            }
          }
        }
      }
    }
    return fileNames;
  }
}
