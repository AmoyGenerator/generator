package org.eclipse.jet.exception;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;


public class PrintJmrException {

  private static final String NL = System.getProperty("line.separator");
  private static final String DOUBLE_SPACE = "  ";
  private static final String CAUSE = "[Cause]:";
  private static final String PROPERTY = "[property]: ";
  private static final String TEMPLATE = "[Template]: ";

  public static String print(Exception e, boolean printStackTrace, boolean onlyFirstStackTrace){
    StringBuffer message = new StringBuffer();
    JmrException jmrException;
    if(e instanceof JmrException){
      jmrException = (JmrException) e;
      ErrorCode errorCode = jmrException.getErrorCode();
      int number = errorCode.getNumber();
      message.append(NL);
      message.append(CAUSE + CodeMappingMessage.get(number) + NL);

      Map<String, Object> properties = jmrException.getProperties();
      Set<String> keySet = properties.keySet();

      Iterator<String> keySetIterator = keySet.iterator();
      while (keySetIterator.hasNext()) {
        String key = (String) keySetIterator.next();
        message.append(DOUBLE_SPACE + PROPERTY + key + "(" + (properties.get(key)).toString() + ")" + NL);
      }

      String jetLocation = jmrException.getJetLocation();
      if(jetLocation != null){
        message.append(DOUBLE_SPACE + TEMPLATE + jetLocation + NL);
      }

      Throwable cause = e.getCause();
      if(cause != null){
        message.append("[PrintStackTrace]: " + cause.toString() + NL);
        StackTraceElement[] trace = cause.getStackTrace();
        if(trace.length > 0){
          if(printStackTrace){
            if(onlyFirstStackTrace){
              message.append(DOUBLE_SPACE + DOUBLE_SPACE + "at " + trace[0] + NL);
            }else{
              for (int i = 0; i < trace.length; i++)
              {
                message.append(DOUBLE_SPACE + DOUBLE_SPACE + "at " + trace[i] + NL);
              }
            }
          }
        }
      }
    }
    return message.toString();
  }

}
