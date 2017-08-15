package org.eclipse.jet.exception;

public enum ModelCode implements ErrorCode {
    
	MODEL_EXCEPTION(601);
	
	private final int number;

	private ModelCode(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
}