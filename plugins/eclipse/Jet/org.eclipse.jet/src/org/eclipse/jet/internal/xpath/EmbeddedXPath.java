/**
 * <copyright>
 *
 * Copyright (c) 2009 IBM Corporation and others.
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
 * $Id: EmbeddedXPath.java,v 1.1 2009/04/06 17:55:12 pelder Exp $
 */
package org.eclipse.jet.internal.xpath;


import org.eclipse.jet.JET2Context;
import org.eclipse.jet.XPathContextExtender;
import org.eclipse.jet.core.expressions.IEmbeddedExpression;
import org.eclipse.jet.internal.core.expressions.IEmbeddedExpressionScanner;
import org.eclipse.jet.internal.core.expressions.IEmbeddedLanguage;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.osgi.util.NLS;


/**
 * An implementation of {@link IEmbeddedLanguage} for JET's built-in XPath Language implementation
 */
public class EmbeddedXPath implements IEmbeddedLanguage
{

  /**
     * An {@link IEmbeddedExpressionScanner} for JET's build-in XPath language.
     */
  public static class XPathScanner implements IEmbeddedExpressionScanner
  {
    /**
     * Define the behavior of the scanner's state.
     */
    interface State {
      
      /**
       * Return the {@link Scanner} into which the {@link XPathScanner} must transition on encountering the passed character.
       * @param ch
       * @return
       */
      State nextState(int ch);
      
      /**
       * Return whether the state represents the scanner in an XPath quoted string.
       * @return <code>true</code> if the state indicates the scanner is positioned within an XPath string.
       */
      boolean isInXPathString();
      
    }
    
    /**
     * A {@link Scanner} representing non-string content.
     */
    private static State NOT_IN_STRING = new State() {

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#isInXPathString()
       */
      public boolean isInXPathString()
      {
        return false;
      }

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#nextState(int)
       */
      public State nextState(int ch)
      {
        if(ch == '\'') {
          return SINGLE_QUOTE_STRING;
        } else if(ch == '"') {
          return DOUBLE_QUOTE_STRING;
        }
        return this;
      }
      
    };
    
    /**
     * A {@link Scanner} representing the scanner within a string surrounded by single quotes.
     */
    private static State SINGLE_QUOTE_STRING = new State() {

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#isInXPathString()
       */
      public boolean isInXPathString()
      {
        return true;
      }

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#nextState(int)
       */
      public State nextState(int ch)
      {
        return ch == '\'' ? POSSIBLE_ESCAPED_SINGLE_QUOTE : this;
      }};
      
    /**
     * A {@link Scanner} representing the scanner within a singly-quoted string that has just encountered
     * a single quote. The Scanner decides between the start of an escaped single quote (two single quotes) or the
     * end of the quoted string.
     */
    private static State POSSIBLE_ESCAPED_SINGLE_QUOTE = new State() {

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#isInXPathString()
       */
      public boolean isInXPathString()
      {
        return true;
      }

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#nextState(int)
       */
      public State nextState(int ch)
      {
        return ch == '\'' ? SINGLE_QUOTE_STRING : NOT_IN_STRING;
      }
      
    };
    
    /**
     * A {@link Scanner} representing the scanner within a string surrounded by double quotes.
     */
    private static State DOUBLE_QUOTE_STRING = new State() {

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#isInXPathString()
       */
      public boolean isInXPathString()
      {
        return true;
      }

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#nextState(int)
       */
      public State nextState(int ch)
      {
        return ch == '"' ? POSSIBLE_ESCAPED_DOUBLE_QUOTE : this;
      }};
      
      /**
       * A {@link Scanner} representing the scanner within a doubly-quoted string that has just encountered
       * a double quote. The Scanner decides between the start of an escaped double quote (two double quotes) or the
       * end of the quoted string.
       */
    private static State POSSIBLE_ESCAPED_DOUBLE_QUOTE = new State() {

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#isInXPathString()
       */
      public boolean isInXPathString()
      {
        return true;
      }

      /* (non-Javadoc)
       * @see org.eclipse.jet.internal.xpath.EmbeddedXPath.XPathScanner.State#nextState(int)
       */
      public State nextState(int ch)
      {
        return ch == '"' ? DOUBLE_QUOTE_STRING : NOT_IN_STRING;
      }
      
    };
    
    /**
     * The state of the scanner. Initially, the scanner is NOT withing an XPath string.
     */
    private State state = NOT_IN_STRING;
    
    /* (non-Javadoc)
     * @see org.eclipse.jet.internal.core.parser.IEmbeddedExpressionScanner#setChar(int)
     */
    public void setNextChar(int ch)
    {
      state = state.nextState(ch);
    }

    /* (non-Javadoc)
     * @see org.eclipse.jet.internal.core.parser.IEmbeddedExpressionScanner#ignoreChar()
     */
    public boolean ignoreChar()
    {
      return state.isInXPathString();
    }

  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.core.IEmbeddedExpressionFactory#getFactory(org.eclipse.jet.JET2Context)
   */
  public IEmbeddedExpression getExpression(final String expression)
  {
    return new IEmbeddedExpression()
      {

        /* (non-Javadoc)
         * @see org.eclipse.jet.IEmbeddedExpression#asString()
         */
        public String evalAsString(JET2Context context)
        {
          String result = null;
          result = XPathContextExtender.getInstance(context).resolveAsString(expression);
          if (result == null)
          {
            final String fullExpr = "${" + expression + "}"; //$NON-NLS-1$ //$NON-NLS-2$
            throw new JET2TagException(NLS.bind("The expression {0} returned no value", fullExpr));
          }
          return result == null ? "" : result; //$NON-NLS-1$
        }
        
        /* (non-Javadoc)
         * @see java.lang.Object#toString()
         */
        public String toString()
        {
          return "${" + expression +"}";  //$NON-NLS-1$//$NON-NLS-2$
        }

        /* (non-Javadoc)
         * @see org.eclipse.jet.IEmbeddedExpression#isText()
         */
        public boolean isText()
        {
          return false;
        }
      };
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.internal.core.parser.IEmbeddedLanguage#getScanner()
   */
  public IEmbeddedExpressionScanner getScanner()
  {
    return new XPathScanner();
  }

}
