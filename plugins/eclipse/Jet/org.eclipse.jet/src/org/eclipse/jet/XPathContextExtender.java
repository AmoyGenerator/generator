/*******************************************************************************
 * Copyright (c) 2005, 2009 IBM Corporation and others.
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
 * $Id$
 * /
 *******************************************************************************/

package org.eclipse.jet;


import java.text.MessageFormat;
import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.runtime.Platform;
import org.eclipse.emf.ecore.xml.type.impl.AnyTypeImpl;
import org.eclipse.jet.api.Group;
import org.eclipse.jet.api.JMR;
import org.eclipse.jet.exception.JetTemplateCode;
import org.eclipse.jet.exception.JmrException;
import org.eclipse.jet.exception.utils.ExceptionLocationUtil;
import org.eclipse.jet.internal.InternalJET2Platform;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.internal.xpath.AnnotationManagerImpl;
import org.eclipse.jet.internal.xpath.NodeSetImpl;
import org.eclipse.jet.internal.xpath.functions.StringFunction;
import org.eclipse.jet.messages.Messages;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.xpath.DefaultXPathFunctionResolver;
import org.eclipse.jet.xpath.IAnnotationManager;
import org.eclipse.jet.xpath.NamespaceContext;
import org.eclipse.jet.xpath.NodeSet;
import org.eclipse.jet.xpath.XPath;
import org.eclipse.jet.xpath.XPathException;
import org.eclipse.jet.xpath.XPathExpression;
import org.eclipse.jet.xpath.XPathFactory;
import org.eclipse.jet.xpath.XPathFunctionMetaData;
import org.eclipse.jet.xpath.XPathRuntimeException;
import org.eclipse.jet.xpath.XPathUtil;
import org.eclipse.jet.xpath.XPathVariableResolver;
import org.eclipse.jet.xpath.inspector.AddElementException;
import org.eclipse.jet.xpath.inspector.CopyElementException;
import org.eclipse.jet.xpath.inspector.ExpandedName;
import org.eclipse.jet.xpath.inspector.IElementInspector;
import org.eclipse.jet.xpath.inspector.INodeInspector;
import org.eclipse.jet.xpath.inspector.InspectorManager;
import org.eclipse.jet.xpath.inspector.InvalidChildException;
import org.eclipse.jet.xpath.inspector.SimpleElementRequiresValueException;
import org.eclipse.jet.xpath.inspector.INodeInspector.NodeKind;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;


/**
 * Context Extender that understands XPath processing.
 *
 */
public final class XPathContextExtender implements XPathVariableResolver
{

  private static String PRIVATE_CONTEXT_DATA_KEY = XPathContextExtender.class.getName();

  private static final Pattern DYNAMIC_XPATH = Pattern.compile("\\{(.*?)}"); //$NON-NLS-1$
  private static boolean DEBUG = InternalJET2Platform.getDefault().isDebugging()
  && Boolean.valueOf(Platform.getDebugOption("org.eclipse.jet/debug/xpath/compilations")).booleanValue(); //$NON-NLS-1$

  private final ContextData contextData;

  private final JET2Context context;

  private static final class ContextData
  {
    public final IAnnotationManager annotationManager = new AnnotationManagerImpl();

    public DefaultXPathFunctionResolver customFunctionResolver = null;

    public final Map knownXPathExpressions = new HashMap();

    public final Stack contextObjectStack = new Stack();
  }

  /**
   * @param context
   * @deprecated Use {@link #getInstance(JET2Context)}. This method will be made private in the near future.
   */
  public XPathContextExtender(JET2Context context)
  {
    this(context, getInstance(context).contextData);
  }

  private XPathContextExtender(JET2Context context, ContextData contextData)
  {
    this.context = context;
    this.contextData = contextData;
    XPath xp = XPathFactory.newInstance().newXPath(contextData.annotationManager);
    // Add one a custom function resolver which delegates to the resolver installed by default.
    contextData.customFunctionResolver = new DefaultXPathFunctionResolver(xp.getXPathFunctionResolver());

  }

