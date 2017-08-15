package org.eclipse.jet.api;


public class Property {

	private String name;
	private Object value;
	private Boolean isNew;

	public Property(String name, Object value, Boolean isNew) {
		super();
		this.name = name;
		this.value = value;
		this.isNew = isNew;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public Boolean getIsNew() {
		return isNew;
	}

	public void setIsNew(Boolean isNew) {
		this.isNew = isNew;
	}

	@Override
	public String toString() {
		if(value != null){
			return String.valueOf(value);
		}
		return null;
	}

//	public Map<?, ?> toMap(){
//		try {
//			Object object = Ognl.getValue(value, new HashMap<Object, Object>());
//			if(object instanceof Map<?, ?>){
//				return (Map<?, ?>)object;
//			}
//		} catch (OgnlException e) {
//		}
//		return null;
//	}
//
//	public List<?> toList(){
//		try {
//			Object object = Ognl.getValue(value, new HashMap<Object, Object>());
//			if(object instanceof List<?>){
//				return (List<?>)object;
//			}
//		} catch (OgnlException e) {
//		}
//		return null;
//	}
//	
//	public Boolean toBool(){
//		try {
//			Object object = Ognl.getValue(value, new HashMap<Object, Object>());
//			if(object instanceof Boolean){
//				return (Boolean)object;
//			}
//		} catch (OgnlException e) {
//		}
//		return null;
//	}
	
}
