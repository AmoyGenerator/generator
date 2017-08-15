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
 * $Id: JET2Context.java,v 1.4 2009/04/06 17:55:06 pelder Exp $
 * /
 *******************************************************************************/

package org.eclipse.jet;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;

import org.eclipse.jet.core.expressions.EmbeddedExpressionFactory;
import org.eclipse.jet.core.expressions.IEmbeddedExpression;
import org.eclipse.jet.exception.JetTemplateCode;
import org.eclipse.jet.exception.JmrException;
import org.eclipse.jet.exception.utils.ExceptionLocationUtil;
import org.eclipse.jet.internal.l10n.JET2Messages;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.taglib.TagFactory;
import org.eclipse.jet.taglib.TagInfo;

/**
 * Define the execution context for a JET2 transform or template.
 * 
 */
public final class JET2Context {

	/**
	 * Protocol for a listener to the {@link JET2Context} log.
	 */
	public interface LogListener {
		public abstract void log(ContextLogEntry entry);
	}

	private Object source;

	private final List logEntries = new ArrayList();

	/**
	 * Use a LinkedHashSet to ensure listener uniqueness and to preserve order
	 * of addition
	 */
	private final Set logListeners = new LinkedHashSet();

	private final Map extendedContextData = new HashMap();

	private final Map privateData = new HashMap();

	private final Map sessionVariables = new HashMap();

	private TagFactory tagFactory = null;

	private final Map globalVariables = new HashMap();

	private String templatePath = ""; //$NON-NLS-1$

	private String jetBundleId;

	private List exceptions = new ArrayList();

	private CompileMessage compileMessage;

	private Map<TagInfo, Map> tagVariables = new HashMap<TagInfo, Map>();

	private Map<TagInfo, Object> tagCollection = new HashMap<TagInfo, Object>();

//	private List<StackTraceElement[]> setVarStacks = new ArrayList<StackTraceElement[]>();
	
	/**
	 * Create a JET2 context with the specified source argument and the
	 * specified variables.
	 * 
	 * @param source
	 *            the source object
	 * @param variables
	 *            A map <String,Object> of variable names to the object values.
	 */
	public JET2Context(Object source, Map variables) {
		this.source = source;
		globalVariables.putAll(variables);
	}

	/**
	 * Create a JET2 context with the specified source argument and no
	 * variables.
	 * <p>
	 * This is exactly equivalent to:
	 * 
	 * <pre>
	 * JET2Context(source, Collections.EMPTY_MAP)
	 * </pre>
	 * 
	 * @param source
	 *            the source object
	 */
	public JET2Context(Object source) {
		this(source, Collections.EMPTY_MAP);
	}

	/**
	 * Set the source object for the transformation
	 * 
	 * @param source
	 */
	public void setSource(Object source) {
		this.source = source;
	}

	/**
	 * Return the source object for the transformation.
	 * 
	 * @return the source object
	 */
	public Object getSource() {
		return source;
	}

	/**
	 * @param severity
	 * @param templatePath
	 *            TODO
	 * @param tagInfo
	 * @param throwable
	 */
	private void log(int severity, String templatePath, TagInfo tagInfo,
			String message, Throwable throwable) {
		final ContextLogEntry.Builder builder = new ContextLogEntry.Builder(
				severity);
		if (templatePath != null) {
			builder.templatePath(templatePath);
		}
		if (tagInfo != null) {
			builder.tagInfo(tagInfo);
		}
		if (message != null) {
			builder.message(message);
		}
		if (throwable != null) {
			builder.exception(throwable);
		}
		final ContextLogEntry logEntry = builder.build();

		logEntries.add(logEntry);

		for (Iterator i = logListeners.iterator(); i.hasNext();) {
			LogListener listener = (LogListener) i.next();
			listener.log(logEntry);

		}
	}

