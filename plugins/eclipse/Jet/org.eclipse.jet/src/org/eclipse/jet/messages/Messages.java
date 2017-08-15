package org.eclipse.jet.messages;

import org.eclipse.osgi.util.NLS;
import org.jmr.lang.a.a;
import org.jmr.lang.a.b;

public class Messages extends NLS implements a{
  private static String BUNDLE_NAME_ENGLISH = "org.eclipse.jet.messages.messages_en"; //$NON-NLS-1$
  private static String BUNDLE_NAME_CHINESE = "org.eclipse.jet.messages.messages_zh"; //$NON-NLS-1$

  private static Messages messages;

  public static Messages getDefault(){
    if(messages == null){
      messages = new Messages();
    }
    return messages;
  }

  static {
    b.a(getDefault());
    // initialize resource bundle
    int language = b.d();
    if(a.CHINESE == language){
      NLS.initializeMessages(BUNDLE_NAME_CHINESE, Messages.class);
    }else{
      NLS.initializeMessages(BUNDLE_NAME_ENGLISH, Messages.class);
    }
  }

  public static String XPathContextExtender_Xpath_Error;
  public static String XPathContextExtender_Xpath_Advice;
 
  public void changeLanguage(int language) {
    if(a.CHINESE == language){
      NLS.initializeMessages(BUNDLE_NAME_CHINESE, Messages.class);
    }else{
      NLS.initializeMessages(BUNDLE_NAME_ENGLISH, Messages.class);
    }
  }

}
