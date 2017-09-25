package com.chanjet.test.httpproxy;

import java.io.IOException;
import java.io.OutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Tool extends OutputStream{
  private MessageDigest digest;
  private String dynamicUrl = null;
  private boolean updated = false;
  
  private Md5Tool(){
    try {
      digest = MessageDigest.getInstance("MD5");
    } catch (NoSuchAlgorithmException e) {
    }
  }
  
  public String getDynamicUrl() {
    return dynamicUrl;
  }

  public void setDynamicUrl(String dynamicUrl) {
    this.dynamicUrl = dynamicUrl;
  }

  public static Md5Tool getInstance(){
    return new Md5Tool();
  }
  
  public Md5Tool update(String input){
    this.updated = true;
    this.digest.update(input.getBytes());
    return this;
  }
  
  public Md5Tool update(byte[] input, int offset, int len){
    this.updated = true;
    this.digest.update(input, offset, len);
    return this;
  }

  public String getMD5() {
    if(!this.updated){
      return "downloadfile";
    }
      
    return byte2hex(digest.digest());
  }
  
  private String byte2hex(byte[] b) {
    String hs = "";
    String stmp = "";
    for (int n = 0; n < b.length; n++) {
      stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
      if (stmp.length() == 1) hs = hs + "0" + stmp;
      else hs = hs + stmp;
    }
    return hs.toLowerCase();
  }

  @Override
  public void write(int b) throws IOException {
    this.updated = true;
    this.digest.update((byte) b);
  }
  static public void main(String args[]) throws IOException {
     Md5Tool t = Md5Tool.getInstance();
     String s  = "POST /special_api/v1/orgUser/regEnterUserWithUsernameWithOutActive";
     t.update(s);
     t.update("appSecret=jjy3uh&entUserInfo=%7B%22orgFullName%22%3A%22csp_qa_test1464177742758%22%2C%22originCode%22%3A%22csp%22%2C%22password%22%3A%2296e79218965eb72c92a549dd5a330112%22%2C%22passwordLevel%22%3A%221%22%2C%22username%22%3A%22csp_qa_test1464177742758%22%7D&appKey=8f442ec3-713e-4133-b64c-5d2d828c5426");
     System.out.println(t.getMD5());
  }
}