  /**
   * Factory method for XPathContextExtenders
   * @param context the JET2Context that is extended
   * @return an XPathContextExtender
   */
  public static XPathContextExtender getInstance(JET2Context context)
  {
    if(context == null) {
      throw new NullPointerException();
    }
    XPathContextExtender ex = (XPathContextExtender)context.getPrivateData(PRIVATE_CONTEXT_DATA_KEY);
    if (ex == null)
    {
      ex = new XPathContextExtender(context, new ContextData());
      context.addPrivateData(PRIVATE_CONTEXT_DATA_KEY, ex);
    }
    return ex;
  }
  /* (non-Javadoc)
   * @see org.eclipse.jet.AbstractContextExtender#createExtendedData(org.eclipse.jet.JET2Context)
   */
  protected Object createExtendedData(JET2Context context)
  {
    final ContextData contextData = new ContextData();

    XPath xp = XPathFactory.newInstance().newXPath(contextData.annotationManager);
    // Add one a custom function resolver which delegates to the resolver installed by default.
    contextData.customFunctionResolver = new DefaultXPathFunctionResolver(xp.getXPathFunctionResolver());

    return contextData;
  }

  /* (non-Javadoc)
   * @see org.eclipse.jet.xpath.XPathVariableResolver#resolveVariable(java.lang.String)
   */
  public Object resolveVariable(String name)
  {
    try
    {
      return context.getVariable(name);
    }
    catch (JET2TagException e)
    {
      // var not defined, just return null
      return null;
    }
  }

  /**
   * Resolve the given XPath expression as a string value. Note that if the XPath expression
   * returns an empty Node set, this method returns <code>null</code>
   * @param xpathContextObject the xpath context
   * @param selectXPath the XPath expression
   * @return the string value of the XPath expression, or <code>null</code> if the expression resulted in an empty node set.
   * @throws JET2TagException if an error occurs during expression evaluation
   */
  public String resolveAsString(Object xpathContextObject, String selectXPath) throws JET2TagException
  {
    Object resultObject = resolveAsObject(xpathContextObject, selectXPath);

    String result = null;
    if(resultObject instanceof NodeSet)
    {
      if(((NodeSet)resultObject).size() > 0)
      {
        result = XPathUtil.xpathString(resultObject);
      }
    } 
    else if(resultObject != null)
    {
      result = XPathUtil.xpathString(resultObject);
    }
    return result;
  }

  /**
   * Resolve the given XPath expression as a string value. Note that if the XPath expression
   * returns an empty Node set, this method returns <code>null</code>. This is a convenience implementation of:
   * <pre>
   * resolveAsString(currentXPathContextObject(), selectXPath)
   * </pre>
   * @param selectXPath the XPath expression
   * @return the string value of the XPath expression, or <code>null</code> if the expression resulted in an empty node set.
   * @throws JET2TagException if an error occurs during expression evaluation
   * @see #resolveAsString(Object, String)
   * @since 1.0
   */
  public String resolveAsString(String selectXPath) throws JET2TagException
  {
    return resolveAsString(currentXPathContextObject(), selectXPath);
  }

  /**
   * Resolve an XPath expression to a single object. If the Xpath expression returns a collection, then
   * return the first element in the collection, or <code>null</code> if that colleciton is empty. Otherwise, the result
   * of the XPath expression is returned.
   * @param xpathContextObject the context object used to resolve relative XPath expressions
   * @param selectXPath the Xpath expression
   * @return an Object or <code>null</code>
   * @throws JET2TagException if an XPath error occurs.
   */
  public Object resolveSingle(Object xpathContextObject, String selectXPath) throws JET2TagException
  {
    try
    {
      XPathExpression expr = compileXPath(selectXPath);
      return expr.evaluateAsSingleNode(xpathContextObject);
    }
    catch (XPathException e)
    {
      throw new JET2TagException(e);
    }

  }

