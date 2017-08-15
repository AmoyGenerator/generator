package util;

import java.util.*;

import ssh.spring.DeployInfoUtil;

public class MailInfo {
 
// 邮件类型 text /html
 public static String CONTENTTYPE_TEXT="text";
 public static String CONTENTTYPE_HTML="html";
 
 public static String CONTEENTYPE_HTML_CONTENT = "text/html;charset=big5";
 
 private String subject; //标题
 private String text; //正文
 private String from = DeployInfoUtil.getEmailFrom();//发送邮箱
 private String to; //接收邮箱
 private String[] toArray; //群发的邮件数组
 private String[] toBccArray; //密件抄送群发邮件数组
 private String encode = "Big5"; //字符编码 ， 预设是Big5
 private String contentType = CONTENTTYPE_TEXT; //邮件类型 text /html
 private List file = new ArrayList();//储存附件路径
 
 public MailInfo(){
  //
 }



    /**
     * 一封邮件
     * @param subject
     * @param text
     * @param to
     */
 public MailInfo(String subject, String text, String to) {
  super();
  this.subject = subject;
  this.text = text;
  this.to = to;
 }
 
 /**
  * 蜜饯群组发送
  * @param subject
  * @param text
  * @param from
  * @param toArray
  */ 
 public MailInfo(String subject, String text,  String[] toBccArray) {
  super();
  this.subject = subject;
  this.text = text;
  this.toBccArray = toBccArray;
 }

 public String getEncode() {
  return encode;
 }
 public void setEncode(String encode) {
  this.encode = encode;
 }
 public String getSubject() {
  return subject;
 }
 public void setSubject(String subject) {
  this.subject = subject;
 }
 public String getText() {
  return text;
 }
 public void setText(String text) {
  this.text = text;
 }
 public String getTo() {
  return to;
 }
 public void setTo(String to) {
  this.to = to;
 }
 public String[] getToArray() {
  return toArray;
 }
 public void setToArray(String[] toArray) {
  this.toArray = toArray;
 }
 public String[] getToBccArray() {
  return toBccArray;
 }
 public void setToBccArray(String[] toBccArray) {
  this.toBccArray = toBccArray;
 }

 


 public String getFrom() {
  return from;
 }

 


 public void setFrom(String from) {
  this.from = from;
 }

 public List getFile() {
  return file;
 }

 public void setFile(List file) {
  this.file = file;
 }

 public String getContentType() {
  return contentType;
 }

 public void setContentType(String contentType) {
  this.contentType = contentType;
 }
 
 

}

