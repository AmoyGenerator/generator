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
package org.eclipse.jet.xpath.inspector;


import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * Manager for finding XPath Inspectors for classes
 */
public final class InspectorManager
{

  /**
   * Inspector for unknown nodes.
   */
  private static final class UnknownObjectInspector implements INodeInspector {

    private static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];
    
    public ExpandedName expandedNameOf(Object node)
    {
      return new ExpandedName(""); //$NON-NLS-1$
    }

    public Object[] getChildren(Object node)
    {
      return EMPTY_OBJECT_ARRAY;
    }

    public Object getDocumentRoot(Object node)
    {
      return null;
    }

    public NodeKind getNodeKind(Object obj)
    {
      return null;
    }

    public Object getParent(Object obj)
    {
      return null;
    }

    public String nameOf(Object node)
    {
      return ""; //$NON-NLS-1$
    }

    public String stringValueOf(Object node)
    {
      return node.toString();
    }

    public boolean testExpandedName(Object node, ExpandedName testName)
    {
      return false;
    }
    
  }
  
  private final INodeInspector unknownObjectInspector = new UnknownObjectInspector();
  
  private static InspectorManager instance = null;

  private Map inspectorInstances = new HashMap();

  private Map cachedInspectorsByConcreteClass = new HashMap();

  private final Map registeredInspectorsByInspectableClass = new HashMap();

  /**
   * 
   */
  private InspectorManager()
  {
    super();
  }

  public static InspectorManager getInstance()
  {
    if (instance == null)
    {
      instance = new InspectorManager();

//      // initialize standard inspectors...
//      instance.registerInspector(new Class []{ org.w3c.dom.Node.class }, DOMInspector.class);
//      instance.registerInspector(new Class []{ org.eclipse.emf.ecore.EObject.class }, EObjectInspector.class);
//      instance.registerInspector(new Class []{ org.eclipse.emf.ecore.resource.Resource.class }, EMFResourceInspector.class);
//      instance.registerInspector(
//        new Class []{ org.eclipse.emf.ecore.EStructuralFeature.Setting.class },
//        EStructuralFeatureSettingInspector.class);
//      instance.registerInspector(new Class []{ EMFEAttrAsElementWrapper.class }, EMFEAttrAsElementWrapperInspector.class);
//      instance.registerInspector(new Class []{ EMFXMLNodeWrapper.class }, EMFXMLNodeWrapperInspector.class);
    }
    return instance;
  }

  /**
   * Compute class order per org.eclipse.core.runtime.IAdapterManager.computeClassOrder().
   * <P>
   * This method is copied from org.eclipse.core.internal.runtime.AdapterManager.
   * @param adaptable
   * @param classes
   */
  private void computeClassOrder(Class adaptable, Collection classes)
  {
    Class clazz = adaptable;
    Set seen = new HashSet(4);
    while (clazz != null)
    {
      classes.add(clazz);
      computeInterfaceOrder(clazz.getInterfaces(), classes, seen);
      clazz = clazz.getSuperclass();
    }
  }

  /**
   * Helper to {@link #computeClassOrder(Class, Collection)}.
   * <P>
   * This method is copied form org.eclipse.core.internal.runtime.AdapterManager.
   * @param interfaces
   * @param classes
   * @param seen
   */
  private void computeInterfaceOrder(Class[] interfaces, Collection classes, Set seen)
  {
    List newInterfaces = new ArrayList(interfaces.length);
    for (int i = 0; i < interfaces.length; i++)
    {
      Class interfac = interfaces[i];
      if (seen.add(interfac))
      {
        //note we cannot recurse here without changing the resulting interface order
        classes.add(interfac);
        newInterfaces.add(interfac);
      }
    }
    for (Iterator it = newInterfaces.iterator(); it.hasNext();)
      computeInterfaceOrder(((Class)it.next()).getInterfaces(), classes, seen);
  }

  public synchronized void registerInspector(Class[] inspectableClasses, Class inspectorClass)
  {
    for (int i = 0; i < inspectableClasses.length; i++)
    {
      registeredInspectorsByInspectableClass.put(inspectableClasses[i].getName(), inspectorClass);
    }
    flushCaches();
  }
  
  public synchronized void registerInspector(String[] inspectableClassNames, Class inspectorClass)
  {
    for (int i = 0; i < inspectableClassNames.length; i++)
    {
      registeredInspectorsByInspectableClass.put(inspectableClassNames[i], inspectorClass);
    }
    flushCaches();
  }
  
  public INodeInspector getInspector(Object object)
  {
    if(object == null)
    {
      return null;
    }
    String inspectableClassName = object.getClass().getName();
    // make a local copy to avoid concurrent flush
    Map cache = cachedInspectorsByConcreteClass;
    INodeInspector inspector = (INodeInspector)cache.get(inspectableClassName);

    if (inspector == null)
    {
      List classOrder = new ArrayList();
      computeClassOrder(object.getClass(), classOrder);
      for (Iterator i = classOrder.iterator(); i.hasNext();)
      {
        Class aClass = (Class)i.next();
        Class inspectorClass = (Class)registeredInspectorsByInspectableClass.get(aClass.getName());
        if (inspectorClass != null)
        {
          inspector = getInspectorInstance(inspectorClass);
          break;
        }

      }
      if (inspector == null)
      {
        inspector = unknownObjectInspector;
      }
      cache.put(inspectableClassName, inspector);
    }
    return inspector;
  }

  /**
   * @param inspectorClass
   * @return
   * @throws InstantiationException
   * @throws IllegalAccessException
   */
  private INodeInspector getInspectorInstance(Class inspectorClass)
  {
    INodeInspector inspector = null;
    try
    {
      // make a local copy to avoid a concurrent flush
      final Map inspectors = inspectorInstances;
      inspector = (INodeInspector)inspectors.get(inspectorClass.getName());
      if (inspector == null)
      {
        inspector = (INodeInspector)inspectorClass.newInstance();
        inspectors.put(inspectorClass.getName(), inspector);
      }
    }
    catch (InstantiationException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IllegalAccessException e)
    {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    return inspector;
  }
  
  private synchronized void flushCaches()
  {
    cachedInspectorsByConcreteClass = new HashMap();
    inspectorInstances = new HashMap();
  }

  public synchronized void unregisterInspector(String[] inspectableClassNames, String inspectorClassName)
  {
    for (int i = 0; i < inspectableClassNames.length; i++)
    {
      registeredInspectorsByInspectableClass.remove(inspectableClassNames[i]);
    }
    flushCaches();
  }

}
