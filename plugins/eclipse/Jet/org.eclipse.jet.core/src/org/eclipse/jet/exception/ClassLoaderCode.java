package org.eclipse.jet.exception;

public enum ClassLoaderCode implements ErrorCode {
    
	PROJECT_LOADER_EXCEPTION(301),
	CLASS_LOADER_EXCEPTION(302),
	CLASS_REFLECT_EXCEPTION(303);
	
	private final int number;

	private ClassLoaderCode(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}
