package util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.Iterator;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.apache.commons.logging.*;
import org.springframework.mail.*;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import ssh.spring.SpringContextUtil;


public class MailUtil {
 
 private static final Log log = LogFactory.getLog(MailUtil.class);   
 /**
  * 发送电子邮件（可单发，群发，密件发送，但不能加附件）
  * @param mail
  * @return
  */
 public static boolean sendSimpleMail(MailInfo mail){
  try {
   MailSender sender = getMailSender();
   SimpleMailMessage smm = getSMM(mail);
   sender.send(smm);
   log.info("发送Email成功");
   return true;
  } catch (MailException e) {
   e.printStackTrace();
   log.debug("发送Email失败");
   return false;
  }
 }
 
 /**
  * 
  * 发送复杂邮件（可单发，群发，密件发送，但不能加附件）
  * @param mail
  * @return
  */
 public static boolean sendMimeMessage(final MailInfo mail){
  try {
   JavaMailSender sender = (JavaMailSender)getMailSender();

   MimeMessagePreparator preparator = new MimeMessagePreparator(){

    public void prepare(MimeMessage msg) throws MessagingException {
    
     msg.setFrom(new InternetAddress(mail.getFrom()));
     if(mail.getTo()!= null && !mail.getTo().equals("")){
      msg.setRecipients(Message.RecipientType.TO, //单个地址
        InternetAddress.parse(mail.getTo()));
     }
     if(mail.getToArray()!= null && mail.getToArray().length>0){
      msg.setRecipients(Message.RecipientType.TO, //发送多个地址
        InternetAddress.parse(strArrayFromStr(mail.getToArray())));
     }
     if(mail.getToBccArray()!= null && mail.getToBccArray().length>0){
      msg.setRecipients(Message.RecipientType.BCC, //发送多个地址
        InternetAddress.parse(strArrayFromStr(mail.getToBccArray())));
     } 
     
     msg.setSubject(mail.getSubject());
     Multipart mp = new MimeMultipart();
     msg.setContent(mp);
     
     MimeBodyPart mbpContent = new MimeBodyPart();//增加文本內容
     if(mail.getContentType().equals(MailInfo.CONTENTTYPE_TEXT)){ //text格式邮件
      mbpContent.setText(mail.getText());
     }else{//html格式邮件
      mbpContent.setContent(mail.getText(), MailInfo.CONTEENTYPE_HTML_CONTENT);
     }     
     mp.addBodyPart(mbpContent);
     
     for (Iterator iter = mail.getFile().iterator(); iter.hasNext();) {
      String  filePath = (String ) iter.next();
      MimeBodyPart mbpFile = new MimeBodyPart();//增加附件
      FileDataSource fds = new FileDataSource(filePath);//路径
      mbpFile.setDataHandler(new DataHandler(fds));
      try {
       mbpFile.setFileName(MimeUtility.encodeText(fds.getName()));//编码处理
      } catch (UnsupportedEncodingException e) {
       e.printStackTrace();
      }
      mp.addBodyPart(mbpFile);
     }
     msg.setSentDate(new Date());
     
    }
   };
   
   sender.send(preparator);
   log.info("发送Email成功");
   return true;
  } catch (MailException e) {
   e.printStackTrace();
   log.debug("发送Email失败");
   return false;
  }
 }
 
 public static String strArrayFromStr(String val[]){
  String returnVal = "";
  for (int i = 0; i < val.length; i++) {
   returnVal = (i==0 ? val[i]:returnVal+","+ val[i]);
  }
  return returnVal;
 }
 
 /**
  * 取得邮件发送
  * @return
  */
 public static MailSender getMailSender(){
  MailSender mailSender = (MailSender)SpringContextUtil.getApplicationContext().getBean("mailSender");
  return mailSender;
 }
 
 /**
  * to(单发), toArray(群发), toBccArray(密件群发），只能有一个
  * @param mail
  * @return
  */
 public static SimpleMailMessage getSMM(MailInfo mail){
  SimpleMailMessage smm = new SimpleMailMessage();
  smm.setSubject(mail.getSubject());
  smm.setText(mail.getText());
  smm.setFrom(mail.getFrom());
  if(mail.getTo()!= null && !mail.getTo().equals("")){
   smm.setTo(mail.getTo());
  }
  if(mail.getToArray()!= null && mail.getToArray().length>0){
   smm.setTo(mail.getToArray());
  }
  if(mail.getToBccArray()!= null && mail.getToBccArray().length>0){
   smm.setBcc(mail.getToBccArray());
  }  
  return smm;
 }

}

