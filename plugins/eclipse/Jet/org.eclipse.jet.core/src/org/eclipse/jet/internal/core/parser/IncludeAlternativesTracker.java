/*******************************************************************************
 * Copyright (c) 2006, 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id: IncludeAlternativesTracker.java,v 1.3 2007/04/12 18:02:42 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser;

import java.util.Stack;

/**
 * Track processing and nesting of &#064;include's with &#064;start and &#064;end directives for the JET compiler.
 */
public class IncludeAlternativesTracker {
	private final Stack stack = new Stack();

	private static final class State {

		public static final State INITIAL = new State("INITIAL"); //$NON-NLS-1$

		public static final State START = new State("START"); //$NON-NLS-1$

		public static final State END = new State("END"); //$NON-NLS-1$

		private final String display;

		private State(String display) {
			this.display = display;
		}

		public String toString() {
			return display;
		}
	}

	private static final class IncludeWithAlternative {

		private State state;

		private final boolean processAlternative;

		private final boolean parentCompileEnabled;

		private final Object includeDirective;

		private Object startDirective;

		public IncludeWithAlternative(Object includeDirective,
				boolean processAlternative, boolean parentCompileEnabled) {
			this.includeDirective = includeDirective;
			this.processAlternative = processAlternative;
			this.parentCompileEnabled = parentCompileEnabled;
			this.state = State.INITIAL;

		}

		public void doStart(Object startDirective) throws IllegalStateException {
			this.startDirective = startDirective;
			if (state != State.INITIAL) {
				throw new IllegalStateException();
			}

			state = State.START;
		}

		public void doEnd(Object startDirective) throws IllegalStateException {
			if (state != State.START) {
				throw new IllegalStateException();
			}

			state = State.END;
		}

		public boolean isProcessAlternative() {
			return processAlternative;
		}

		public boolean isCompileEnabled() throws IllegalStateException {
			if (state == State.END) {
				throw new IllegalStateException();
			}
			return parentCompileEnabled
					&& (state != State.START || isProcessAlternative());
		}

		public boolean isStateInitial() {
			return state == State.INITIAL;
		}

		public boolean isStateStart() {
			return state == State.START;
		}

		public Object getIncludeDirective() {
			return includeDirective;
		}

		public Object getStartDirective() {
			return startDirective;
		}
	}

	/**
	 * Test whether compiling is currently enabled
	 * @return <code>true</code> if compiling currently enabled.
	 */
	public boolean isCompileEnabled() {
		return stack.isEmpty()
				|| ((IncludeWithAlternative) stack.peek()).isCompileEnabled();
	}

	/**
	 * Start tracking an <code>&lt%&#064;include fail="alternative" ... %&gt;</code> directive
	 * @param directive the include directive
	 * @param processAlternative true if the content between &#064;start and &#064;end directives should be processed by the compiler.
	 */
	public void addIncludeWithAlternative(Object directive,
			boolean processAlternative) {
		stack.push(new IncludeWithAlternative(directive, processAlternative,
				isCompileEnabled()));
	}

	/**
	 * Mark the beginning of an include alternative, a <code>&lt;%&#064;start%&gt;</code> directive.
	 * @param directive the Start directive
	 * @throws IllegalStateException if there is no preceeding <code>at;include fail="alternative"</code> directive.
	 */
	public void startAlternative(Object directive) throws IllegalStateException {
		if (stack.isEmpty()) {
			throw new IllegalStateException();
		}

		IncludeWithAlternative include = (IncludeWithAlternative) stack.peek();
		include.doStart(directive);
	}

	/**
	 * Mark the end of an include alternative, a <code>&lt;%&#064;end%&gt;</code> directive.
	 * @param directive
	 * @throws IllegalStateException
	 */
	public void endAlternative(Object directive) throws IllegalStateException {
		if (stack.isEmpty()) {
			throw new IllegalStateException();
		}
		// only peek on the stack, until we're certain doEnd succeeds
		IncludeWithAlternative include = (IncludeWithAlternative) stack.peek();
		include.doEnd(directive);
		stack.pop();
	}

	public static interface ValidationProblemReporter {

		void reportMissingStart(Object includeDirective);

		void reportMissingEnd(Object startDirective);

	}

	public void validateStackIsEmpty(ValidationProblemReporter reporter) {
		while (!stack.isEmpty()) {
			IncludeWithAlternative include = (IncludeWithAlternative) stack
					.pop();
			if (include.isStateInitial()) {
				reporter.reportMissingStart(include.getIncludeDirective());
			} else if (include.isStateStart()) {
				reporter.reportMissingEnd(include.getStartDirective());
			}
		}
	}
}
