/*
 * Copyright (c) 2007 IBM Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     IBM Corporation - initial API and implementation
 */
package org.eclipse.jet;
import org.eclipse.jet.internal.core.ContextLogEntryFactoryManager;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Entry in the log created by JET2Context log methods 
 *
 */
public final class ContextLogEntry {

	/**
	 * Builder for {@link ContextLogEntry} entities
	 *
	 */
	public static final class Builder {

		private final int severity;
		private final ContextLogEntry[] children;
		private String message;
		private Throwable exception;
		private String templatePath;
		private TagInfo tagInfo;
		private int line;
		private int col; 

		/**
		 * Create a ContextLogEntry Builder that summarizes a collection
		 * of other ContextLog Entries.
		 * @param children a non-null list of ContextLogEntries
		 * @throws NullPointerException if children is <code>null</code> or an array entry is <code>null</code>
		 */
		public Builder(ContextLogEntry[] children) {
			if(children == null) {
				throw new NullPointerException();
			}
			this.children = children;
			int severity = OK;
			for (int i = 0; i < children.length; i++) {
				if(children[i] == null) {
					throw new NullPointerException(String.valueOf(i));
				}
				severity = Math.max(severity, children[i].getSeverity());
			}
			this.severity = severity;
		}
		
		/**
		 * Create a ContextLogEntry Builder for a single entry with the indicated severity
		 * @param severity one of {@link ContextLogEntry#OK}, {@link ContextLogEntry#INFO}, 
		 * {@link ContextLogEntry#WARNING}, {@link ContextLogEntry#ERROR} or {@link ContextLogEntry#CANCEL}
		 * @throws IllegalArgumentException if severity is not a valid value
		 */
		public Builder(int severity) {
			if(severity != ContextLogEntry.OK 
					&& severity != ContextLogEntry.INFO
					&& severity != ContextLogEntry.WARNING
					&& severity != ContextLogEntry.ERROR
					&& severity != ContextLogEntry.CANCEL) {
				throw new IllegalArgumentException();
			}
			this.severity = severity;
			this.children = EMPTY_ENTRIES_ARRAY;
		}
		
		/**
		 * Specify an option message for the ContextLogEntry. Note that
		 * this replaces any previously specified or calculated message
		 * for the entry
		 * @param message a message, possibly <code>null</code>
		 * @return this builder
		 */
		public Builder message(String message) {
			this.message = message;
			return this;
		}
		
		/**
		 * Specify an exception for the ContextLogEntry. 
		 * Note that
		 * this replaces any previously specified exception for the entry
		 * @param exception an exception, possibly <code>null</code>
		 * @return this builder
		 */
		public Builder exception(Throwable exception) {
			this.exception = exception;
			if(message == null) {
				message(exception.getLocalizedMessage());
			}
			if(message == null) {
				message(exception.toString());
			}
			return this;
		}
		
		/**
		 * Specify the template path where the entry originates
		 * Note that
		 * this replaces any previously specified templatePath for the entry
		 * @param templatePath a template path
		 * @return this builder
		 */
		public Builder templatePath(String templatePath) {
			this.templatePath = templatePath;
			return this;
		}
		
		/**
		 * Specify the template line and column for the entry.
		 * Note that
		 * this replaces any previously specified line and column for the entry
		 * @param line the line
		 * @param col the column
		 * @return this builder
		 */
		public Builder location(int line, int col) {
			this.line = line;
			this.col = col;
			return this;
		}
		
		/**
		 * Specify the JET tag information for this entry.
		 * Note that seting tag information will replace any
		 * previously specified location information. 
		 * @param tagInfo a tag information object
		 * @return this builder
		 * @throws NullPointerException if tagInfo is null
		 */
		public Builder tagInfo(TagInfo tagInfo) {
			if(tagInfo == null) {
				throw new NullPointerException();
			}
			this.tagInfo = tagInfo;
			location(tagInfo.getLine(), tagInfo.getCol());
			return this;
		}
		