	/**
	 * Add a listener to context logging entries. Adding the same listener more
	 * than once has no effect.
	 * 
	 * @param listener
	 *            a log listener
	 */
	public void addLogListener(LogListener listener) {
		logListeners.add(listener);
	}

	/**
	 * Remove a previously registerd listener from the context log. Attempting
	 * to remove an listener not previously registered with
	 * {@link #addLogListener(org.eclipse.jet.JET2Context.LogListener)} has no
	 * effect.
	 * 
	 * @param listener
	 *            a log listener
	 */
	public void removeLogListener(LogListener listener) {
		logListeners.remove(listener);
	}

	/**
	 * Return the id of the JET Bundle defining the current template. Used in
	 * generating error messages.
	 * 
	 * @return the JET Bundle id, or <code>null</code> if not defined.
	 * @see #setJETBundleId(String)
	 */
	public String getJETBundleId() {
		return jetBundleId;
	}

	/**
	 * Set the id of the JET Bundle defining the current template. Used in
	 * generating error messages. If not set, then the ID of the JET plugin is
	 * used.
	 * 
	 * @param jetBundleId
	 *            the JET Bundle ide.
	 */
	public void setJETBundleId(String jetBundleId) {
		this.jetBundleId = jetBundleId;

	}

	// private void log(ExecutionLogEntry entry) {
	// executionLog.add(entry);
	// }

	/**
	 * Log an informational message
	 * 
	 * @param message
	 */
	// Used once: LogTag.doFunction()
	public void logInfo(String message) {
		log(ContextLogEntry.INFO, getTemplatePath(), null, message,
				(Throwable) null);
	}

	/**
	 * Return the path for the executing template. This is used in creating
	 * error messages.
	 * 
	 * @return the template path or <code>null</code> if no templatePath is
	 *         defined.
	 * @see #setTemplatePath(String)
	 */
	public String getTemplatePath() {
		return templatePath;
	}

	/**
	 * Set the templatePath. The templatePath is used in generating error
	 * messages.
	 * 
	 * @param templatePath
	 *            the template path or <code>null</code> to indicate no
	 *            executing template.
	 */
	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	/**
	 * Log a warning message
	 * 
	 * @param message
	 */
	// Used once: LogTag.doFunction()
	public void logWarning(String message) {
		log(ContextLogEntry.WARNING, getTemplatePath(), null, message,
				(Throwable) null);
	}

	/**
	 * Log an error message
	 * 
	 * @param message
	 */
	// Used once: LogTag.doFunction()
	public void logError(String message) {
		log(ContextLogEntry.ERROR, getTemplatePath(), null, message, null);
	}

	/**
	 * Log an exeception that occurred during execution
	 * 
	 * @param e
	 */
	// Used once: TransformContextExtender.commit()
	public void logError(Throwable e) {
		log(ContextLogEntry.ERROR, getTemplatePath(), null, null, e);
	}

	/**
	 * Log an exception that occured during execution, along with a message.
	 * 
	 * @param message
	 * @param e
	 * @deprecated Please don't use, will be removed...
	 */
	// Never used!
	public void logError(String message, Throwable e) {
		log(ContextLogEntry.ERROR, getTemplatePath(), null, message, e);
	}

	public ContextLogEntry getLogEntries() {
		final ContextLogEntry[] entries = (ContextLogEntry[]) logEntries
		.toArray(new ContextLogEntry[0]);
		final ContextLogEntry.Builder builder = new ContextLogEntry.Builder(
				entries);

		switch (builder.getSeverity()) {
		case ContextLogEntry.OK:
			builder.message(JET2Messages.JET2Context_SuccessfulExecution);
			break;
		case ContextLogEntry.INFO:
			builder.message(JET2Messages.JET2Context_SuccessfulWithMessages);
			break;
		case ContextLogEntry.WARNING:
			builder.message(JET2Messages.JET2Context_SuccessfulWithWarnings);
			break;
		case ContextLogEntry.ERROR:
			builder.message(JET2Messages.JET2Context_ErrorsInExecution);
			break;
		case ContextLogEntry.CANCEL:
			builder.message(JET2Messages.JET2Context_ExecutionCancelled);
			break;
		}

		return builder.build();
	}

