package org.eclipse.jet.model;

import java.io.Serializable;

/**
 * 参数属性
 * @author xyu
 * 
 */
public class Attr implements Serializable{
	/**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String key;
	private String value;
	private boolean isNew;
	
	public Attr(String key, String value, boolean isNew) {
		this.key = key;
		this.value = value;
		this.isNew = isNew;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public boolean isNew() {
		return isNew;
	}

	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}
	
}
