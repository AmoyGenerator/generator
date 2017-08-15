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
package org.eclipse.jet.internal.core.compiler;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Include dependencies for a JET transformation/compiler
 *
 */
public class IncludeDependencies implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 3810681358080776836L;

	private static final String[] EMPTY_STRINGS_ARRAY = new String[0];
	
	private Map dependentPathToTemplates = new HashMap(); // Map<String, Set<String>>
	private transient Map templatePathToDependencies = new HashMap();  // Map<String, Set<String>>
	
	public void addDependencies(String templatePath, String dependencies[]) {
		Set dependents = (Set)templatePathToDependencies.get(templatePath);
		if(dependents == null) {
			dependents = new HashSet(Arrays.asList(dependencies));
			templatePathToDependencies.put(templatePath, dependents);
		} else {
			dependents.addAll(Arrays.asList(dependencies));
		}
		
		for (int i = 0; i < dependencies.length; i++) {
			final String dependentPath = dependencies[i];
			
			Set templates = (Set) dependentPathToTemplates.get(dependentPath);
			
			if(templates == null) {
				templates = new HashSet();
				dependentPathToTemplates.put(dependentPath, templates);
			}
			
			templates.add(templatePath);
		}
	}
	
	public void removeDependencies(String templatePath) {
		Set dependencies = (Set) templatePathToDependencies.remove(templatePath);
		if(dependencies != null) {
			for (Iterator i = dependencies.iterator(); i.hasNext();) {
				String dependentPath = (String) i.next();
				final Set templates = (Set) dependentPathToTemplates.get(dependentPath);
				if(templates != null) {
					templates.remove(templatePath);
					if(templates.size() == 0) {
						dependentPathToTemplates.remove(dependentPath);
					}
				}
			}
		}
	}
	
	public String[] getAffectedTemplates(String dependency) {
		final Set templates = (Set) dependentPathToTemplates.get(dependency);
		return templates == null ? EMPTY_STRINGS_ARRAY : (String[])templates.toArray(EMPTY_STRINGS_ARRAY);
	}

	private void writeObject(ObjectOutputStream s) throws IOException {
		s.defaultWriteObject();
	}
	
	private void readObject(ObjectInputStream s) throws IOException, ClassNotFoundException{
		s.defaultReadObject();

		// rebuild the reverse map
		templatePathToDependencies = new HashMap();
		for (Iterator i = dependentPathToTemplates.entrySet().iterator(); i.hasNext();) {
			Map.Entry entry = (Map.Entry) i.next();
			String dependentPath = (String) entry.getKey();
			Set templates = (Set) entry.getValue();
			for (Iterator j = templates.iterator(); j.hasNext();) {
				String templatePath = (String) j.next();
				Set dependents = (Set)templatePathToDependencies.get(templatePath);
				if(dependents == null) {
					dependents = new HashSet();
					templatePathToDependencies.put(templatePath, dependents);
				}
				
				dependents.add(dependentPath);
			}
		}
	}
}