  /**
   * Resolve an XPath expression to a single object. This is a convenience implementation of:
   * <pre>
   * resolveSingle(currentXPathContextObject(), selectXPath)
   * </pre>
   * @param selectXPath the Xpath expression
   * @return an Object or <code>null</code>
   * @throws JET2TagException if an XPath error occurs.
   * @see #resolveSingle(Object, String)
   * @since 1.0
   */
  public Object resolveSingle(String selectXPath) throws JET2TagException
  {
    return resolveSingle(currentXPathContextObject(), selectXPath);
  }
  /**
   * @param selectXPath
   * @return
   * @throws XPathException
   */
  private XPathExpression compileXPath(String selectXPath) throws XPathException
  {
    Object result = contextData.knownXPathExpressions.get(selectXPath);
    if (result == null)
    {
      if(DEBUG) System.out.println("XPath compile of " + selectXPath); //$NON-NLS-1$
      XPath xp = XPathFactory.newInstance().newXPath(contextData.annotationManager);
      // install the custom resolver.
      Object object = context.getVariable(JMR.GROUP);
      if(object instanceof Group){
        Group group = (Group) object;
        final Document document = group.getDocument();

        if(document != null){
          xp.setNamespaceContext(new NamespaceContext()
          {
            public String getNamespaceURI(String prefix)
            {
              if(prefix != null){
                return getDocumentURIByPrefix(document, prefix);
              }
              return null;
            }
          });
        }
      }

      xp.setXPathFunctionResolver(contextData.customFunctionResolver);
      xp.setXPathVariableResolver(this);
      try
      {
        result = xp.compile(selectXPath);
        if(DEBUG) System.out.println("  compiled to " + result); //$NON-NLS-1$
      }
      catch (XPathException e)
      {
        result = e;
        if(DEBUG) System.out.println("  exception " + result); //$NON-NLS-1$
      }
      contextData.knownXPathExpressions.put(selectXPath, result);
    } else {
      if(DEBUG) System.out.println("XPath cache hit on " + selectXPath); //$NON-NLS-1$
    }

    if(result instanceof XPathExpression) {
      return (XPathExpression) result;
    }
    else
    {
      throw (XPathException) result;
    }
  }