	/**
	 * Log an error from the specified tag.
	 * 
	 * @param tagInfo
	 * @param message
	 *            the error message to display, or <code>null</code>
	 * @param exception
	 */
	// Used 3 times: TagSafeRunnable.handleException() x 2,
	// tagFactoryImpl.createTagElement(),
	public void logError(TagInfo tagInfo, String message, Throwable exception) {
		log(ContextLogEntry.ERROR, getTemplatePath(), tagInfo, message,
				exception);
	}

	private String getContextExtenderId(Class clazz) {
		return clazz.getName();
	}

	/**
	 * Test whether the context has an extender of the pass class.
	 * 
	 * @param extenderClass
	 *            the extender class
	 * @return <code>true</code> if the context has a registered extender of the
	 *         passed class.
	 */
	public boolean hasContextExtender(Class extenderClass) {
		return extendedContextData
		.containsKey(getContextExtenderId(extenderClass));
	}

	/**
	 * Register a context extender class and its data.
	 * <P>
	 * This method is not normally called by clients. It is intended for use by
	 * {@link AbstractContextExtender#AbstractContextExtender(JET2Context)}.
	 * </P>
	 * 
	 * @param extenderClass
	 *            the extender class
	 * @param extenderData
	 *            the data to be associated with the class
	 * @throws IllegalStateException
	 *             if <code>extenderClass</code> has already been registered on
	 *             this context.
	 * @deprecated Use {@link #addPrivateData(String, Object)} instead.
	 */
	public void registerContextExtender(Class extenderClass, Object extenderData) {
		String extenderId = getContextExtenderId(extenderClass);
		if (extendedContextData.containsKey(extenderClass)) {
			throw new IllegalStateException(extenderId + "already registered"); //$NON-NLS-1$
		}

		extendedContextData.put(extenderId, extenderData);
	}

	/**
	 * Return the context extension data for the passed class, or null if the
	 * extender class has no associated data, or if <code>extenderClass</code>
	 * is not registered on the context.
	 * <P>
	 * This method is not normally called by clients. It is intended for use by
	 * {@link AbstractContextExtender#getExtendedData()}.
	 * </P>
	 * 
	 * @param extenderClass
	 *            the context extender class.
	 * @return the associated data or <code>null</code>.
	 * @deprecated Use {@link #getPrivateData(String)} instead.
	 */
	public Object getContextExtenderData(Class extenderClass) {
		return extendedContextData.get(getContextExtenderId(extenderClass));
	}

	/**
	 * Return private data associated with the key.
	 * 
	 * @param key
	 *            a private data key.
	 * @return the private data or <code>null</code> if not data is associate
	 *         with the key.
	 * @since 0.9.0
	 */
	public Object getPrivateData(String key) {
		return privateData.get(key);
	}

	/**
	 * Add private data to the context.
	 * 
	 * @param key
	 *            the key for the private data
	 * @param value
	 *            the data value
	 * @throws IllegalStateException
	 *             if <code>key</code> has already been used to add private
	 *             data.
	 * @throws NullPointerException
	 *             if <code>value</code> or <code>key</code> is
	 *             <code>null</code>.
	 * @see #getPrivateData(String)
	 */
	public void addPrivateData(String key, Object value) {
		if (privateData.containsKey(key)) {
			throw new IllegalStateException();
		}
		if (key == null || value == null) {
			throw new NullPointerException();
		}
		privateData.put(key, value);
	}

	/**
	 * Remove private data associated with the key. Quietly succeeds there is no
	 * private data for the key.
	 * 
	 * @param key
	 *            the key for the private data
	 */
	public void removePrivateData(String key) {
		privateData.remove(key);
	}

