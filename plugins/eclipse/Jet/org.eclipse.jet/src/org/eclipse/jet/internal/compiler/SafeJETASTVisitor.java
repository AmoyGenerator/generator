/**
 * <copyright>
 *
 * Copyright (c) 2007 IBM Corporation and others.
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
 * $Id: SafeJETASTVisitor.java,v 1.4 2008/05/22 15:07:11 pelder Exp $
 */
package org.eclipse.jet.internal.compiler;

import java.util.Map;
import java.util.regex.Matcher;

import org.eclipse.jet.BufferedJET2Writer;
import org.eclipse.jet.IWriterListener;
import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.core.parser.ast.JETASTVisitor;
import org.eclipse.jet.core.parser.ast.JavaDeclaration;
import org.eclipse.jet.core.parser.ast.JavaElement;
import org.eclipse.jet.core.parser.ast.JavaExpression;
import org.eclipse.jet.internal.core.NewLineUtil;
import org.eclipse.jet.internal.taglib.DocumentHelper;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.BadPositionCategoryException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.Position;

/**
 * A JETAST Visitor this doesn't case compile errors when used in JET templates
 */
public class SafeJETASTVisitor extends JETASTVisitor
{

  private final class IndentingWriter implements JET2Writer, BufferedJET2Writer {
    
    private final JET2Writer delegate;

    private boolean atLineStart;
    
    public IndentingWriter(JET2Writer delegate) {
      this.delegate = delegate;
      this.atLineStart = true;
      
    }

    public void addEventListener(String category, IWriterListener listener)
    {
      delegate.addEventListener(category, listener);
    }

    public void addPosition(String category, Position position)
    {
      IDocument document = getDocument();
      if(document != null) {
        try
        {
          document.addPosition(category, position);
        }
        catch (BadLocationException e)
        {
          throw new RuntimeException(e);
        }
        catch (BadPositionCategoryException e)
        {
          throw new RuntimeException(e);
        }
      }
    }

    public void addPositionCategory(String category)
    {
      IDocument document = getDocument();
      DocumentHelper.installPositionCategory(document, category);
    }

    public IDocument getDocument()
    {
      return delegate instanceof BufferedJET2Writer ? 
          (IDocument)((BufferedJET2Writer)delegate).getAdapter(IDocument.class) 
          : null;
    }

    public int getLength()
    {
      return getContentLength();
    }

    public JET2Writer getParentWriter()
    {
      return delegate.getParentWriter();
    }

    public Position[] getPositions(String category)
    {
      final IDocument document = getDocument();
      if(document != null) {
        try
        {
          return document.getPositions(category);
        }
        catch (BadPositionCategoryException e)
        {
          throw new RuntimeException(e);
        }
      }
      return new Position[0];
    }

    public JET2Writer newNestedContentWriter()
    {
      return delegate.newNestedContentWriter();
    }

    public void replace(int offset, int length, String text)
    {
      replaceContent(offset, length, text);
     }

    public void write(boolean b)
    {
      write(Boolean.valueOf(b));
    }

    public void write(char c)
    {
      write(Character.toString(c));
    }

    public void write(char[] data)
    {
      write(data == null ? "" : new String(data)); //$NON-NLS-1$
    }

    public void write(double d)
    {
      write(Double.toString(d));
    }

    public void write(float f)
    {
      write(Float.toString(f));
    }

    public void write(int i)
    {
      write(Integer.toString(i));
    }

    public void write(JET2Writer bodyContent)
    {
      throw new UnsupportedOperationException();
    }

    public void write(long l)
    {
      write(Long.toString(l));
    }

    public void write(Object obj)
    {
      write(String.valueOf(obj));
    }

