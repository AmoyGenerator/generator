package org.eclipse.jet.internal.compiler.templates.v2;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.core.parser.ast.*;
import org.eclipse.jet.internal.compiler.V2CodeGenVisitor;
import org.eclipse.jet.internal.compiler.HasNewlinesUtil;
import org.eclipse.jet.internal.compiler.TagUsageVisitor;
import org.eclipse.jet.taglib.TagLibraryReference;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import org.eclipse.jet.internal.JavaUtil;

public class JET2JavaGeneratorNew implements JET2Template {

    public JET2JavaGeneratorNew() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

    JETCompilationUnit cu = (JETCompilationUnit)context.getVariable("cu"); //$NON-NLS-1$
    TagUsageVisitor tuv = new TagUsageVisitor();
    cu.accept(tuv);
    TagLibraryReference[] tlrefs = tuv.getUsedTagLibraryReferences(cu);
    Arrays.sort(tlrefs, new Comparator() {
       public int compare(Object o1, Object o2) {
          return ((TagLibraryReference)o1).getTagLibraryId().compareTo(((TagLibraryReference)o2).getTagLibraryId());
       }
    });
    final String nlConstantName = "NL"; //$NON-NLS-1$

cu.accept(new V2CodeGenVisitor(context,out, "") { //$NON-NLS-1$ 
    public boolean visit(Comment comment) {
        final String text = comment.getCommentText();
        final String leadIn = " * "; //$NON-NLS-1$
        if(isHeaderComment(text)) {

        out.write("/*");  //$NON-NLS-1$        
        out.write(NL);         
 out.write(formatComment(text, leadIn)) ;
        out.write(" */");  //$NON-NLS-1$        
        out.write(NL);         

        }
        return true;
    }
});