	/**
	 * Log an error on the pass template
	 * 
	 * @param templatePath
	 * @param tagInfo
	 * @param message
	 * @param e
	 */
	public void logError(String templatePath, TagInfo tagInfo, String message,
			Throwable e) {
		log(ContextLogEntry.ERROR, templatePath, tagInfo, message, e);

	}

	private static final Pattern validVariableNamePattern = Pattern
	.compile("(?:_|\\p{L})(?:_|-|\\.|\\p{L}|\\d)*"); //$NON-NLS-1$

	/**
	 * Assigne or create a variable, and set its value.
	 * 
	 * @param var
	 *            the variable name. Cannot be <code>null</code>.
	 * @param value
	 *            the variable value.
	 */
	public void setVariable(String var, Object value){
		if (!validVariableNamePattern.matcher(var).matches()) {
			JET2TagException jet2TagException = new JET2TagException(MessageFormat.format(
					JET2Messages.JET2Context_InvalidVariableName,
					new Object[] { var }));
			String jetLocation = ExceptionLocationUtil.getExceptionJetLocation(jet2TagException, this, templatePath);
			JmrException jmrException = new JmrException(jet2TagException, true, 2, JetTemplateCode.JET2CONTEXT_INVALID_VARIABLE_NAME, jetLocation);
			addException(jmrException);
		}else{
			globalVariables.put(var, value);
//			Throwable t = new Throwable();
//			StackTraceElement[] stackElements = t.getStackTrace();
//			setVarStacks.add(stackElements);
		}
	}

	public void setSessionVariable(String var, Object value){
		if (!validVariableNamePattern.matcher(var).matches()) {
			JET2TagException jet2TagException = new JET2TagException(MessageFormat.format(
					JET2Messages.JET2Context_InvalidVariableName,
					new Object[] { var }));
			String jetLocation = ExceptionLocationUtil.getExceptionJetLocation(jet2TagException, this, templatePath);
			JmrException jmrException = new JmrException(jet2TagException, false, JetTemplateCode.JET2CONTEXT_INVALID_VARIABLE_NAME, jetLocation);
			addException(jmrException);
			return;
		}else{
			sessionVariables.put(var, value);
		}
	}

	@Deprecated
	public void setVariable(String var, Object value, int line){
		if (!validVariableNamePattern.matcher(var).matches()) {
			JET2TagException jet2TagException = new JET2TagException(MessageFormat.format(
					JET2Messages.JET2Context_InvalidVariableName,
					new Object[] { var }));
			String jetLocation = ExceptionLocationUtil.getJetLocation(templatePath, line);
			JmrException jmrException = new JmrException(jet2TagException, false, JetTemplateCode.JET2CONTEXT_INVALID_VARIABLE_NAME, jetLocation);
			addException(jmrException);
		}else{
			globalVariables.put(var, value);
		}
	}

	@Deprecated
	public void setSessionVariable(String var, Object value, int line){
		if (!validVariableNamePattern.matcher(var).matches()) {
			JET2TagException jet2TagException = new JET2TagException(MessageFormat.format(
					JET2Messages.JET2Context_InvalidVariableName,
					new Object[] { var }));
			String jetLocation = ExceptionLocationUtil.getJetLocation(templatePath, line);
			JmrException jmrException = new JmrException(jet2TagException, true, 2, JetTemplateCode.JET2CONTEXT_INVALID_VARIABLE_NAME, jetLocation);
			addException(jmrException);
			return;
		}else{
			sessionVariables.put(var, value);
		}
	}

	/**
	 * Return the value of a context variable
	 * 
	 * @param var
	 *            the variable name
	 * @return the value of the variable
	 * @throws JET2TagException
	 *             if the variable does not exist.
	 */
	public Object getVariable(String var) throws JET2TagException {
		//		if (!hasVariable(var)) {
		//			String msg = JET2Messages.JET2Context_VariableNotFound;
		//			throw new JET2TagException(MessageFormat.format(msg,
		//					new Object[] { var }));
		//		}
		return globalVariables.get(var);
	}

