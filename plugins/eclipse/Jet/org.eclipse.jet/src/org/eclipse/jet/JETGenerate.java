package org.eclipse.jet;

import java.io.IOException;

import javax.xml.transform.TransformerException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.jdt.core.IJavaProject;
import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.JavaModelException;
import org.eclipse.jet.CoreJETException;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.core.parser.ast.JETCompilationUnit;
import org.eclipse.jet.internal.compiler.CompilationHelper;


public class JETGenerate {

  private static IJavaProject javaProject;
  private static IProject project;
  private static String packageName;
  private static String className;
  private static ProjectClassLoader loader;
  private static Class compiledClass;

  /**
   * 得到生成代码的字符串
   * 如果编译模板文件失败则返回null
   * @throws JavaModelException 
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws IOException 
   * @throws CoreJETException 
   * @throws TransformerException 
   * @throws ClassNotFoundException 
   */

  public static void generate(String path, JET2Context context, JET2Writer out) throws Exception
  { 
    if(init() == true){
      compileTemplate(path);
      generateTemplate(context, out);
    }
  }

  /**
   * 初始化
   * @throws JavaModelException 
   */
  private static boolean init() throws Exception{

    project = ResourcesPlugin.getWorkspace().getRoot().getProject("JET2Project");
    if (project == null)
    {
      return false;
    }
    if (!project.exists() || !project.isOpen())
    {
      return false;
    }
    javaProject = JavaCore.create(project);
    loader =  new ProjectClassLoader(javaProject);
    return true;
  }


  /**
   * 编译模板
   * @param path
   * @return
   * @throws JavaModelException
   */
  private static void compileTemplate(String path) throws Exception{
    //*****编译模板*****
    CompilationHelper jetCompilationHelper = new CompilationHelper(project);
    JETCompilationUnit target = jetCompilationHelper.getAST("../"+path);
    // 得到编译后的文件内容
    String javaSource = jetCompilationHelper.getJavaCode(target);
    //得到模板中设置编译文件的包名
    packageName = target.getOutputJavaPackage();
    //得到模板中设置编译文件的类名
    className = target.getOutputJavaClassName();

    //编译路径
    String compliedPath = packageName+"."+className;
    compiledClass = loader.loadClass(compliedPath);
    
  }

  /**
   * 生成代码字符串并返回
   * @param vars
   * @param modelContext
   * @return
   * @throws IllegalAccessException 
   * @throws InstantiationException 
   * @throws TransformerException 
   * @throws IOException 
   * @throws CoreJETException 
   */
  private static void generateTemplate(JET2Context context, JET2Writer out) throws Exception{
    //*****生成结果字符串*****

    //得到编译后的模板文件

    Object compiledObject = compiledClass.newInstance();

    JET2Template jet2template = (JET2Template)compiledObject;

    //生成结果字符串
    jet2template.generate(context, out);

  }

}