        out.write("package ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaPackage() );
        out.write(";");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("import org.eclipse.jet.JET2Context;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import org.eclipse.jet.JET2Template;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import org.eclipse.jet.JET2Writer;");  //$NON-NLS-1$        
        out.write(NL);         
 if(tuv.hasTags()) { 
        out.write("import org.eclipse.jet.taglib.RuntimeTagElement;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import org.eclipse.jet.taglib.TagInfo;");  //$NON-NLS-1$        
        out.write(NL);         
 } 
 for(Iterator i=cu.getImports().iterator();i.hasNext();) { 
        out.write("import ");  //$NON-NLS-1$        
        out.write( (String)i.next() );
        out.write(";");  //$NON-NLS-1$        
        out.write(NL);         
 } 
        out.write(NL);         
cu.accept(new V2CodeGenVisitor(context,out, "") { //$NON-NLS-1$ 
    public boolean visit(Comment comment) {
        final String text = comment.getCommentText();
        final String leadIn = " * "; //$NON-NLS-1$
        if(isClassComment(text)) {

        out.write("/**");  //$NON-NLS-1$        
        out.write(NL);         
 out.write(formatComment(text, leadIn)) ;
        out.write(" */");  //$NON-NLS-1$        
        out.write(NL);         

        }
        return true;
    }
});

        out.write("public class ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write(" implements JET2Template {");  //$NON-NLS-1$        
        out.write(NL);         
    for(int i = 0; i < tlrefs.length; i++ ) { 
        out.write("    private static final String _jetns_");  //$NON-NLS-1$        
        out.write( tlrefs[i].getPrefix() );
        out.write(" = \"");  //$NON-NLS-1$        
        out.write( tlrefs[i].getTagLibraryId() );
        out.write("\"; //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
    } 
        out.write(NL);         
 cu.accept(new V2CodeGenVisitor(context,out) {
      public boolean visit(JavaDeclaration decl) {

 writeAndRecord(decl); 
        out.write(NL);         

        return true;
      }
   });

        out.write("    public ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write("() {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        super();");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("    }");  //$NON-NLS-1$        
        out.write(NL);         
 if(HasNewlinesUtil.test(cu)) { 
        out.write(NL);         
        out.write("    private static final String ");  //$NON-NLS-1$        
        out.write(nlConstantName);
        out.write(" = System.getProperty(\"line.separator\"); //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
 } 
 if(tuv.hasTags()) { 
        out.write("    ");  //$NON-NLS-1$        
        out.write(NL);         
 cu.accept(new V2CodeGenVisitor(context,out, "    ") { //$NON-NLS-1$
      public boolean visit(XMLBodyElement e) {
          doElementVisit(e);
          return true;
      }
      public boolean visit(XMLEmptyElement e) {
          doElementVisit(e);
          return true;
      }
      public void doElementVisit(XMLElement e) {

        out.write("    private static final TagInfo _td_");  //$NON-NLS-1$        
        out.write( tagInfoVar(e) );
        out.write(" = new TagInfo(\"");  //$NON-NLS-1$        
        out.write(e.getName());
        out.write("\", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            ");  //$NON-NLS-1$        
        out.write( e.getLine() );
        out.write(", ");  //$NON-NLS-1$        
        out.write( e.getColumn() );
        out.write(",");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            new String[] {");  //$NON-NLS-1$        
        out.write(NL);         
          for (Iterator i = e.getAttributes().keySet().iterator(); i.hasNext();) { 
        out.write("                \"");  //$NON-NLS-1$        
        out.write( (String) i.next() );
        out.write("\", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
          } 
        out.write("            },");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            new String[] {");  //$NON-NLS-1$        
        out.write(NL);         
          for (Iterator i = e.getAttributes().values().iterator(); i.hasNext();) { 
                String attrValue = (String) i.next(); 
        out.write("                \"");  //$NON-NLS-1$        
        out.write( JavaUtil.escapeJavaString(attrValue.toCharArray()) );
        out.write("\", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
          } 
        out.write("            } );");  //$NON-NLS-1$        
        out.write(NL);         
    } 
   }); 
 } 
        out.write(NL);         
        out.write("    public void generate(final JET2Context context, final JET2Writer __out) {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        JET2Writer out = __out;");  //$NON-NLS-1$        
        out.write(NL);         
 cu.accept(new V2CodeGenVisitor(context,out, "    ") { //$NON-NLS-1$
    public boolean visit(TextElement text) {
        if(text.getText().length > 0) {
            String[] strings = textConstants(text, nlConstantName);
            for(int i = 0; i < strings.length; i++) {

        out.write("        out.write(");  //$NON-NLS-1$        
        out.write(strings[i]);
        out.write("); ");  //$NON-NLS-1$        
 if(strings[i].startsWith("\"")) { //$NON-NLS-1$ 
        out.write(" //$NON-NLS-1$");  //$NON-NLS-1$        
}
        out.write("        ");  //$NON-NLS-1$        
        out.write(NL);         

            }
        }
        return true;
      }
      public boolean visit(JavaScriptlet scriptlet) {

 writeAndRecord(scriptlet); 
        out.write(NL);         

          return true;
      }
      public boolean visit(JavaExpression expr) {

        out.write("        out.write(");  //$NON-NLS-1$        
 writeAndRecord(expr); 
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         

          return true;
      }
      public boolean visit(EmbeddedExpression expr) {

        out.write("        out.write(context.embeddedExpressionAsString(");  //$NON-NLS-1$        
        out.write( JavaUtil.asJavaQuotedString(expr.getExpression().toCharArray()) );
        out.write(", ");  //$NON-NLS-1$        
        out.write( expr.getLine() );
        out.write(", ");  //$NON-NLS-1$        
        out.write( expr.getColumn());
        out.write(")); //$NON-NLS-1$ //$NON-NLS-2$");  //$NON-NLS-1$        
        out.write(NL);         

          return true;
      }
      public boolean visit(XMLBodyElement e) {

        out.write("        RuntimeTagElement ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(" = context.getTagFactory().createRuntimeTag(_jetns_");  //$NON-NLS-1$        
        out.write( e.getNSPrefix() );
        out.write(", \"");  //$NON-NLS-1$        
        out.write( e.getTagNCName() );
        out.write("\", \"");  //$NON-NLS-1$        
        out.write( e.getName() );
        out.write("\", _td_");  //$NON-NLS-1$        
        out.write( tagInfoVar(e) );
        out.write("); //$NON-NLS-1$ //$NON-NLS-2$");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".setRuntimeParent(");  //$NON-NLS-1$        
        out.write( parentTagVar(e) );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".setTagInfo(_td_");  //$NON-NLS-1$        
        out.write( tagInfoVar(e) );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".doStart(context, out);");  //$NON-NLS-1$        
        out.write(NL);         
 if(requiresOwnWriter(e)) { 
        out.write("        JET2Writer ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write("_saved_out = out;");  //$NON-NLS-1$        
        out.write(NL);         
 } 
        out.write("        while (");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".okToProcessBody()) {");  //$NON-NLS-1$        
        out.write(NL);         
 if(requiresOwnWriter(e)) { 
        out.write("            out = out.newNestedContentWriter();");  //$NON-NLS-1$        
        out.write(NL);         
 } 

          in();
          return true;
      }
      public void endVisit(XMLBodyElement e) {
          out();

        out.write("            ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".handleBodyContent(out);");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        }");  //$NON-NLS-1$        
        out.write(NL);         
 if(requiresOwnWriter(e)) { 
        out.write("        out = ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write("_saved_out;");  //$NON-NLS-1$        
        out.write(NL);         
  } 
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".doEnd();");  //$NON-NLS-1$        
        out.write(NL);         

 
      }
      public boolean visit(XMLEmptyElement e) {

        out.write("        RuntimeTagElement ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(" = context.getTagFactory().createRuntimeTag(_jetns_");  //$NON-NLS-1$        
        out.write( e.getNSPrefix() );
        out.write(", \"");  //$NON-NLS-1$        
        out.write( e.getTagNCName() );
        out.write("\", \"");  //$NON-NLS-1$        
        out.write( e.getName() );
        out.write("\", _td_");  //$NON-NLS-1$        
        out.write( tagInfoVar(e) );
        out.write("); //$NON-NLS-1$ //$NON-NLS-2$");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".setRuntimeParent(");  //$NON-NLS-1$        
        out.write( parentTagVar(e) );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".setTagInfo(_td_");  //$NON-NLS-1$        
        out.write( tagInfoVar(e) );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".doStart(context, out);");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("        ");  //$NON-NLS-1$        
        out.write( tagVar(e) );
        out.write(".doEnd();");  //$NON-NLS-1$        
        out.write(NL);         

          return true;
      }
  

      public boolean visit(Comment comment) {
        final String text = comment.getCommentText();
        final String leadIn = "        //";  //$NON-NLS-1$
        if(isNormalComment(text)) {
			out.write(formatComment(text, leadIn)) ;
         }
          return true;
      }
  

   });

        out.write("    }");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("}");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