	public Object getSessionVariable(String var){
		return sessionVariables.get(var);
	}

	public Object removeSessionVariable(String var){
		return sessionVariables.remove(var);
	}

	/**
	 * Return a map of all variables currently defined in the context. The map
	 * is a copy of the variables maintained by the context; changes to the map
	 * have no affect on the context.
	 * 
	 * @return a Map of variables, where the key is a variable name, and the
	 *         value is the variable value.
	 */
	public Map getVariables() {
		return new HashMap(globalVariables);
	}

	public Map getSessionVariables() {
		return new HashMap(sessionVariables);
	}

	/**
	 * Remove a variable
	 * 
	 * @param var
	 *            the variable name
	 */
	public void removeVariable(String var) throws JET2TagException {
		globalVariables.remove(var);
	}

	/**
	 * Test whether a variable is defined
	 * 
	 * @param var
	 *            the variable name
	 * @return <code>true</code> if defined, <code>false</code> otherwise.
	 */
	public boolean hasVariable(String var) {
		return globalVariables.containsKey(var);
	}

	public boolean hasSessionVariable(String var) {
		return sessionVariables.containsKey(var);
	}

	/**
	 * @return the tagFactory
	 */
	public final TagFactory getTagFactory() {
		return tagFactory;
	}

	/**
	 * @param tagFactory
	 *            the tagFactory to set
	 */
	public final void setTagFactory(TagFactory tagFactory) {
		this.tagFactory = tagFactory;
	}

	/**
	 * Extract a list of variables from the context
	 * 
	 * @param variableNames
	 *            a comma separated list of variables. May be <code>null</code>.
	 * @return a Map keyed by variable name. Will be <code>null</code> if
	 *         <code>variableNames</code> is <code>null</code>.
	 * @throws JET2TagException
	 *             if <code>variableNames</code> contains an invalid variable
	 *             name.
	 */
	public Map extractVariables(String variableNames) throws JET2TagException {
		Map savedVariableValues = null;
		if (variableNames != null) {
			savedVariableValues = new HashMap();
			for (StringTokenizer tokenizer = new StringTokenizer(variableNames,
			","); tokenizer.hasMoreTokens();) { //$NON-NLS-1$
				String varName = tokenizer.nextToken();
				varName = varName.trim();
				savedVariableValues.put(varName, getVariable(varName));
			}
		}
		return savedVariableValues;
	}

	/**
	 * Restore variables in the passed map to the context.
	 * 
	 * @param savedVariableValues
	 *            a Map keyed by variable name. If <code>null</code> the method
	 *            does nothing.
	 * @throws JET2TagException
	 *             if a variable name is invalid
	 */
	public void restoreVariables(Map savedVariableValues)
	throws JET2TagException {
		if (savedVariableValues != null) {
			for (Iterator i = savedVariableValues.entrySet().iterator(); i
			.hasNext();) {
				Map.Entry entry = (Map.Entry) i.next();
				setVariable((String) entry.getKey(), entry.getValue());
			}
		}
	}

	/**
	 * Set the context variables to only the variables in variablesToPass
	 * 
	 * @param variablesToPass
	 *            a non-null map keyed by variable name.
	 * @throws JET2TagException
	 *             if a variable name is invalid
	 */
	@Deprecated
	public void setVariables(Map variablesToPass) throws JET2TagException {
		globalVariables.clear();
		for (Iterator i = variablesToPass.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String varName = (String) entry.getKey();
			setVariable(varName, entry.getValue());
		}
	}

	public void setSessionVariables(Map sessionVars){
		sessionVariables.clear();
		for (Iterator i = sessionVars.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String varName = (String) entry.getKey();
			setSessionVariable(varName, entry.getValue());
		}
	}

