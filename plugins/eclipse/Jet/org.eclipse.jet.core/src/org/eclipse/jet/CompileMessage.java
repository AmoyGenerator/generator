package org.eclipse.jet;

import java.util.Map;

public class CompileMessage {
	
	private Class compiledClass;

	private String javaSource;

	private Map mappingPositions;

	public CompileMessage(Class compiledClass, String javaSource,
			Map mappingPositions) {
		super();
		this.compiledClass = compiledClass;
		this.javaSource = javaSource;
		this.mappingPositions = mappingPositions;
	}

	public Class getCompiledClass() {
		return compiledClass;
	}

	public void setCompiledClass(Class compiledClass) {
		this.compiledClass = compiledClass;
	}

	public String getJavaSource() {
		return javaSource;
	}

	public void setJavaSource(String javaSource) {
		this.javaSource = javaSource;
	}

	public Map getMappingPositions() {
		return mappingPositions;
	}

	public void setMappingPositions(Map mappingPositions) {
		this.mappingPositions = mappingPositions;
	}


}
