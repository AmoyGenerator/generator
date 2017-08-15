/*******************************************************************************
 * Copyright (c) 2002, 2009 IBM Corporation and others.
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
 * $Id: JETReader.java,v 1.3 2009/04/06 17:55:06 pelder Exp $
 * /
 *******************************************************************************/
package org.eclipse.jet.internal.core.parser.jasper;


import java.io.BufferedReader;
import java.io.CharArrayWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.eclipse.jet.internal.core.expressions.IEmbeddedExpressionScanner;
import org.eclipse.jet.internal.core.parser.LineInfo;


/**
 * JETReader is an input buffer for the JSP parser. It should allow
 * unlimited lookahead and pushback. It also has a bunch of parsing
 * utility methods for understanding htmlesque thingies.
 */
public class JETReader
{
	public interface IStackPopNotifier {
		public abstract void stackPopped();
	}

	private static final char XML_TAG_START = '<';

	protected char startTagInitialChar = '<';

	protected char endTagInitialChar = '%';

	protected char endTagFinalChar = '>';

	protected JETMark current = null;

	protected String master = null;

	protected List sourceFiles = new ArrayList();

	protected List baseURIs = new ArrayList();

	protected int size = 0;

	protected boolean trimExtraNewLine = true;

	private IStackPopNotifier stackPopNotifier = null;

	public JETReader(String locationURI, Reader reader) throws JETException {
		this(null, locationURI, reader);
	}

	public JETReader(String baseURI, String locationURI, Reader reader) throws JETException {
		stackStream(baseURI, locationURI, reader);
	}

	public JETReader() {
	}

	/**
	 * 
	 * @param baseURI
	 * @param locationURI
	 * @param inputStream
	 * @param encoding
	 * @throws JETException
	 * @deprecated Use {@link #JETReader(String, String, Reader)}.
	 */
	public JETReader(String baseURI, String locationURI, InputStream inputStream, String encoding) throws JETException
	{
		stackStream(baseURI, locationURI, inputStream, encoding);
	}

	/**
	 * 
	 * @param locationURI
	 * @param inputStream
	 * @param encoding
	 * @throws JETException
	 * @deprecated Use {@link #JETReader(String, Reader)}.
	 */
	public JETReader(String locationURI, InputStream inputStream, String encoding) throws JETException
	{
		this(null, locationURI, inputStream, encoding);
	}

	public String getFile(int fileid)
	{
		return (String)sourceFiles.get(fileid);
	}

	public String getBaseURI(int fileid)
	{
		return (String)baseURIs.get(fileid);
	}

	public void stackStream(String locationURI, Reader reader) throws JETException {
		stackStream(null, locationURI, reader);
	}

	public void stackStream(String baseURI, String locationURI, Reader reader) throws JETException {
		try {
			// Register the file, and read its content:
			//
			int fileid = registerSourceFile(locationURI);
			registerBaseURI(baseURI);
			final char[] charArray = getContents(reader);
			if (current == null) {
				current = new JETMark(this, charArray, fileid, locationURI);
			} else {
				current.pushStream(charArray, fileid, locationURI);
			}
		} catch (UnsupportedEncodingException exception) {
			throw new JETException(exception);
		} catch (IOException exception) {
			throw new JETException(exception);
		}

	}
	/**
	 * 
	 * @param locationURI
	 * @param iStream
	 * @param encoding
	 * @throws JETException
	 * @deprecated Use {@link #stackStream(String, Reader)}.
	 */
	public void stackStream(String locationURI, InputStream iStream, String encoding) throws JETException
	{
		try {
			stackStream(locationURI, new BufferedReader(new InputStreamReader(iStream, encoding)));
		} catch (UnsupportedEncodingException e) {
			throw new JETException(e);
		}
	}

	/**
	 * Stack a stream for parsing
	 * @param iStream Stream ready to parse
	 * @param encoding Optional encoding to read the file.
	 * @deprecated Use {@link #stackStream(String, String, Reader)}.
	 */
	public void stackStream(String baseURI, String locationURI,
			InputStream iStream, String encoding) throws JETException {
		try {
			stackStream(baseURI, locationURI, new BufferedReader(new InputStreamReader(iStream, encoding)));
		} catch (UnsupportedEncodingException e) {
			throw new JETException(e);
		}
	}

