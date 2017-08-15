package org.eclipse.jet.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jet.model.util.ProposalType;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class Select extends Model{

  /**
   * 
   */
  private static final long serialVersionUID = 1L;
  /** table列表 */
	private List<Value> values = new ArrayList<Value>();

	public Select() {
		super(ModelTagEnum.SELECT.getValue());
	}

	public List<Value> getValues() {
		return values;
	}

	/**
	 * 根据序号得到元素
	 */
	public Value getValue(int index){
		return values.get(index);
	}

	/**
	 * 得到第一个元素
	 */
	public Value getFirstValue(){
		return values.get(0);
	}

	/**
	 * 得到最后一个元素
	 */
	public Value getLastValue(){
		int size = values.size();
		if(size > 0){
			return values.get(size - 1);
		}
		return null;
	}

	public void addValue(Value value){			
		value.setParent(this);
		values.add(value);			
	}

	public void add(int index, Value value){
		value.setParent(this);
		values.add(index, value);
	}

	public void addAllValue(Collection<? extends Value> c){
		Iterator<? extends Value> it = c.iterator();
		Value value;
		while(it.hasNext()){
			value = it.next();
			value.setParent(this);
		}
		values.addAll(c);
	}

	public void addAllValue(int index, Collection<? extends Value> c){
		Iterator<? extends Value> it = c.iterator();
		Value value;
		while(it.hasNext()){
			value = it.next();
			value.setParent(this);
		}
		values.addAll(index, c);
	}

	public void removeValue(int index){
		values.remove(index);
	}

	public void removeValue(Value value){
		values.remove(value);	
	}

	public void removeAllValue(Collection<?> c){
		values.removeAll(c);
	}

	@Override
	public Document getSourceDocument() {
		Document document = super.getSourceDocument();
		Element element = document.createElement(nodeName);
		document.appendChild(element);

		Value value;
		for (int i = 0; i < values.size(); i++) {
			value = values.get(i);
			Document valueDocument = value.getSourceDocument();
			Node valueNode = valueDocument.getDocumentElement();
			Node importNode = document.importNode(valueNode, true);
			element.appendChild(importNode);
		}
				
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
