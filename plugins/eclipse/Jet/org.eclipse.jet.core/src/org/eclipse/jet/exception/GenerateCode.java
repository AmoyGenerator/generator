package org.eclipse.jet.exception;

public enum GenerateCode implements ErrorCode {
    
	TAG_EXCEPTION(401),
	COMPLIE_CLASS_EXCEPTION(402),
	GENERATE_RUN_EXCEPTION(403),
	CONTAINER_CREATE_EXCEPTION(404);
	
	private final int number;

	private GenerateCode(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
}