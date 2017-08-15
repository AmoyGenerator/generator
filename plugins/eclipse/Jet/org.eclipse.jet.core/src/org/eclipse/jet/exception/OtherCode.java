package org.eclipse.jet.exception;

public enum OtherCode implements ErrorCode {
    
	UNKNOWN_EXCEPTION(101);
	
	private final int number;

	private OtherCode(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
}
