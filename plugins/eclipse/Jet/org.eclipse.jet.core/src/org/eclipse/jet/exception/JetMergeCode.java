package org.eclipse.jet.exception;

public enum JetMergeCode implements ErrorCode {
    
	TARGET_IO_EXCEPTION(501),
	TARGET_SET_EXCEPTION(502);
	
	private final int number;

	private JetMergeCode(int number) {
		this.number = number;
	}

	public int getNumber() {
		return number;
	}
	
}