	/**
	 * Read the contents of the reader into a char array.
	 * @param reader the reader.
	 * @return the contents as a char array.
	 * @throws IOException if an IO error occurs
	 */
	private char[] getContents(Reader reader) throws IOException {
		try {
			CharArrayWriter writer = new CharArrayWriter();
			char buf[] = new char [1024];
			for (int i = 0; (i = reader.read(buf)) != -1;){
				// Remove zero width non-breaking space, which may be used as a byte
				// order marker,
				// and may be ignored according to the Unicode FAQ:
				// http://www.unicode.org/unicode/faq/utf_bom.html#38
				//
				if (buf[0] == '\uFEFF') {
					writer.write(buf, 1, i - 1);
				}
				else {
					writer.write(buf, 0, i);
				}
			}
			writer.close();
			final char[] charArray = writer.toCharArray();
			return charArray; 
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (Exception exception) {
					// nothing we can do, suppress it.
				}
			}
		}

	}

	public boolean popFile()
	{
		// Is stack created ? (will happen if the JET file we're looking at is missing.
		//
		if (current == null)
		{
			return false;
		}

		// Restore parser state:
		//
		size--;

		if(stackPopNotifier != null) {
			stackPopNotifier.stackPopped();
		}

		return current.popStream();
	}

	/**
	 * Register a new source file.
	 * This method is used to implement file inclusion. Each included file
	 * gets a uniq identifier (which is the index in the array of source files).
	 * @return The index of the now registered file.
	 */
	protected int registerSourceFile(String file)
	{
		sourceFiles.add(file);
		++this.size;
		return sourceFiles.size() - 1;
	}

	/**
	 * Register a new baseURI.
	 * This method is used to implement file inclusion. Each included file
	 * gets a uniq identifier (which is the index in the array of base URIs).
	 * @return The index of the now registered file.
	 */
	protected void registerBaseURI(String baseURI)
	{
		baseURIs.add(baseURI);
	}

	/**
	 * Returns whether more input is available. If the end of the buffer for an included file is reached, it will return
	 * to the context of the previous file, and return whether more input is available from there. In this case, if
	 * trimExtraNewLine is true, then an unwanted extra newline character will be suppressed. We consider the first
	 * newline in the buffer we are returning to be unwanted if the ending buffer already has at least one trailing
	 * newline.
	 */
	public boolean hasMoreInput()
	{
		if (current.cursor < current.stream.length)
		{
			return true;
		}

		boolean nl = hasTrailingNewLine();
		while (popFile())
		{
			if (current.cursor < current.stream.length)
			{
				if (trimExtraNewLine && nl)
				{
					skipNewLine();
				}
				return true;
			}
		}
		return false;
	}

	/**
	 * Tests whether the current stream has at least one trailing newline, optionally followed by spaces.
	 */
	protected boolean hasTrailingNewLine()
	{
		if(current.lineInfo != null && current.lineInfo.length > 0)
		{
			return current.lineInfo[current.lineInfo.length - 1].getDelimiter().length() > 0;
		} else
		{
			return false;
		}
	}

	/**
	 * If the next character would be a line break, moves the cursor past it.
	 */
	protected void skipNewLine()
	{
		LineInfo li = LineInfo.getLineInfo(current.lineInfo, current.cursor);
		if(current.cursor >= li.getEnd()) {
			current.cursor = li.getEnd() + li.getDelimiter().length();
		}
	}

	public int nextChar()
	{
		if (!hasMoreInput())
		{
			return -1;
		}

		int ch = current.stream[current.cursor];

		++current.cursor;

		return ch;
	}

	/**
	 * Gets Content until the next potential JSP element.  Because all elements
	 * begin with a '&lt;' we can just move until we see the next one.
	 */
	public String nextContent()
	{
		int cur_cursor = current.cursor;
		int len = current.stream.length;
		if (cur_cursor == len)
			return ""; //$NON-NLS-1$
		// pure obsfuscated genius!
		while (++current.cursor < len && (current.stream[current.cursor]) != startTagInitialChar && current.stream[current.cursor] != XML_TAG_START && current.stream[current.cursor] != '$')
		{
			// do nothing
		}

		return new String(current.stream, cur_cursor, current.cursor - cur_cursor);
	}

	public char[] getChars(JETMark start, JETMark stop)
	{
		JETMark oldstart = mark();
		reset(start);
		CharArrayWriter writer = new CharArrayWriter();
		while (!stop.equals(mark()))
		{
			writer.write(nextChar());
		}
		writer.close();
		reset(oldstart);
		return writer.toCharArray();
	}

	public int peekChar()
	{
		if (hasMoreInput())
		{
			return current.stream[current.cursor];
		}
		else
		{
			return -1;
		}
	}

	public JETMark mark()
	{
		return new JETMark(current);
	}

	public void reset(JETMark mark)
	{
		current = new JETMark(mark);
	}

	public boolean matchesIgnoreCase(String string)
	{
		JETMark mark = mark();
		int ch = 0;
		int i = 0;
		do
		{
			ch = nextChar();
			if (Character.toLowerCase((char)ch) != string.charAt(i++))
			{
				reset(mark);
				return false;
			}
		}
		while (i < string.length());
		reset(mark);
		return true;
	}

	public boolean matches(String string)
	{
		JETMark mark = mark();
		int ch = 0;
		int i = 0;
		do
		{
			ch = nextChar();
			if (((char)ch) != string.charAt(i++))
			{
				reset(mark);
				return false;
			}
		}
		while (i < string.length());
		reset(mark);
		return true;
	}

	public void advance(int n)
	{
		while (--n >= 0)
			nextChar();
	}

	public int skipSpaces()
	{
		int i = 0;
		while (isSpace())
		{
			++i;
			nextChar();
		}
		return i;
	}

	/**
	 * Skip until the given string is matched in the stream.
	 * When returned, the context is positioned past the end of the match.
	 * @param limit The String to match.
	 * @return A non-null <code>JETMark</code> instance if found,
	 * <strong>null</strong> otherwise.
	 */
	public JETMark skipUntil(String limit)
	{
		JETMark ret = null;
		int limlen = limit.length();
		int ch;

		skip: for (ret = mark(), ch = nextChar(); ch != -1; ret = mark(), ch = nextChar())
		{
			if (ch == limit.charAt(0))
			{
				for (int i = 1; i < limlen; i++)
				{
					if (Character.toLowerCase((char)nextChar()) != limit.charAt(i))
					{
						continue skip;
					}
				}
				return ret;
			}
		}
		return null;
	}

	protected boolean isSpace()
	{
		int c = peekChar();
		return c <= ' ' && c >= 0;
	}

	/**
	 * Parse a space delimited token.
	 * If quoted the token will consume all characters up to a matching quote,
	 * otherwise, it consumes up to the first delimiter character.
	 * @param quoted If <strong>true</strong> accept quoted strings.
	 */
	public String parseToken(boolean quoted) throws JETException
	{
		return parseToken(quoted, true /* skip spaces*/);
	}

	/**
	 * Parse an attribute/value pair, and store it in provided hash table.
	 * The attribute/value pair is defined by:
	 * <pre>
	 * av := spaces token spaces '=' spaces token spaces
	 * </pre>
	 * Where <em>token</em> is defined by <code>parseToken</code> and
	 * <em>spaces</em> is defined by <code>skipSpaces</code>.
	 * The name is always considered case insensitive, hence stored in its
	 * lower case version.
	 * @param into The HashMap instance to save the result to.
	 */
	protected void parseAttributeValue(HashMap into) throws JETException
	{
		// Get the attribute name:
		//
		skipSpaces();
		String name = parseToken(false);

		// Check for an equal sign:
		//
		skipSpaces();
		if (peekChar() != '=')
		{
			throw new JETException(MessagesUtil.getString("jet.error.attr.novalue", new Object []{ name, mark().toString() })); //$NON-NLS-1$
		}
		nextChar();

		// Get the attribute value:
		//
		skipSpaces();
		String value = parseToken(true);
		skipSpaces();

		if(name != null && (name.equals("startTag") || name.equals("endTag"))){
			if(value == null || value.trim().isEmpty()){
				throw new JETException(MessagesUtil.getString("jet.error.attr.mustvalue", new Object []{name})); //$NON-NLS-1$
			}
		}
		
		// Add the binding to the provided hashtable:
		//
		into.put(name, value);
	}

	/**
	 * Parse some tag attributes for Beans.
	 * The stream is assumed to be positioned right after the tag name. The
	 * syntax recognized is:
	 * <pre>
	 * tag-attrs := empty | attr-list ("&gt;" | "--&gt;" | %&gt;)
	 * attr-list := empty | av spaces attr-list
	 * empty     := spaces
	 * </pre>
	 * Where <em>av</em> is defined by <code>parseAttributeValue</code>.
	 * @return A HashMap mapping String instances (variable names) into
	 * String instances (variable values).
	 */
	public HashMap parseTagAttributesBean() throws JETException
	{
		HashMap values = new HashMap(11);
		while (true)
		{
			skipSpaces();
			int ch = peekChar();
			if (ch == endTagFinalChar)
			{
				// End of the useBean tag.
				//
				return values;
			}
			else if (ch == '/')
			{
				JETMark mark = mark();
				nextChar();

				// XMLesque Close tags
				//
				try
				{
					if (nextChar() == endTagFinalChar)
					{
						return values;
					}
				}
				finally
				{
					reset(mark);
				}
			}
			if (ch == -1)
			{
				break;
			}

			// Parse as an attribute=value:
			//
			parseAttributeValue(values);
		}

		// Reached EOF:
		//
		throw new JETException(MessagesUtil.getString("jet.error.tag.attr.unterminated", new Object []{ mark().toString() })); //$NON-NLS-1$
	}

	public JETMark[] parseXmlAttribute() throws JETException
	{
		JETMark[] attributeMarks = new JETMark [5];

		attributeMarks[0] = mark(); // start of name
		String name = parseToken(false, false);
		attributeMarks[1] = mark(); // end of name

		skipSpaces();
		if (peekChar() != '=')
		{
			throw new JETException(MessagesUtil.getString("jet.error.param.novalue", new Object []{ name, mark().toString() })); //$NON-NLS-1$
		}
		attributeMarks[2] = mark(); // equals
		nextChar();

		// Get the attribute value:
		skipSpaces();
		attributeMarks[3] = mark(); // value start
		parseToken(true /* quoted token */, false);
		attributeMarks[4] = mark();

		return attributeMarks;
	}

	class AttReturn {
		private HashMap map;
		private JETException exception;
		public AttReturn(HashMap map, JETException exception) {
			super();
			this.map = map;
			this.exception = exception;
		}
		public HashMap getMap() {
			return map;
		}
		public void setMap(HashMap map) {
			this.map = map;
		}
		public JETException getException() {
			return exception;
		}
		public void setException(JETException exception) {
			this.exception = exception;
		}
	}
	
	/**
	 * Parse some tag attributes. The stream is assumed to be positioned right
	 * after the tag name. The syntax recognized is:
	 * 
	 * <pre>
	 *  tag-attrs := empty | attr-list (&quot;&gt;&quot; | &quot;--&gt;&quot; | %&gt;)
	 *  attr-list := empty | av spaces attr-list
	 *  empty     := spaces
	 * </pre>
	 * 
	 * Where <em>av</em> is defined by <code>parseAttributeValue</code>.
	 * 
	 * @return A HashMap mapping String instances (variable names) into String
	 *         instances (variable values).
	 */
	public AttReturn parseTagAttributes() throws JETException
	{
		HashMap values = new LinkedHashMap(11);
		JETException findException = null;
		while (true)
		{
			skipSpaces();
			int ch = peekChar();
			if (ch == endTagFinalChar)
			{
				return new AttReturn(values, null);
			}

			if (ch == '-')
			{
				JETMark mark = mark();
				nextChar();
				// Close NCSA like attributes "->"
				try
				{
					if (nextChar() == '-' && nextChar() == endTagFinalChar)
					{
					   return new AttReturn(values, null);
					}
				}
				finally
				{
					reset(mark);
				}
			}
			else if (ch == endTagInitialChar)
			{
				JETMark mark = mark();
				nextChar();
				// Close variable like attributes "%>"
				try
				{
					if (nextChar() == endTagFinalChar)
					{
						return new AttReturn(values, null);
					}
				}
				finally
				{
					reset(mark);
				}
			}
			else if (ch == '/')
			{
				JETMark mark = mark();
				nextChar();
				// XMLesque Close tags
				try
				{
					if (nextChar() == endTagFinalChar)
					{
						return new AttReturn(values, null);
					}
				}
				finally
				{
					reset(mark);
				}
			}
			if (ch == -1)
			{
				return new AttReturn(values, null);
			}
			
			// Parse as an attribute=value:
			try {
				parseAttributeValue(values);
			} catch (JETException e) {
				findException = e;
				break;
			}
		}
		return new AttReturn(values, findException);
		// Reached EOF:
		//    throw new JETException(CodeGenPlugin.getPlugin().getString("jet.error.tag.attr.unterminated", new Object [] { mark().toString() }));
	}

	/**
	 * Parse utils - Is current character a token delimiter ?
	 * Delimiters are currently defined to be =, &gt;, &lt;, ", and ' or any
	 * any space character as defined by <code>isSpace</code>.
	 * @return A boolean.
	 */
	protected boolean isDelimiter()
	{
		if (!isSpace())
		{
			int ch = peekChar();

			// Look for a single-char work delimiter:
			//
			if (ch == '=' || ch == endTagFinalChar || ch == '"' || ch == '\'' || ch == '/' || ch == '>')
			{
				return true;
			}

			// Look for an end-of-comment or end-of-tag:
			//
			if (ch == '-')
			{
				JETMark mark = mark();
				if (((ch = nextChar()) == endTagFinalChar) || ((ch == '-') && (nextChar() == endTagFinalChar)))
				{
					reset(mark);
					return true;
				}
				else
				{
					reset(mark);
					return false;
				}
			}
			return false;
		}
		else
		{
			return true;
		}
	}

	public void setStartTag(String startTag)
	{
		startTagInitialChar = startTag.charAt(0);
	}

	public void setEndTag(String endTag)
	{
		endTagFinalChar = endTag.charAt(endTag.length() - 1);
		endTagInitialChar = endTag.charAt(0);
	}

	/**
	 * Parse a space delimited token.
	 * If quoted the token will consume all characters up to a matching quote,
	 * otherwise, it consumes up to the first delimiter character.
	 * @param quoted If <strong>true</strong> accept quoted strings.
	 * @param skipSpaces if <code>true</code>, skip leading spaces to find the token, false otherwise.
	 * @return the token, or an empty string if no token is found.
	 */
	public String parseToken(boolean quoted, boolean skipSpaces) throws JETException
	{
		StringBuffer stringBuffer = new StringBuffer();
		if (skipSpaces)
		{
			skipSpaces();
		}
		stringBuffer.setLength(0);

		int ch = peekChar();

		if (quoted)
		{
			if (ch == '"' || ch == '\'')
			{
				char endQuote = ch == '"' ? '"' : '\'';

				// Consume the open quote:
				//
				ch = nextChar();
				for (ch = nextChar(); ch != -1 && ch != endQuote; ch = nextChar())
				{
					if (ch == '\\')
					{
						ch = nextChar();
					}
					stringBuffer.append((char)ch);
				}

				// Check end of quote, skip closing quote:
				//
				if (ch == -1)
				{
					throw new JETException(MessagesUtil.getString("jet.error.quotes.unterminated", new Object []{ mark().toString() })); //$NON-NLS-1$
				}
			}
			else
			{
				throw new JETException(MessagesUtil.getString("jet.error.attr.quoted", new Object []{ mark().toString() })); //$NON-NLS-1$
			}
		}
		else
		{
			if (!isDelimiter())
			{
				// Read value until delimiter is found:
				do
				{
					ch = nextChar();
					// Take care of the quoting here.
					if (ch == '\\')
					{
						if (peekChar() == '"' || peekChar() == '\'' || peekChar() == endTagFinalChar || peekChar() == endTagInitialChar || peekChar() == '>')
						{
							ch = nextChar();
						}
					}
					else if (ch == -1)
					{
						break;
					}
					stringBuffer.append((char)ch);
				}
				while (!isDelimiter());
			}
		}
		return stringBuffer.toString();
	}

	public char[] getChars() 
	{
		return current.stream;
	}


	/**
	 * Set the stack pop notifier
	 * @param stackPopNotifier an instance of {@link IStackPopNotifier} or <code>null</code>
	 */
	public void setStackPopNotifier(IStackPopNotifier stackPopNotifier) {
		this.stackPopNotifier = stackPopNotifier;
	}

	/**
	 * @param reader
	 * @param scanner
	 * @return
	 */
	public JETMark scanEmbeddedExpression(	final IEmbeddedExpressionScanner scanner) {
		for(int ch = nextChar(); ch != -1; ch = nextChar()) {
			scanner.setNextChar(ch);
			if(!scanner.ignoreChar() && ch == '}') {
				return mark();
			}
		}
		return null;
	}

}
