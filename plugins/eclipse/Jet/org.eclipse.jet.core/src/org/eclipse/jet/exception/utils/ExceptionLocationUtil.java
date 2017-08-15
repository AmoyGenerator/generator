/**
 * <copyright>
 *
 * Copyright (c) 2013 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 * </copyright>
 *
 * $Id$
 */
package org.eclipse.jet.exception.utils;

import java.util.Iterator;
import java.util.Map;

import org.eclipse.jet.CompileMessage;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.core.parser.ast.JETASTElement;
import org.eclipse.jet.core.parser.ast.JavaDeclaration;
import org.eclipse.jet.core.parser.ast.JavaScriptlet;
import org.eclipse.jface.text.Position;

public class ExceptionLocationUtil
{

	private static final String NL = System.getProperty("line.separator");

	public static String getJetLocation(String templatePath, Integer line){
		String jetLocation = "(" + getProjectPath(templatePath) + (line!=null?":"+line:"") + ")";
		return jetLocation;
	}

	private static String getProjectPath(String templatePath){
	    if(templatePath != null && templatePath.startsWith("../")){
	      templatePath = templatePath.substring(3);
	    }
	    return templatePath;
	  }
	
	public static String getExceptionJetLocation(Exception e, JET2Context content, String templatePath){
		String jetLocation = "(" + getProjectPath(templatePath)  + ")";
		CompileMessage compileMessage  = content.getCompileMessage();
		if(compileMessage != null){
			Class compiledClass = compileMessage.getCompiledClass();
			String javaSource = compileMessage.getJavaSource();
			Map mappingPositions = compileMessage.getMappingPositions();
			if(templatePath != null && compiledClass != null && javaSource != null && mappingPositions != null){
				int traceLine = getStrceLine(e, compiledClass);
				if(traceLine > 0){
					int exceptionLine = traceLine;
					String afterContent = getLinesContent(javaSource, exceptionLine - 1);
					int exceptionLineStart = javaSource.indexOf(afterContent);
					if(exceptionLineStart != -1){
						JETASTElement astElement = getElementForJavaOffset(mappingPositions, exceptionLineStart);
						if(astElement != null){
							String lineContent = getLineContent(javaSource, exceptionLine - 1);
							if(lineContent != null){
								int atJavaScriptLine = getLineOfContent(astElement, lineContent);
								if(atJavaScriptLine != -1){
									int exceptionJetLine = astElement.getLine() + atJavaScriptLine;
									jetLocation = "(" + getProjectPath(templatePath) + ":" + exceptionJetLine + ")";
								}
							}
						}
					}
				}
			}
		}
		return jetLocation;
	}

	public static int getStrceLine(Exception e, Class compiledClass){
		int traceLine = 0;
		StackTraceElement[] traceElements = e.getStackTrace();
		for (int i = 0; i < traceElements.length; i++) {
			StackTraceElement traceElement = traceElements[i];
			String stackTraceClassName = traceElement.getClassName();
			int lineNumber = traceElement.getLineNumber();
			if(stackTraceClassName != null && stackTraceClassName.equals(compiledClass.getName())){
				traceLine = lineNumber;
				break;
			}
		}
		return traceLine;
	}

	public static int getStrceIndex(Exception e, Class compiledClass){
		int traceIndex = 0;
		StackTraceElement[] traceElements = e.getStackTrace();
		for (int i = 0; i < traceElements.length; i++) {
			StackTraceElement traceElement = traceElements[i];
			String stackTraceClassName = traceElement.getClassName();
			if(stackTraceClassName != null && stackTraceClassName.equals(compiledClass.getName())){
				traceIndex = i;
				break;
			}
		}
		return traceIndex;
	}

	private static String getLinesContent(String targetContent, int startLine){
		if(targetContent != null){
			StringBuffer content = new StringBuffer();
			String[] lines = targetContent.split(NL);
			for (int i = startLine; i < lines.length; i++) {
				content.append(lines[i]);
				if(i < lines.length - 1){
					content.append(NL);
				}
			}
			return content.toString();
		}
		return null;
	}

	private static String getLineContent(String targetContent, int line){
		if(targetContent != null){
			String[] lines = targetContent.split(NL);
			if(line < lines.length){
				return lines[line]; 
			}
		}
		return null;
	}

	private static int getLineOfContent(JETASTElement astElement, String content){
		if(astElement instanceof JavaScriptlet){
			JavaScriptlet javaScriptlet = (JavaScriptlet) astElement;
			String javaContent = javaScriptlet.getJavaContent();
			if(javaContent != null){
				String[] lines = javaContent.split(NL);
				for (int i = 0; i < lines.length; i++) {
					String lineContent = lines[i];
					if(lineContent.equals(content)){
						return i;
					}
				}
			}
		}else if(astElement instanceof JavaDeclaration){
			JavaDeclaration javaDeclaration = (JavaDeclaration) astElement;
			String javaContent = javaDeclaration.getJavaContent();
			if(javaContent != null){
				String[] lines = javaContent.split(NL);
				for (int i = 0; i < lines.length; i++) {
					String lineContent = lines[i];
					if(lineContent.equals(content)){
						return i;
					}
				}
			}
		}
		return -1;
	}

	private static JETASTElement getElementForJavaOffset(Map mappingPositions, int javaOffset) {
		for (Iterator iterator = mappingPositions.keySet().iterator(); iterator
		.hasNext();) {
			JETASTElement element = (JETASTElement) iterator.next();
			if(element instanceof JavaScriptlet){
				JavaScriptlet javaScriptlet = (JavaScriptlet) element;
			}
			Position pos = (Position) mappingPositions.get(element);
			if (pos.getOffset() <= javaOffset
					&& javaOffset < pos.getOffset() + pos.getLength()) {
				return element;
			}
		}

		return null;
	}


}
