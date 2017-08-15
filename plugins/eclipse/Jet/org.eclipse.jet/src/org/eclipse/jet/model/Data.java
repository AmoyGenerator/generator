package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.eclipse.jet.model.util.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;


public class Data extends Model {
	
  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private String name;
	
	private String text;
	
	public Data() {
		super(ModelTagEnum.DATA.getValue());
	}
	
	public Data(String name, String text) {
		super(ModelTagEnum.DATA.getValue());
		setName(name);
		setText(text);
	}
		
	public String getName() {
		return StringUtils.exceptNull(name);
	}

	public void setName(String value) {
		super.setAttr(ModelTagAttrEnum.DATA_NAME.getValue(), value);
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
    List<ProposalType> list = new ArrayList<ProposalType>();
    ProposalType nameProposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, "name");
    nameProposalType.addValue(name);
    list.add(nameProposalType);
    ProposalType textProposalType = new ProposalType(ProposalType.STYLE_CONTEXT, ProposalType.TYPE_PROPERTY, "text");
    textProposalType.addValue(text);
    list.add(textProposalType);
    return list;
  }

  @Override
  protected List<Model> getModelByKey(String key)
  {
    return null;
  }

}
