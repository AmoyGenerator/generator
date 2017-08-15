package org.eclipse.jet.exception;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class JmrException extends Exception{

	private static final long serialVersionUID = 1L;

	private ErrorCode errorCode;

	private WarnCode warnCode;

	private String jetLocation;

	private boolean printStackTrace;

	private int onlyPrintLine;

	public JmrException(ErrorCode errorCode) {
		super();
		this.errorCode = errorCode;
	}

	public JmrException(ErrorCode errorCode, String jetLocation) {
		super();
		this.errorCode = errorCode;
		this.jetLocation = jetLocation;
	}

	public JmrException(String message, Throwable cause, boolean printStackTrace, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
		this.printStackTrace = printStackTrace;
	}

	public JmrException(String message, Throwable cause, boolean printStackTrace, ErrorCode errorCode, String jetLocation) {
		super(message, cause);
		this.errorCode = errorCode;
		this.jetLocation = jetLocation;
		this.printStackTrace = printStackTrace;
	}

	public JmrException(String message, Throwable cause, boolean printStackTrace, int onlyPrintLine, ErrorCode errorCode) {
		super(message, cause);
		this.errorCode = errorCode;
		this.printStackTrace = printStackTrace;
		this.onlyPrintLine = onlyPrintLine;
	}

	public JmrException(String message, Throwable cause, boolean printStackTrace, int onlyPrintLine, ErrorCode errorCode, String jetLocation) {
		super(message, cause);
		this.errorCode = errorCode;
		this.jetLocation = jetLocation;
		this.printStackTrace = printStackTrace;
		this.onlyPrintLine = onlyPrintLine;
	}

	public JmrException(String message, ErrorCode errorCode) {
		super(message);
		this.errorCode = errorCode;
	}

	public JmrException(String message, ErrorCode errorCode, String jetLocation) {
		super(message);
		this.errorCode = errorCode;
		this.jetLocation = jetLocation;
	}

	public JmrException(Throwable cause, boolean printStackTrace, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
		this.printStackTrace = printStackTrace;
	}

	public JmrException(Throwable cause, boolean printStackTrace, ErrorCode errorCode, String jetLocation) {
		super(cause);
		this.errorCode = errorCode;
		this.jetLocation = jetLocation;
		this.printStackTrace = printStackTrace;
	}
	
	public JmrException(Throwable cause, boolean printStackTrace, int onlyPrintLine, ErrorCode errorCode) {
		super(cause);
		this.errorCode = errorCode;
		this.printStackTrace = printStackTrace;
		this.onlyPrintLine = onlyPrintLine;
	}

	public JmrException(Throwable cause, boolean printStackTrace, int onlyPrintLine, ErrorCode errorCode, String jetLocation) {
		super(cause);
		this.errorCode = errorCode;
		this.jetLocation = jetLocation;
		this.printStackTrace = printStackTrace;
		this.onlyPrintLine = onlyPrintLine;
	}

	private Map<String, Object> properties = new LinkedHashMap<String, Object>();

	public WarnCode getWarnCode() {
		return warnCode;
	}

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public Object get(String key){
		return properties.get(key);
	}

	public JmrException set(String key, Object value){
		properties.put(key, value);
		return this;
	}

	public String getJetLocation() {
		return jetLocation;
	}

	public void setJetLocation(String jetLocation) {
		this.jetLocation = jetLocation;
	}

	private static final String NL = System.getProperty("line.separator");
	private static final String DOUBLE_SPACE = "  ";
	private static final String CAUSE = "[Cause]: ";
	private static final String PROPERTY = "[property]: ";
	private static final String TEMPLATE = "[Template]: ";
	private static final String ADVICE = "[Advice]: ";
	
	public String print(){
		StringBuffer message = new StringBuffer();
		ErrorCode errorCode = getErrorCode();
		int number = errorCode.getNumber();
		message.append(NL);
		String jetLocation = getJetLocation();
		if(jetLocation != null){
			message.append(TEMPLATE + jetLocation);
		}
		message.append(NL);
		message.append(CAUSE + CodeMappingMessage.get(number) + NL);

		Map<String, Object> properties = getProperties();
		Set<String> keySet = properties.keySet();

		Iterator<String> keySetIterator = keySet.iterator();
		while (keySetIterator.hasNext()) {
			String key = (String) keySetIterator.next();
			String value = (String)(properties.get(key));
			message.append(PROPERTY + key + NL);
			if(value != null && !value.trim().isEmpty()){
				message.append(ADVICE + NL + (properties.get(key)).toString() + NL);
			}
		}

		Throwable cause = getCause();
		if(cause != null){
			message.append("[PrintStackTrace]: " + cause.toString() + NL);
			StackTraceElement[] trace = cause.getStackTrace();
			if(trace.length > 0){
				if(printStackTrace){
					if(onlyPrintLine > 0){
						message.append(DOUBLE_SPACE + DOUBLE_SPACE + "at " + trace[onlyPrintLine - 1] + NL);
					}else{
						for (int i = 0; i < trace.length; i++)
						{
							message.append(DOUBLE_SPACE + DOUBLE_SPACE + "at " + trace[i] + NL);
						}
					}
				}
			}
		}
		return message.toString();
	}

}
