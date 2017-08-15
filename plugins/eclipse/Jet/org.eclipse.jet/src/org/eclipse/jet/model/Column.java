package org.eclipse.jet.model;

import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.model.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Column extends Model implements Comparable<Column>{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

    /**列名*/
	private String name;

	/**类型*/
	private String type;


	/**可选项*/
	private Select select;

	public Column() {
		super(ModelTagEnum.COLUMN.getValue());
	}

	public Column(String name, String type, Select select) {
		super(ModelTagEnum.COLUMN.getValue());	
		setName(name);
		setType(type);
		
		setSelect(select);
	}

	public String getName() {
		return StringUtils.exceptNull(name);
	}

	public void setName(String value) {
		super.setAttr(ModelTagAttrEnum.COLUMN_NAME.getValue(), value);
		name = value;
	}

	public String getType() {
		return StringUtils.exceptNull(type);
	}

	public void setType(String value) {
		super.setAttr(ModelTagAttrEnum.COLUMN_TYPE.getValue(), value);
		type = value;
	}

	public Select getSelect() {
		return select;
	}

	public void setSelect(Select select) {
		this.select = select;
	}

	@Override
	public Document getSourceDocument() {
		Document document = super.getSourceDocument();
		Element element = document.createElement(nodeName);
		document.appendChild(element);

		Attr attr;	
		for (int i = 0; i < attrs.size(); i++) {
			attr = attrs.get(i);
			element.setAttribute(attr.getKey(), attr.getValue());
		}

		if(select != null){
			Document selectDocument = select.getSourceDocument();
			Node selectNode = selectDocument.getDocumentElement();
			Node importNode = document.importNode(selectNode, true);
			element.appendChild(importNode);
		}
		
		return document;
	}

  public int compareTo(Column o)
  {
    return name.compareTo(o.getName());
  }

  @Override
  protected List<ProposalType> getByKey(String key)
  {
    return null;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    return null;
  }

}
