package util;

import org.htmlparser.*;
import org.htmlparser.util.*;
import org.htmlparser.tags.*;
import javax.mail.*;
import java.util.*;
import org.apache.commons.lang.*;

import ssh.spring.DeployInfoUtil;

import javax.mail.*;

import java.io.*;

public class ChgPathLinkService  {
    Parser parser;
    String href;
    List recilist;
    String subject;
    String charset;
    public ChgPathLinkService(String location,String href,List recilist, String subject,String charset) {
        if ((location == null) || (location.trim().length() == 0)) {
            throw new IllegalArgumentException(
                "Argumeng location can not be null or empty string.");
        }

        if ((href == null) || (href.trim().length() == 0)) {
            throw new IllegalArgumentException(
                "Argumeng href can not be null or empty string.");
        }

        if ((recilist == null) || (recilist.size() == 0)) {
            throw new IllegalArgumentException(
                "Argumeng recilist can not be null or empty list");
        }

        if ((subject == null) || (subject.trim().length() == 0)) {
            throw new IllegalArgumentException(
                "Argumeng subject can not be null or empty string.");
        }

        if ((charset == null) || (charset.trim().length() == 0)) {
            throw new IllegalArgumentException(
                "Argumeng charset can not be null or empty string.");
        }


        this.href=href;
        this.recilist=recilist;
        this.subject=subject;
        this.charset=charset;
        try {
            parser = new Parser(location,
                     new DefaultParserFeedback(DefaultParserFeedback.QUIET+1));
            parser.registerScanners();
        } catch (ParserException ex) {
            ex.printStackTrace();
        }
    }

    public void process() throws ParserException ,SendFailedException{
        Node node;
        String htmlStr="";

        StringBuffer sb=new StringBuffer();

        LinkTag linkTag;
        ImageTag imgTag;
        NodeIterator i = parser.elements();
        while (i.hasMoreNodes()) {
            node = i.nextNode();
            htmlStr = node.toHtml();
            htmlStr = StringUtils.replace(htmlStr, "css/", href+"/css/");
            htmlStr = StringUtils.replace(htmlStr, "images/", href+"/images/");
          //  System.out.println(htmlStr);
            sb.append(htmlStr);
        }


        for (int j = 0; j < recilist.size(); j++) {
        List rlist=new ArrayList();
        rlist.add(recilist.get(j));

        //准备寄信
        String Subject =subject;
        // from
        String from = "JMR";
        String host=  DeployInfoUtil.getEmailHost() ;

    // session
        Session session = MailTool.buildSession(host);
        // html with images
        HtmlMultiPart aHMP= new HtmlMultiPart();

        //內容
       aHMP.setContent(sb.toString());

       try{
          //MailTool.mailSend(session,rlist,null,null,from,Subject,aHMP.getMultipart() ,null,null,charset);
          MailTool.mailSend(session,rlist,null,null,from,Subject,sb ,null,null,charset);
      } catch (UnsupportedEncodingException uee) {
          uee.printStackTrace();

      } catch (SendFailedException sfe) {
          sfe.printStackTrace();
/*
          System.out.println(sfe.getMessage());
          Address[] x = sfe.getInvalidAddresses();
          if (x.length != 0) {
              for (int n = 0; n < x.length; n++) {
                  String testmail = x[n].toString();
                  testmail = testmail.substring(testmail.indexOf("<") +
                             1, testmail.indexOf(">"));
                  for (int q = 0; q < recilist.size(); q++) {
                      EPrecipient isfail = (EPrecipient) recilist.get(q);
                      if (isfail.getEmail().equals(testmail)) recilist.
                          remove(q);
                  }
              }
              try {
                  MailTool.mailSend(session, recilist, null, recilist,
                      from, subject, aHMP.getMultipart(), null, null,
                      charset);
              } catch (UnsupportedEncodingException uee2) {
                  uee2.printStackTrace();
              } catch (SendFailedException sfe2) {
                  sfe2.printStackTrace();
              } catch (MessagingException mse2) {
                  mse2.printStackTrace();
              }

          }

*/
      } catch (MessagingException mse) {
          mse.printStackTrace();
      }


        }





    }

}





//

