/**
 * <copyright>
 *
 * Copyright (c) 2006 IBM Corporation and others.
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

package org.eclipse.jet.internal.ui.launch;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.ui.IDebugUIConstants;
import org.eclipse.debug.ui.console.ConsoleColorProvider;
import org.eclipse.debug.ui.console.IConsole;
import org.eclipse.debug.ui.console.IConsoleColorProvider;
import org.eclipse.jet.internal.JETPreferences;
import org.eclipse.jet.internal.launch.JETProcess;
import org.eclipse.jet.internal.launch.JETStreamsProxy;
import org.eclipse.jet.ui.Activator;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;

/**
 * Colorize the JET execution console
 *
 */
public class JETConsoleColorProvider extends ConsoleColorProvider implements IConsoleColorProvider {

	private Map colors;
	/**
	 * 
	 */
	public JETConsoleColorProvider() {
		super();
	}

	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.console.IConsoleColorProvider#connect(org.eclipse.debug.core.model.IProcess, org.eclipse.debug.ui.console.IConsole)
	 */
	public void connect(IProcess process, IConsole console) {
		colors = new HashMap();
		JETProcess jetProcess = (JETProcess) process;
		final JETStreamsProxy jetStreamsProxy = (JETStreamsProxy) jetProcess
				.getStreamsProxy();

		if (jetStreamsProxy != null) {
			if (jetStreamsProxy.getWarningStreamMonitor() != null) {
				console.connect(jetStreamsProxy.getWarningStreamMonitor(),
						JETStreamsProxy.JET_WARNING_STREAM);
			}
			if (jetStreamsProxy.getTraceStreamMonitor() != null) {
				console.connect(jetStreamsProxy.getTraceStreamMonitor(),
						JETStreamsProxy.JET_TRACE_STREAM);
			}
			if (jetStreamsProxy.getDebugStreamMonitor() != null) {
				console.connect(jetStreamsProxy.getDebugStreamMonitor(),
						JETStreamsProxy.JET_DEBUG_STREAM);
			}
		}
		super.connect(process, console);
	}

	public void disconnect() {
		for (Iterator i = colors.values().iterator(); i.hasNext();) {
			Color color = (Color) i.next();
			color.dispose();
		}
		colors.clear();
		super.disconnect();
	}
	
	private Color getPrefColor(String prefKey) {
		Color color = (Color) colors.get(prefKey);
		if(color == null) {
			final IPreferenceStore store = Activator.getDefault().getPreferenceStore();
			final RGB rgb = PreferenceConverter.getColor(store, prefKey);
			color = new Color(Display.getCurrent(), rgb);
			colors.put(prefKey, color);
		}
		return color;
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.debug.ui.console.IConsoleColorProvider#getColor(java.lang.String)
	 */
	public Color getColor(String streamIdentifer) {
		if (streamIdentifer.equals(IDebugUIConstants.ID_STANDARD_OUTPUT_STREAM)) {
			return getPrefColor(JETPreferences.CONSOLE_INFO_COLOR);
		}
		if (streamIdentifer.equals(IDebugUIConstants.ID_STANDARD_ERROR_STREAM)) {
			return getPrefColor(JETPreferences.CONSOLE_ERROR_COLOR);
		}				
		if (streamIdentifer.equals(JETStreamsProxy.JET_DEBUG_STREAM)) {
			return getPrefColor(JETPreferences.CONSOLE_DEBUG_COLOR);
		}
		if (streamIdentifer.equals(JETStreamsProxy.JET_TRACE_STREAM)) {
			return getPrefColor(JETPreferences.CONSOLE_TRACE_COLOR);
		}
		if (streamIdentifer.equals(JETStreamsProxy.JET_WARNING_STREAM)) {
			return getPrefColor(JETPreferences.CONSOLE_WARNING_COLOR);
		}
		return super.getColor(streamIdentifer);
	}

	public boolean isReadOnly() {
		return true;
	}

}
