package util;

import java.util.*;
import java.io.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.activation.*;

public class HtmlMultiPart {

    private BodyPart messageBodyPart = null;
    private MimeMultipart multipart = null;
    private Vector imageBodyPart = null;

    public HtmlMultiPart() {
        multipart = new MimeMultipart("related");
        imageBodyPart = new Vector();
    }

    /**
     *
     * @param sHtmlCode String html code
     */
    public void setContent(String sHtmlCode) {
        try {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sHtmlCode, "text/html;charset=utf-8");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        ;
    }

    public void setContent(String sHtmlCode, String charset) {
        try {
            messageBodyPart = new MimeBodyPart();
            messageBodyPart.setContent(sHtmlCode, "text/html;charset=" + charset);
        } catch (Exception ex) {ex.printStackTrace();
        }
        ;
    }

    /**
     *
     * @param sImgFileLocation String image file location
     * @param sContentID String ContentID(must match with sHtmlCode from setConetnt(sHtmlCode)
     */
    public void buildContentImage(String sImgFileLocation, String sContentID) {
        try {
            DataSource fds = new FileDataSource(sImgFileLocation);
            MimeBodyPart aBP = new MimeBodyPart();
            aBP.setDataHandler(new DataHandler(fds));
            aBP.setHeader("Content-ID", sContentID);
            imageBodyPart.add(aBP);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @return
     */
    public Multipart getMultipart() {
        if (messageBodyPart == null) {
            return null;
        }
        try {
            multipart.addBodyPart(messageBodyPart);
            for (int i = 0; i < imageBodyPart.size(); i++) {
                multipart.addBodyPart((MimeBodyPart) imageBodyPart.get(i));
            }
            return multipart;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }
}
