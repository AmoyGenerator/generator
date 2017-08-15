package org.eclipse.jet.model;

import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.model.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Sign extends Model {
	
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String name;
	
	private String text;
	
	public Sign() {
		super(ModelTagEnum.SIGN.getValue());
	}
	
	public Sign(String name, String text) {
		super(ModelTagEnum.SIGN.getValue());
		setName(name);
		setText(text);
	}
		
	public String getName() {
		return StringUtils.exceptNull(name);
	}

	public void setName(String value) {
		super.setAttr(ModelTagAttrEnum.SIGN_NAME.getValue(), value);
		name = value;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	

	@Override
	public Document getSourceDocument() {	
		
		Document document = super.getSourceDocument();
		if(document == null){
			return null;
		}
		Element element = document.createElement(nodeName);
		document.appendChild(element);
		
		Attr attr;	
		for (int i = 0; i < attrs.size(); i++) {
			attr = attrs.get(i);
			element.setAttribute(attr.getKey(), attr.getValue());
		}	
	    
		element.setTextContent(text);
		return document;
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