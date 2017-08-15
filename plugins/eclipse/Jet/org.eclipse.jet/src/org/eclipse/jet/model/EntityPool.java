package org.eclipse.jet.model;



public class EntityPool extends Entity{
	
  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  //定义一个对象池提取的KEY值
	private String key;
	
	//构造函数获得相同标志
	public EntityPool(String _key){
		this.key = _key;
	}
	
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
}
