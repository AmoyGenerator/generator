/*******************************************************************************
 * Copyright (c) 2005, 2006 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.jet.internal.exceptions;

import java.text.MessageFormat;

import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.JET2TagException;

public class MissingRequiredAttributeException extends JET2TagException {

	private static final long serialVersionUID = -143366914438772236L;

	private String attributeName;
	
	public MissingRequiredAttributeException(String attributeName) {
		super();
		this.attributeName = attributeName;
	}

	public String getLocalizedMessage() {
		return buildMessage();
	}

	public String getMessage() {
		return buildMessage();
	}

	public String toString() {
		return buildMessage();
	}

	private String buildMessage() {
		return MessageFormat.format(getPattern(),getArgs());
	}

	private String getPattern() {
		return JET2Messages.JET2Compiler_MissingRequiredAttribute;
	}

	private Object[] getArgs() {
		String args[] = new String[] {
          attributeName,
        };
		return args;
	}

	
}
