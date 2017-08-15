package util;

import java.io.*;
import java.util.*;

import javax.mail.*;
import javax.mail.internet.MimeUtility;

import com.jmr.entity.Member;

import ssh.spring.DeployInfoUtil;
import tkb.util.mail.EasyMail;

public class MailTool {

    public static Session buildSession(String sMailServer) {
        if (sMailServer == null)return null;
        Properties props = System.getProperties();
        props.put("mail.smtp.host", sMailServer);
        Session session = Session.getDefaultInstance(props, null);
        session.setDebug(false);
        return session;
    }

    /**
     *
     * @param session mail session 可使用 MailTool.buildSession 建立
     * @param to vector 存放收件者的mail account
     * @param cc vector 存放副本
     * @param bcc vector 存放密件副本
     * @param from String 寄件者
     * @param subject String 主旨
     * @param bodytext Multipart 请参考HtmlMultiPart or TextMultiPart
     * @param username String 使用者名称(not yet implement)
     * @param password String 密码(not yet implemnet)
     * @return boolean true: send ok false: send fail
     */
    public static boolean mailSend(Session session, List to, Vector cc, List bcc,
        String from, String subject,
        StringBuffer sb, String username, String password,
        String charset) throws UnsupportedEncodingException, SendFailedException,
        MessagingException {
        if (session == null || to == null || from == null || subject == null || sb == null)return false;

        String sTo = null, sCC = null, sBCC = null;
        Map map =  new HashMap();

        if (cc != null)
            for (int i = 0; i < cc.size(); i++) {
                if (sCC == null)
                    sCC = (String) cc.get(i);
                else
                    sCC = sCC + "," + (String) cc.get(i);
            }

        session.setDebug(false); //open mail sesison debug function
        try {
            EasyMail easyMail = new EasyMail(DeployInfoUtil.getEmailHost());
//             easyMail.setFrom(Constant.MAIL_FROM, from,charset);
             easyMail.setFrom(DeployInfoUtil.getEmailFrom(), MimeUtility.encodeText(from, "utf-8", "B"));
            for (int i = 0; i < to.size(); i++) {
            	
            	Member member = (Member)to.get(i);
            	System.out.println(member.getEmail()+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
            	System.out.println(subject+">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>");
                map.put(member.getEmail(), member.getName());
            }
//            easyMail.setRecipient(Message.RecipientType.BCC, map,charset);
            easyMail.setRecipient(Message.RecipientType.BCC, map);
//            easyMail.setSubject(subject,charset);
            easyMail.setSubject(MimeUtility.encodeText(subject, "utf-8", "B"));
            easyMail.setMailContent(sb, "text/html; charset="+charset);
            easyMail.sendMail();
            return true;
        } catch (UnsupportedEncodingException uee) {
            uee.printStackTrace();
            throw new UnsupportedEncodingException(uee.getMessage());

        } catch (SendFailedException sfe) {
            sfe.printStackTrace();
            throw new SendFailedException(sfe.getMessage());

        } catch (MessagingException mex) {
            System.err.print("有错误" + mex.getMessage());
            throw new MessagingException(mex.getMessage());
        }
    }
}