	private final EmbeddedExpressionFactory expressionFactory = EmbeddedExpressionFactory
	.newInstance();

	/**
	 * Evaluate the embedded expression found at the given line and column and return
	 * the expression result as a string.
	 * Should an error occur during evaluation, an appropriate error is logged in the
	 * context's log.
	 * @param expression the embedded expression, including the initial ${ and } characters
	 * @param line the template line at which the expression starts
	 * @param col the template column at which the expresison starts
	 * @return the expression result
	 */
	public String embeddedExpressionAsString(String expression, int line,
			int col) {
		try {
			final IEmbeddedExpression expr = expressionFactory
			.createExpression(expression);
			return expr.evalAsString(this);
		} catch (JET2TagException e) {
			logError(new TagInfo(expression, line, col, new String[0],
					new String[0]), e.getMessage(), null);
			return "";
		}
	}

	/**
	 * Return the expression factory for parsing embedded expressions withing
	 * strings.
	 * 
	 * @return the expressionFactory
	 */
	final public EmbeddedExpressionFactory getExpressionFactory() {
		return expressionFactory;
	}

	public void addException(Exception e){
		exceptions.add(e);
	}

	public boolean removeException(Exception e){
		return exceptions.remove(e);
	}

	public List getExceptions(){
		return exceptions;
	}

	public CompileMessage getCompileMessage() {
		return compileMessage;
	}

	public void setCompileMessage(CompileMessage compileMessage) {
		this.compileMessage = compileMessage;
	}

	public Map<TagInfo, Map> getTagVariables() {
		return tagVariables;
	}

	public void setTagVariable(TagInfo tagInfo, Map map) {
		tagVariables.put(tagInfo, map);
	}

	public Map getTagVariable(TagInfo tagInfo){
		return tagVariables.get(tagInfo);
	}

	public Map getTagVariable(int line, int col){
		Iterator<TagInfo> it = tagVariables.keySet().iterator();
		while (it.hasNext()) {
			TagInfo tagInfo = it.next();
			int tagLine = tagInfo.getLine();
			int tagCol = tagInfo.getCol();
			if(line == tagLine && col == tagCol){
				return tagVariables.get(tagInfo);
			}
		}
		return Collections.emptyMap();
	}

	public void removeTagVariable(TagInfo tagInfo){
		tagVariables.remove(tagInfo);
	}

	public void setTagCollection(TagInfo tagInfo, Object object) {
		tagCollection.put(tagInfo, object);
	}

	public Object getTagCollection(TagInfo tagInfo){
		return tagCollection.get(tagInfo);
	}
	
	public Object getTagCollection(int line, int col){
		Iterator<TagInfo> it = tagCollection.keySet().iterator();
		while (it.hasNext()) {
			TagInfo tagInfo = it.next();
			int tagLine = tagInfo.getLine();
			int tagCol = tagInfo.getCol();
			if(line == tagLine && col == tagCol){
				return tagCollection.get(tagInfo);
			}
		}
		return Collections.emptyMap();
	}

	public void removeTagCollection(TagInfo tagInfo){
		tagCollection.remove(tagInfo);
	}

	public void clear(){
		logListeners.clear();
		extendedContextData.clear();
		privateData.clear();
		sessionVariables.clear();
		globalVariables.clear();
		exceptions.clear();
		tagVariables.clear();
		tagCollection.clear();
	}
	
	public JET2Context copy(){
		JET2Context copy = new JET2Context(null);
		copy.logListeners.addAll(logListeners);
		copy.extendedContextData.putAll(extendedContextData);
		copy.privateData.putAll(privateData);
		copy.sessionVariables.putAll(sessionVariables);
		copy.globalVariables.putAll(globalVariables);
		copy.exceptions.addAll(exceptions);
		copy.tagCollection.putAll(tagCollection);
		return copy;
	}
	
//	public List<StackTraceElement[]> getSetVarStacks() {
//		return setVarStacks;
//	}

}
