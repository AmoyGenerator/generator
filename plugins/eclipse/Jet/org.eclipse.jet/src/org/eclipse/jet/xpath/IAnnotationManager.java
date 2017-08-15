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
package org.eclipse.jet.xpath;


/**
 * Interface that manages annotations of model elements.
 *
 */
public interface IAnnotationManager
{

  /**
   * Return the annotation object corresponding to the model object. If the
   * manager has not annotation object for the model object, it must create one.
   * Repeated calls to the method with the same argument must return the same
   * result.
   * @param modelObject the model object for which the annotation object is sought.
   * @return the annotation object.
   * @throws NullPointerException if <code>modelObject</code> is <code>null</code>
   */
  public abstract Object getAnnotationObject(Object modelObject);

  /**
   * Test whether the manager has annotations of the given model object.
   * @param modelObject
   * @return <code>true</code> if there are annotations, <code>false</code> otherwise.
   */
  public abstract boolean hasAnnotations(Object modelObject);

}
