package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;

public class ModelXPathMessage {
	
	/**元素的key,由"父名称-子名称"构成*/
	private String tagName;
	/**元素xpath路径*/
	private String xPath;
	/**深度*/
	private int deep;
	/**用于比较元素间差异的属性数组*/
	private String[] ids;
	/**是否为叶子元素*/
	private boolean isLeaf;
	/**属性显示格式*/
	private int attrShowStyle;
	
	private ModelXPathMessage parent;
	
	private List<ModelXPathMessage> children = new ArrayList<ModelXPathMessage>();
	
	public ModelXPathMessage(String tagName, String xPath, int deep, String[] ids, boolean isLeaf, int attrShowStyle) {
		super();
		this.tagName = tagName;
		this.xPath = xPath;
		this.deep = deep;
		this.ids = ids;
		this.isLeaf = isLeaf;
		this.attrShowStyle = attrShowStyle;
	}

	public String getTagName() {
		return tagName;
	}

	public void setTagName(String tagName) {
		this.tagName = tagName;
	}



	public String getXPath() {
		return xPath;
	}

	public void setXPath(String xPath) {
		this.xPath = xPath;
	}

	public int getDeep() {
		return deep;
	}

	public void setDeep(int deep) {
		this.deep = deep;
	}

	public String[] getIds() {
		return ids;
	}

	public void setIds(String[] ids) {
		this.ids = ids;
	}

	public boolean isLeaf() {
		return isLeaf;
	}

	public void setLeaf(boolean isLeaf) {
		this.isLeaf = isLeaf;
	}

	public int getAttrShowStyle() {
		return attrShowStyle;
	}

	public void setAttrShowStyle(int attrShowStyle) {
		this.attrShowStyle = attrShowStyle;
	}

	public ModelXPathMessage getParent() {
		return parent;
	}

	public void setParent(ModelXPathMessage parent) {
		this.parent = parent;
	}

	public List<ModelXPathMessage> getChildren() {
		return children;
	}

	public void setChildren(List<ModelXPathMessage> children) {
		this.children = children;
	}
	
	public void addChild(ModelXPathMessage child){
		children.add(child);
	}
	
	public ModelXPathMessage getChildByKeyName(String name){
		ModelXPathMessage child = null;
		for (int i = 0; i < children.size(); i++) {
			child = children.get(i);
			if(name.equals(child.getTagName())){
				return child;
			}
		}
		return null;
	}
	
}
