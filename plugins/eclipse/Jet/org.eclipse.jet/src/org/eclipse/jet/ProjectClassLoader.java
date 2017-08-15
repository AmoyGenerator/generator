package org.eclipse.jet;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.IPackageFragmentRoot;
import org.eclipse.jdt.core.JavaModelException;

/**
 * 根据工作空间中的项目得到项目的classloader
 * @author xyu
 *
 */
public class ProjectClassLoader extends URLClassLoader {

  public ProjectClassLoader(IJavaProject project) throws JavaModelException {
      super(getURLSFromProject(project, null), Thread.currentThread()
              .getContextClassLoader());
  }

  public ProjectClassLoader(IJavaProject project, URL[] extraUrls)
          throws JavaModelException {
      super(getURLSFromProject(project, extraUrls), Thread.currentThread()
              .getContextClassLoader());
  }

  @SuppressWarnings("deprecation")
  private static URL[] getURLSFromProject(IJavaProject project,
          URL[] extraUrls) throws JavaModelException {
      List<URL> list = new ArrayList<URL>();
      if (null != extraUrls) {
          for (int i = 0; i < extraUrls.length; i++) {
              list.add(extraUrls[i]);
          }
      }

      IPackageFragmentRoot[] roots = project.getAllPackageFragmentRoots();
     
      IPath installPath = ResourcesPlugin.getWorkspace().getRoot()
              .getLocation();

      for (int i = 0; i < roots.length; i++) {
          try {
              if (roots[i].isArchive()) {
                  File f = new File(FileLocator.resolve(
                          installPath.append(roots[i].getPath()).toFile()
                                  .toURL()).getFile());
                  if (!f.exists()) {
                      f = new File(FileLocator.resolve(
                              roots[i].getPath().makeAbsolute().toFile()
                                      .toURL()).getFile());
                  }
                  list.add(f.toURL());
              } else {
                  IPath path = roots[i].getJavaProject().getOutputLocation();
                  if (path.segmentCount() > 1) {
                      IWorkspaceRoot root = ResourcesPlugin.getWorkspace()
                              .getRoot();
                      path = root.getFolder(path).getLocation();
                      list.add(path.toFile().toURL());
                  } else {
                      path = roots[i].getJavaProject().getProject()
                              .getLocation();
                      list.add(path.toFile().toURL());
                  }
              }
          } catch (Exception e) {
          }
      }

      URL[] urls = new URL[list.size()];
      int index = 0;
      for (Iterator<URL> i = list.iterator(); i.hasNext(); index++) {
          urls[index] = i.next();
      }
      return urls;
  }
  
}
