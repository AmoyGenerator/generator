/**
 * Copyright (c) 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 */
package org.eclipse.jet.internal.compiler;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.nio.charset.CoderResult;
import java.nio.charset.CodingErrorAction;

/**
 * Utility class for encoding Java code
 *
 */
public class UnicodeEscapeUtil {

	private final CharsetEncoder encoder;
	private final CharBuffer in;
	private ByteBuffer out;
	private CharBuffer unicodeEscapes;

	private UnicodeEscapeUtil(Charset charset, CharBuffer in) {
		this.in = in;
		encoder = charset.newEncoder()
			.onUnmappableCharacter(CodingErrorAction.REPORT)
			.onMalformedInput(CodingErrorAction.REPLACE);
		int capacity = (int) (in.remaining() * encoder.averageBytesPerChar());
		out = ByteBuffer.allocate(capacity);
		// for convenience, ensure uncideEscapes is never null
		unicodeEscapes = CharBuffer.wrap(""); //$NON-NLS-1$
	}

	/**
	 * Follow the contract of {@link CharsetEncoder#encode(CharBuffer, ByteBuffer, boolean)}, 
	 * with {@link CharsetEncoder#onUnmappableCharacter(CodingErrorAction)} set to
	 * {@link CodingErrorAction#REPORT}. On such report, replace the offending characters
	 * with their Unicode escape equivalent \\uXXXX, and continue encoding.
	 * @return
	 * @throws CharacterCodingException
	 */
	private ByteBuffer doEncode()
			throws CharacterCodingException {
	
		if (in.remaining() == 0) {
			return out;
		}
		encoder.reset();
		for (;;) {
			CoderResult cr;
			if(unicodeEscapes.hasRemaining()) {
				cr = encoder.encode(unicodeEscapes, out, false);
			} else {
				// 
				cr = encoder.encode(in, out, false);
			} 
			if (cr.isUnderflow()) {
				if(in.hasRemaining()) {
					continue;
				} else {
					break;
				}
			}
			if (cr.isOverflow()) {
				// re-allocate and copy the output buffer
				final int newCapacity = out.capacity() 
					+ (int)((in.remaining() + unicodeEscapes.remaining()) * encoder.averageBytesPerChar());
				final ByteBuffer newOut = ByteBuffer.allocate(newCapacity);
				out.flip();
				newOut.put(out);
				out = newOut;
				continue;
			}
			if(cr.isUnmappable()) {
				// re-write any unmappable characters as unicode escapes
				StringBuffer buffer = new StringBuffer(6 * cr.length());
				for(int i = 0; i < cr.length(); i++ ) {
					int ch = in.get();
					buffer.append("\\u"); //$NON-NLS-1$
					String hex = Integer.toHexString(ch);
					for(int j = 4 - hex.length(); j > 0; j--) buffer.append('0');
					buffer.append(hex);
				}
				unicodeEscapes = CharBuffer.wrap(buffer.toString());
				continue;
			}
			cr.throwException();
		}
		// religiously follow the contract for encode. 
		encoder.encode(in, out, true);
		encoder.flush(out);
		out.flip();
		return out;
	
	}

	/**
     * Encode the passed contents in the specified character set, re-writting any characters
     * not representable in that character set as a Java unicode escape (\\uXXXX0).
     * @param buffer the contents to encode
     * @param charset the character set or {@link NullPointerException}
     * @return a ByteBuffer containing the encoded contents
     * @throws CharacterCodingException if an encoding error occurs.
	 */
	public static ByteBuffer encode(CharBuffer buffer, Charset charset) throws CharacterCodingException {
		return new UnicodeEscapeUtil(charset, buffer).doEncode();
	}

	
	/**
	 * Encode the passed contents in the specified character set, re-writting any characters
	 * not representable in that character set as a Java unicode escape (\\uXXXX0).
	 * @param contents the contents to encode
	 * @param charset the character set or {@link NullPointerException}
	 * @return a ByteBuffer containing the encoded contents
	 * @throws CharacterCodingException if an encoding error occurs.
	 */
	public static ByteBuffer encode(CharSequence contents, Charset charset) throws CharacterCodingException {
		return encode(CharBuffer.wrap(contents), charset);
	}
}