    public void write(String string)
    {
      if(string == null) {
        string = ""; //$NON-NLS-1$
      }
      if(atLineStart && string.length() > 0) {
        delegate.write(indent);
        atLineStart = false;
      }
      int nextToWrite = 0;
      for(final Matcher nlMatcher = NewLineUtil.NEW_LINE_PATTERN.matcher(string);
        nlMatcher.find();) {
        writeLineText(string, nextToWrite, nlMatcher.start());
        delegate.write(string.substring(nlMatcher.start(), nlMatcher.end()));
        nextToWrite = nlMatcher.end();
        atLineStart = true;
      }
      writeLineText(string, nextToWrite, string.length());
      // atLineStart is true if this string ended with a new line
      atLineStart = nextToWrite > 0 && nextToWrite == string.length();
    }

    /**
     * @param string
     * @param start
     * @param end
     */
    private void writeLineText(String string, int start, int end)
    {
      if(start < end) {
        if(atLineStart) {
          delegate.write(indent);
        }
        delegate.write(string.substring(start, end));
      }
    }

    public IWriterListener[] getEventListeners()
    {
      return delegate.getEventListeners();
    }


    public Object getAdapter(Class adapterClass)
    {
      if(delegate instanceof BufferedJET2Writer) {
        return ((BufferedJET2Writer)delegate).getAdapter(adapterClass);
      } else {
        throw new IllegalStateException();
      }
    }

    public String getContent()
    {
      if(delegate instanceof BufferedJET2Writer) {
        return ((BufferedJET2Writer)delegate).getContent();
      } else {
        throw new IllegalStateException();
      }
    }

    public String getContent(int offset, int length)
    {
      if(delegate instanceof BufferedJET2Writer) {
        return ((BufferedJET2Writer)delegate).getContent(offset, length);
      } else {
        throw new IllegalStateException();
      }
    }

    public int getContentLength()
    {
      if(delegate instanceof BufferedJET2Writer) {
        return ((BufferedJET2Writer)delegate).getContentLength();
      } else {
        throw new IllegalStateException();
      }
    }

    public void replaceContent(int offset, int length, String text)
    {
      if(delegate instanceof BufferedJET2Writer) {
        ((BufferedJET2Writer)delegate).replaceContent(offset, length, text);
      } else {
        throw new IllegalStateException();
      }
    }

    public void setContent(String content)
    {
      if(delegate instanceof BufferedJET2Writer) {
        ((BufferedJET2Writer)delegate).setContent(content);
      } else {
        throw new IllegalStateException();
      }
    }
  }
  
  protected final JET2Context context;
  protected final JET2Writer out;
  private String indent;
  private final String stdIndent;
  private final Map positionMap;

  public SafeJETASTVisitor(JET2Context context, JET2Writer out) {
    this(context, out, "    "); //$NON-NLS-1$
  }

  public SafeJETASTVisitor(JET2Context context, JET2Writer out, String stdIndent) {
    this.context = context;
    this.out = new IndentingWriter(out);
    this.stdIndent = stdIndent;
    this.indent = ""; //$NON-NLS-1$
    this.positionMap = (Map)(context.hasVariable("positionMap") ? context.getVariable("positionMap") : null); //$NON-NLS-1$ //$NON-NLS-2$
  }

  public SafeJETASTVisitor in()
  {
    indent = indent + stdIndent;
    return this;
  }

  public SafeJETASTVisitor out()
  {
    indent = indent.substring(0, indent.length() - stdIndent.length());
    return this;
  }

  public String ident()
  {
    return indent;
  }
  
  /**
   * @param element
   */
  protected void writeAndRecord(JavaElement element)
  {
    String javaContent = element.getJavaContent();
    out.write(javaContent);
    if (positionMap != null)
    {
      int len = javaContent.length();
      int updateLen = 0;
      if(element instanceof JavaExpression){
        if(javaContent.trim().isEmpty()){
          updateLen = 6;
        }
      }
      Position position = new Position(((BufferedJET2Writer)out).getContentLength() - len - updateLen, len + updateLen);
      positionMap.put((JavaElement)element, position);
      IDocument document = (IDocument)((BufferedJET2Writer)out).getAdapter(IDocument.class);
      DocumentHelper.installPositionCategory(document, this.getClass().getName());
    }
   
  }
 
}