		/**
		 * Construct the ContextLogEntry based on information provided in the builder.
		 * @return the contenxt log entry
		 */
		public ContextLogEntry build() {
			return new ContextLogEntry(this);
		}

		/**
		 * Returnt the severity for the log entry
	     * @return one of {@link #OK}, {@link #INFO}, {@link #WARNING}, {@link #ERROR}, {@link #CANCEL}.
		 */
		public int getSeverity() {
			return severity;
		}
	}
	
	  private static final ContextLogEntry[] EMPTY_ENTRIES_ARRAY = new ContextLogEntry[0];

	/**
		 * The bit mask value <code>0x0</code> for a
		 * {@link #getSeverity severity} indicating everything is okay.
		 */
	public static final int OK = 0x0;

	/**
	 * The bit mask value <code>0x1</code> for a {@link #getSeverity severity}
	 * indicating there is an informational message.
	 */
	public static final int INFO = 0x1;

	/**
	 * The bit mask value <code>0x2</code> for a {@link #getSeverity severity}
	 * indicating there is warning message.
	 */
	public static final int WARNING = 0x2;

	/**
	 * The bit mask value <code>0x1</code> for a {@link #getSeverity severity}
	 * indicating there is an error message.
	 */
	public static final int ERROR = 0x4;

	/**
	 * The bit mask value <code>0x1</code> for a {@link #getSeverity severity}
	 * indicating that the diagnosis was canceled.
	 */
	public static final int CANCEL = 0x8;
	
	private final int severity;
	
	private final ContextLogEntry[] children;

	private final String message;

	private final Throwable exception;

	private final String templatePath;

	private final int line;

	private final int col;

	private final TagInfo tagInfo;
	
	/**
	 * Construct an entry from the builder
	 * @param builder
	 */
	private ContextLogEntry(Builder builder) {
		this.severity = builder.severity;
		
		if(builder.children.length > 0) {
			this.children = new ContextLogEntry[builder.children.length];
			System.arraycopy(builder.children, 0, this.children, 0, builder.children.length);
		} else if(builder.exception != null && builder.exception.getCause() != null) {
			final ContextLogEntry childEntry = ContextLogEntryFactoryManager
					.getFactory().create(builder.exception.getCause());
			this.children = new ContextLogEntry[] {childEntry};
		} else {
			children = EMPTY_ENTRIES_ARRAY;
		}
		
		this.message = builder.message == null ? "" : builder.message; //$NON-NLS-1$
		
		this.exception = builder.exception;
		
		this.templatePath = builder.templatePath;
		
		this.line = builder.line;
		this.col = builder.col;
		this.tagInfo = builder.tagInfo;
	}

	/**
	 * Return the array of child log entries
	 * @return the possibly empty array of child log entries
	 */
	public ContextLogEntry[] getChildren() {
		ContextLogEntry[] copy = new ContextLogEntry[children.length];
		System.arraycopy(children, 0, copy, 0, children.length);
		
		return copy;
	}

	/**
	 * Return the severity of the entry
	 * @return one of {@link #OK}, {@link #INFO}, {@link #WARNING}, {@link #ERROR}, {@link #CANCEL}.
	 */
	public int getSeverity() {
		return severity;
	}

	/**
	 * Return the log entry message
	 * @return the message a possibly empty (but non-null) string
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * Return the exception associated with the log entry
	 * @return the exception. May be <code>null</code>
	 */
	public Throwable getException() {
		return exception;
	}

	/**
	 * Return the template path associated with the log entry
	 * @return the templatePath. May be <code>null</code>.
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * Return the column number associated with the entry
	 * @return the col the column number. A zero or negative value means no column number is associated
	 */
	public int getCol() {
		return col;
	}

	/**
	 * Return the line number associated with the entry
	 * @return the line number. A zero or negative value means no line number is associated
	 */
	public int getLine() {
		return line;
	}

	/**
	 * Return the tag information associated with the entry
	 * @return the tagInfo. May be <code>null</code>.
	 */
	public TagInfo getTagInfo() {
		return tagInfo;
	}
}