  private String getDocumentURIByPrefix(Node node, String prefix){
    String documentURI = null;
    if(node != null && prefix != null){
      String nodePrefix = node.getPrefix();
      if(nodePrefix != null && nodePrefix.equals(prefix)){
        documentURI = node.getNamespaceURI();
      }
      if(documentURI == null){
        NodeList nodeList = node.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++)
        {
          Node child = nodeList.item(i);
          documentURI = getDocumentURIByPrefix(child, prefix);
          if(documentURI != null){
            break;
          }
        }
      }
    }
    return documentURI;
  }

  /**
   * Return the current XPath context object. If no context object has been specified
   * via {{@link #pushXPathContextObject(Object)}, then value of {@link JET2Context#getSource()}
   * is returned
   * @return the XPath context object
   */
  public Object currentXPathContextObject()
  {

    return contextData.contextObjectStack.isEmpty() ? context.getSource() : contextData.contextObjectStack.peek();
  }

  /**
   * Push a new XPath context object.
   * The XPath context object is used to resolve relative XPath expressions. 
   * @param contextObject the object that will be the new XPath context object
   * @see #currentXPathContextObject()
   * @see #popXPathContextObject()
   * 
   * @since 1.0
   */
  public void pushXPathContextObject(Object contextObject)
  {
    contextData.contextObjectStack.push(contextObject);
  }

  /**
   * Restore the previous XPath context object.
   * 
   * @return the XPath context object that was just removed
   * @throws EmptyStackException if there is no prior XPath context to restore.
   * 
   * @see #currentXPathContextObject()
   * @see #pushXPathContextObject(Object)
   * 
   * @since 1.0
   */
  public Object popXPathContextObject()
  {
    return contextData.contextObjectStack.pop();
  }


  /**
   * Resolve an XPath expression into an array of Objects.
   * @param xpathContextObject the context object for relative Xpath expressions
   * @param selectXPath the XPath expression
   * @return a possibly empty array of objects resolved by the expression
   * @throws JET2TagException if an error in the XPath expression occurs
   */
  public Object[] resolve(Object xpathContextObject, String selectXPath) throws JET2TagException
  {
    try
    {
      XPathExpression expr = compileXPath(selectXPath);
      return expr.evaluateAsNodeSet(xpathContextObject).toArray();
    }
    catch (XPathException e)
    {
      throw new JET2TagException(e);
    }
  }

  /**
   * Resolve an XPath expression into an array of Objects. This method
   * is a convenience implementation of:
   * <pre>
   * resolve(currentXPathContextObject(), selectXPath)
   * </pre>
   * 
   * @param selectXPath selectXPath the XPath expression
   * @return a possibly empty array of objects resolved by the expression
   * @throws JET2TagException if an error in the XPath expression occurs
   * @since 1.0
   */
  public Object[] resolve(String selectXPath) throws JET2TagException
  {
    return resolve(currentXPathContextObject(), selectXPath);
  }

  /**
   * Resolve an xpath expression as a boolean result according to the
   * XPath rules.
   * <p>
   * See the documentation of the XPath<a href="http://www.w3.org/TR/xpath#function-boolean">boolean</a>
   * function for complete details on how XPath results are converted to boolean values.
   * </p>
   * @param xpathContext the XPath context object
   * @param testXPath the XPath expression
   * @return <code>true</code> or <code>false</code>
   * @throws JET2TagException if an error occurs in evaluating the expression.
   */
  public boolean resolveTest(Object xpathContext, String testXPath) throws JET2TagException
  {
    try
    {
      XPathExpression expr = compileXPath(testXPath);
      return expr.evaluateAsBoolean(xpathContext);
    }
    catch (XPathException e)
    {
      throw new JET2TagException(e);
    }
  }

  /**
   * Resolve an xpath expression as a boolean result according to the
   * XPath rules. This method is a convenience implementation of:
   * <pre>
   * resolveTest(currentXPathContextObject(), textXPath)
   * </pre>
   * @param testXPath the XPath expression
   * @return <code>true</code> or <code>false</code>
   * @throws JET2TagException if an error occurs in evaluating the expression.
   * @see #resolveTest(Object, String)
   * @since 1.0
   */
  public boolean resolveTest(String textXPath) throws JET2TagException
  {
    return resolveTest(currentXPathContextObject(), textXPath);
  }

  public boolean setAttribute(Object element, String name, String bodyContent) throws JET2TagException
  {
    IElementInspector elementInspector = getElementInspector(element);

    boolean isSet = false;
    try
    {
      isSet = elementInspector.createAttribute(element, name, bodyContent);
    }
    catch (UnsupportedOperationException e)
    {
      // continue, ;
    }

    if(!isSet && contextData.annotationManager != null) {
      Object annotation = contextData.annotationManager.getAnnotationObject(element);
      elementInspector = getElementInspector(annotation);
      isSet = elementInspector.createAttribute(annotation, name, bodyContent);
    }
    return isSet;
  }

  /**
   * Resolve dynamic XPath expressions {...} within the pass value
   * @param value a string containing zero or more dynamic xpath expressions
   * @return the string with all dynamic xpath expressions resolved
   * @throws JET2TagException if an Xpath evaluation error occurs
   */
  public String resolveDynamic(String value) throws JET2TagException
  {
    Matcher matcher = DYNAMIC_XPATH.matcher(value);
    return matcher.find() ? internalResolveDynamic(value, matcher) : value;
  }

  public static String resolveDynamic(String value, JET2Context context) throws JET2TagException
  {
    Matcher matcher = DYNAMIC_XPATH.matcher(value);
    if(matcher.find()) {
      return getInstance(context).internalResolveDynamic(value, matcher);
    }
    return value;
  }

  private String internalResolveDynamic(String value, Matcher matcher)
  {
    final Object contextObject = currentXPathContextObject();
    final StringBuffer buffer = new StringBuffer(value);
    int fudgeFactor = 0;
    do {
      final String xpath = matcher.group(1);
      final String resolved = resolveAsString(contextObject, xpath);
      if (resolved == null)
      {
        String msg = JET2Messages.XPath_DynamicExpressionIsNull;
        throw new JET2TagException(MessageFormat.format(msg, new Object []{ xpath }));
      }
      final int mStart = matcher.start();
      final int mEnd = matcher.end();
      buffer.replace(fudgeFactor + mStart, fudgeFactor + mEnd, resolved);
      final int mLen = mEnd - mStart;
      fudgeFactor += resolved.length() - mLen;
    } while(matcher.find());
    return buffer.toString();
  }

  private IElementInspector getElementInspector(Object element) throws JET2TagException
  {
    final INodeInspector inspector = InspectorManager.getInstance().getInspector(element);
    if (inspector == null || inspector.getNodeKind(element) != NodeKind.ELEMENT || !(inspector instanceof IElementInspector))
    {
      throw new JET2TagException(JET2Messages.XPath_NotAnElement);
    }
    return (IElementInspector)inspector;
  }

  public Object addElement(Object parent, String name) throws JET2TagException
  {
    IElementInspector inspector = getElementInspector(parent);

    try
    {
      return inspector.addElement(parent, new ExpandedName(name), null);
    }
    catch (SimpleElementRequiresValueException e)
    {
      throw new JET2TagException(JET2Messages.XPath_UseAddTextElement);
    }
    catch (InvalidChildException e)
    {
      // cannot happend, as we're passing null as the third argument;
      throw new JET2TagException(e);
    }
    catch(UnsupportedOperationException e)
    {
      throw convertToTagException(e);
    }
  }

  public void removeElement(Object element) throws JET2TagException
  {
    IElementInspector inspector = getElementInspector(element);

    try
    {
      inspector.removeElement(element);
    }
    catch (UnsupportedOperationException e)
    {
      throw convertToTagException(e);
    }

  }

  /**
   * @param e
   * @return
   */
  private JET2TagException convertToTagException(UnsupportedOperationException e)
  {
    // FIXME: add a message for this
    return new JET2TagException(e.toString());
  }

  /**
   * Copy <code>srcElement</code> as a new element with the specified name under <code>tgtParent</code>.
   * If <code>recursive</code> is <code>true</code>, then all the contained children of <code>srcElement</code>
   * are copied, otherwise, only the element and its attributes are copied.
   * @param srcElement the element to copy
   * @param tgtParent the parent element that will contain the copy
   * @param name the name of the copied element
   * @param recursive <code>true</code> if contained chidren are to be copied, too.
   * @return the newly copied element
   * @throws JET2TagException an error occurs
   */
  public Object copyElement(Object srcElement, Object tgtParent, String name, boolean recursive) throws JET2TagException
  {
    IElementInspector inspector = getElementInspector(tgtParent);

    try
    {
      return inspector.copyElement(tgtParent, srcElement, name, recursive);
    }
    catch (CopyElementException e)
    {
      throw new JET2TagException(e.getLocalizedMessage(), e);
    }
    catch (UnsupportedOperationException e)
    {
      throw convertToTagException(e);
    }
  }

  /**
   * Create a new text (simple) element whose content is set to <code>bodyContent</code>.
   * @param parentElement the parent of the new element.
   * @param name the name of the new element.
   * @param bodyContent the content.
   * @return the new Element.
   * @throws JET2TagException if the element cannot be added.
   */
  public Object addTextElement(Object parentElement, String name, String bodyContent) throws JET2TagException
  {
    return addTextElement(parentElement, name, bodyContent, false);
  }

  /**
   * Create a new text (simple) element whose content is set to <code>bodyContent</code>.
   * @param parentElement the parent of the new element.
   * @param name the name of the new element.
   * @param bodyContent the content.
   * @param asCData if <code>true</code>, create the element as a CDATA section, of possible
   * @return the new element.
   * @throws JET2TagException if the element cannot be added.
   */
  public Object addTextElement(Object parentElement, String name, String bodyContent, boolean asCData) throws JET2TagException
  {
    IElementInspector inspector = getElementInspector(parentElement);

    try
    {
      return inspector.addTextElement(parentElement, name, bodyContent, asCData);
    }
    catch (AddElementException e)
    {
      throw new JET2TagException(e.getLocalizedMessage(), e);
    }
    catch (UnsupportedOperationException e) {
      throw convertToTagException(e);
    }

  }
  /**
   * Resolve the XPath expression, returning an object. Unlike the other resolve methods, this
   * method performs no type conversions on the XPath results.
   * @param contextObject the context object to which the XPath expression is relative.
   * @param selectXPath the XPath expression
   * @return the result of the expression evaluation.
   * @throws JET2TagException if an error occurs.
   */
  public Object resolveAsObject(Object contextObject, String selectXPath)
  {
    try
    {
      XPathExpression expr = compileXPath(selectXPath);
      return expr.evaluate(contextObject);
    }
    catch (XPathException e)
    {
      String templathPath = getProjectPath(context.getTemplatePath());

      String jetLocation = ExceptionLocationUtil.getExceptionJetLocation(e, context, templathPath);

      int printLine = 0;
      CompileMessage compileMessage = context.getCompileMessage();
      if(compileMessage != null){
        printLine = ExceptionLocationUtil.getStrceIndex(e, compileMessage.getCompiledClass()) + 1;
      }
      JmrException jmrException = new JmrException(e, true, printLine, JetTemplateCode.XPATH_ERROR, jetLocation)
      .set(Messages.XPathContextExtender_Xpath_Error, Messages.XPathContextExtender_Xpath_Advice);
      context.addException(jmrException);
      //throw new JET2TagException(e.getMessage(), e);
    }
    catch(XPathRuntimeException e) {
      String templathPath = getProjectPath(context.getTemplatePath());

      String jetLocation = ExceptionLocationUtil.getExceptionJetLocation(e, context, templathPath);

      int printLine = 0;
      CompileMessage compileMessage = context.getCompileMessage();
      if(compileMessage != null){
        printLine = ExceptionLocationUtil.getStrceIndex(e, compileMessage.getCompiledClass()) + 1;
      }
      JmrException jmrException = new JmrException(e, true, printLine, JetTemplateCode.XPATH_RUNTIME_ERROR, jetLocation);
      context.addException(jmrException);
    }
    return null;
  }

  @Deprecated
  public Object resolveAsObject(Object contextObject, String selectXPath, int line)
  {
    try
    {
      XPathExpression expr = compileXPath(selectXPath);
      return expr.evaluate(contextObject);
    }
    catch (XPathException e)
    {
      String templatePath = getProjectPath(context.getTemplatePath());
      String jetLocation = ExceptionLocationUtil.getJetLocation(templatePath, line);

      int printLine = 0;
      CompileMessage compileMessage = context.getCompileMessage();
      if(compileMessage != null){
        printLine = ExceptionLocationUtil.getStrceIndex(e, compileMessage.getCompiledClass()) + 1;
      }
      JmrException jmrException = new JmrException(e, false, JetTemplateCode.XPATH_ERROR, jetLocation)
      .set(Messages.XPathContextExtender_Xpath_Error, Messages.XPathContextExtender_Xpath_Advice);
      context.addException(jmrException);
      //throw new JET2TagException(e.getMessage(), e);
    }
    catch(XPathRuntimeException e) {
      String templatePath = getProjectPath(context.getTemplatePath());
      String jetLocation = ExceptionLocationUtil.getJetLocation(templatePath, line);

      int printLine = 0;
      CompileMessage compileMessage = context.getCompileMessage();
      if(compileMessage != null){
        printLine = ExceptionLocationUtil.getStrceIndex(e, compileMessage.getCompiledClass()) + 1;
      }
      JmrException jmrException = new JmrException(e, false, JetTemplateCode.XPATH_RUNTIME_ERROR, jetLocation);
      context.addException(jmrException);
    }
    return null;
  }

  /**
   * Resolve the XPath expression, returning an object. Unlike the other resolve methods, this
   * method performs no type conversions on the XPath results. This is a convenience implementation of:
   * <pre>
   * resolveAsObject(currentXPathContextObject(), selectXPath);
   * </pre>
   * @param selectXPath the XPath expression
   * @return the result of the expression evaluation.
   * @throws JET2TagException if an error occurs.
   * @see #resolveAsObject(Object, String)
   * @since 1.0
   */
  public Object resolveAsObject(String selectXPath) throws JET2TagException
  {
    return resolveAsObject(currentXPathContextObject(), selectXPath);
  }

  /**
   * Return the value of the named attribute on the passed element.
   * @param element the element containing the attribute
   * @param attributeName the attribute name
   * @return the attribute value
   * @throws JET2TagException
   */
  public Object getAttributeValue(Object element, String attributeName) throws JET2TagException
  {
    IElementInspector inspector = getElementInspector(element);

    final Object namedAttribute = inspector.getNamedAttribute(element, new ExpandedName(attributeName));
    INodeInspector attrInspector = InspectorManager.getInstance().getInspector(namedAttribute);
    if(attrInspector != null) {
      return attrInspector.stringValueOf(namedAttribute);
    } else {
      throw new JET2TagException(MessageFormat.format(JET2Messages.XPath_NoValueForAttribute, new Object[] {attributeName}));
    }
  }

  /**
   * Remove the named attribute from the specified element.
   * @param element the element containing the attribute
   * @param attributeName the attribute to remove
   * @throws JET2TagException if the attribute cannot be removed (because it is required), 
   * or if <code>element</code> is not a recognized element.
   */
  public void removeAttribute(Object element, String attributeName) throws JET2TagException
  {
    IElementInspector inspector = getElementInspector(element);
    try
    {
      inspector.removeAttribute(element, attributeName);
    }
    catch (UnsupportedOperationException e)
    {
      throw convertToTagException(e);
    }

  }

  /**
   * Return the string value of the passed object. This equivalent to calling the XPath string() function
   * on the passed object.
   * @param object the object to examine.
   * @return the string value. 
   * @throws JET2TagException if an error occurs.
   */
  public String getContent(Object object) throws JET2TagException {
    return StringFunction.evaluate(object);
  }

  /**
   * Add the passed list of XPath function definitions to the XPath processor.
   * @param functionData possible empty array of {@link XPathFunctionMetaData} instances.
   */
  public void addCustomFunctions(XPathFunctionMetaData functionData[]) 
  {
    final DefaultXPathFunctionResolver resolver = contextData.customFunctionResolver;
    for (int i = 0; i < functionData.length; i++)
    {
      resolver.addFunction(functionData[i]);
    }
  }
 
  /**
   * add by xinyu
   * @param templatePath
   * @return
   */
  public static String getProjectPath(String templatePath){
    if(templatePath != null && templatePath.startsWith("../")){
      templatePath = templatePath.substring(3);
    }
    return templatePath;
  }
}
