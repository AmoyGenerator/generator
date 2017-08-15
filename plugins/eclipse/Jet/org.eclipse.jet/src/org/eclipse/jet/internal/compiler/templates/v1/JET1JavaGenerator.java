package org.eclipse.jet.internal.compiler.templates.v1;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.core.parser.ast.*;
import org.eclipse.jet.internal.compiler.SafeJETASTVisitor;
import java.util.Iterator;
import org.eclipse.jet.internal.JavaUtil;

public class JET1JavaGenerator implements JET2Template {

    public JET1JavaGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

	JETCompilationUnit cu = (JETCompilationUnit)context.getVariable("cu"); //$NON-NLS-1$

        out.write("package ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaPackage() );
        out.write(";");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
 for(Iterator i=cu.getImports().iterator();i.hasNext();) { 
	String importName = (String)i.next();
        out.write("import ");  //$NON-NLS-1$        
        out.write(importName);
        out.write(";");  //$NON-NLS-1$        
        out.write(NL);         
 } 
        out.write(NL);         
        out.write("public class ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write(NL);         
        out.write("{");  //$NON-NLS-1$        
        out.write(NL);         
 cu.accept(new SafeJETASTVisitor(context,out) {
      public boolean visit(JavaDeclaration decl) {

 writeAndRecord(decl); 
        out.write("\t");  //$NON-NLS-1$        
        out.write(NL);         

		return true;
      }
   });

        out.write("  protected static String nl;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("  public static synchronized ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write(" create(String lineSeparator)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("  {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    nl = lineSeparator;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write(" result = new ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write("();");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    nl = null;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    return result;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("  }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("  protected final String NL = nl == null ? (System.getProperties().getProperty(\"line.separator\")) : nl; //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
 cu.accept(new SafeJETASTVisitor(context,out) {
	  private int textCount = 0;
      public boolean visit(TextElement text) {
          if(text.getText().length > 0) {
              textCount++;

        out.write("  protected final String TEXT_");  //$NON-NLS-1$        
        out.write(textCount);
        out.write(" = ");  //$NON-NLS-1$        
        out.write(JavaUtil.asJavaQuoteStringWithNLRemoved(text.getText()));
        out.write(";");  //$NON-NLS-1$        
        out.write(JavaUtil.nlsCommentsForJavaQuoteStringWithNLRemoved(text.getText()));
        out.write(NL);         

         }
		 return true;
      }
   });

        out.write(NL);         
        out.write("  public String generate(Object argument)");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("  {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    final StringBuffer stringBuffer = new StringBuffer();");  //$NON-NLS-1$        
        out.write(NL);         
 cu.accept(new SafeJETASTVisitor(context,out) {
	  private int textCount = 0;
      public boolean visit(TextElement text) {
          if(text.getText().length > 0) {
              textCount++;

        out.write("    stringBuffer.append(TEXT_");  //$NON-NLS-1$        
        out.write(textCount);
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         

          }
		  return true;
      }
      public boolean visit(JavaScriptlet scriptlet) {

 writeAndRecord(scriptlet); 
        out.write(NL);         

		return true;
      }
      public boolean visit(JavaExpression expr) {

        out.write("    stringBuffer.append(");  //$NON-NLS-1$        
 writeAndRecord(expr); 
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         

		return true;
  }
   });

        out.write("    return stringBuffer.toString();");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("  }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("}");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
