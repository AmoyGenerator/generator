/**
 * <copyright>
 *
 * Copyright (c) 2007, 2009 IBM Corporation and others.
 * All rights reserved.   This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: 
 *   IBM - Initial API and implementation
 *
 */
package org.eclipse.jet.internal.runtime.model.jdt;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.jdt.core.ICompilationUnit;
import org.eclipse.jdt.core.IJavaElement;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTNode;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jet.internal.xpath.inspectors.jdt.ASTNodeDocumentRoot;
import org.eclipse.jet.runtime.model.IModelLoader;

/**
 * Load a java file as an JDT Abstract syntax tree.
 * 
 */
public class JavaLoader implements IModelLoader {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#canLoad(java.lang.String)
	 */
	public boolean canLoad(String kind) {

		return "java".equals(kind); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL)
	 */
	public Object load(URL modelUrl) throws IOException {
		return load(modelUrl, "java"); //$NON-NLS-1$
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jet.runtime.model.IModelLoader#load(java.net.URL,
	 * java.lang.String)
	 */
	public Object load(URL modelUrl, String kind) throws IOException {
		if ("platform".equals(modelUrl.getProtocol())) { //$NON-NLS-1$
			String path = modelUrl.getPath();
			if (path != null && path.startsWith("/resource/")) { //$NON-NLS-1$
				final IPath ipath = new Path(path).removeFirstSegments(1);
				final IResource resource = ResourcesPlugin.getWorkspace()
						.getRoot().findMember(ipath);
				if (resource != null) {
					final IJavaElement javaElement = JavaCore.create(resource);
					if (javaElement instanceof ICompilationUnit) {
						final ASTParser astParser = ASTParser
								.newParser(AST.JLS3);
						astParser.setSource((ICompilationUnit) javaElement);
						final ASTNode astNode = astParser
								.createAST(new NullProgressMonitor());
						try {
							final String contents = ((ICompilationUnit) javaElement)
									.getBuffer().getContents();
							return new ASTNodeDocumentRoot(astNode, contents);
						} catch (JavaModelException e) {
							// ignore, ... just fall through;
						}
					}
				}
			}
		}
		InputStreamReader reader = new InputStreamReader(
				new BufferedInputStream(modelUrl.openStream()));
		StringBuffer buffer = new StringBuffer(512);
		try {
			char[] cbuf = new char[512];
			int i;
			while ((i = reader.read(cbuf)) > 0) {
				buffer.append(cbuf, 0, i);
			}

		} finally {
			try {
				reader.close();
			} catch (IOException e) {
				// ignore close exceptiosn
			}
		}
		return loadFromString(buffer.toString(), kind);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.jet.runtime.model.IModelLoader#loadFromString(java.lang.String
	 * , java.lang.String)
	 */
	public Object loadFromString(String serializedModel, String kind)
			throws IOException {
		ASTParser astParser = ASTParser.newParser(AST.JLS3);

		astParser.setSource(serializedModel.toCharArray());

		astParser.setKind(getKind(kind));

		ASTNode astNode = astParser.createAST(new NullProgressMonitor());
		return new ASTNodeDocumentRoot(astNode, serializedModel);
	}

	/**
	 * @param kind
	 * @return
	 */
	private int getKind(String kind) {
		if ("classbodydeclarations".equalsIgnoreCase(kind)) { //$NON-NLS-1$
			return ASTParser.K_CLASS_BODY_DECLARATIONS;
		} else if ("expression".equals(kind)) { //$NON-NLS-1$
			return ASTParser.K_EXPRESSION;

		} else if ("statements".equals(kind)) { //$NON-NLS-1$
			return ASTParser.K_STATEMENTS;
		} else {
			return ASTParser.K_COMPILATION_UNIT;
		}

	}

}
