package org.eclipse.jet.model;

import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Value extends Model{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  private String text;
	
	public Value() {
		super(ModelTagEnum.VALUE.getValue());
	}

	public Value(String value) {
		super(ModelTagEnum.VALUE.getValue());
		text = value;
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
