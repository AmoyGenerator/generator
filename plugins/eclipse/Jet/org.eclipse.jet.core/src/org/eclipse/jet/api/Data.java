package org.eclipse.jet.api;


public class Data {

	private String name;
	private Object value;

	public Data() {
		super();
	}
	
	public Data(String name, Object value) {
		super();
		this.name = name;
		this.value = value;
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

	@Override
	public String toString() {
		return "Data [name=" + name + ", value=" + value + "]";
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
