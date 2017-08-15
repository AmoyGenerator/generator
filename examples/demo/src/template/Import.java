package template;

public class Import {
    
	private String packageName;
	private String className;
	
	public Import(String packageName, String className) {
		super();
		this.packageName = packageName;
		this.className = className;
	}

	public String getPackageName() {
		
		return packageName;
	}

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}
	
}
