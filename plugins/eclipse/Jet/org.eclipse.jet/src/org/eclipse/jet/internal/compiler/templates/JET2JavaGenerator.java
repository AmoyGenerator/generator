package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.compiler.*;
import org.eclipse.jet.compiler.*;
import org.eclipse.jet.transform.TransformContextExtender;
import org.eclipse.jet.taglib.TagLibraryReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;

public class JET2JavaGenerator implements JET2Template {


    private XMLElement[] getAllXMLTags(JET2CompilationUnit cu) {
        final List result = new ArrayList();
    	cu.accept(new DefaultJET2ASTVisitor() {
	    	public void visit(XMLBodyElement element) {
		    	result.add(element);
		    }
		
			public void visit(XMLEmptyElement element) {
				result.add(element);
			}
		});
        return (XMLElement[])result.toArray(new XMLElement[result.size()]);
    }
    
    private JET2Declaration[] getAllJavaDeclarations(JET2CompilationUnit cu) {
        final List result = new ArrayList();
    	cu.accept(new DefaultJET2ASTVisitor() {
	    	public void visit(JET2Declaration declaration) {
		    	result.add(declaration);
		    }
		});
        return (JET2Declaration[])result.toArray(new JET2Declaration[result.size()]);
    }

    public JET2JavaGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

	JET2CompilationUnit cu = (JET2CompilationUnit)context.getVariable("cu"); //$NON-NLS-1$
	
	final JET2Declaration[] allMemberDeclarations = getAllJavaDeclarations(cu);
	final XMLElement[] allXMLElements = getAllXMLTags(cu);
		
	final TagLibraryReference[] tlRefs = cu.getTagLibraryReferences();

	MethodBodyCreator generatedBody = new MethodBodyCreator(TransformContextExtender.getInstance(context).getLoader());
	cu.accept(generatedBody);

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
 if( allXMLElements.length > 0) { 
        out.write("import org.eclipse.jet.taglib.RuntimeTagElement;");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("import org.eclipse.jet.taglib.TagInfo;");  //$NON-NLS-1$        
        out.write(NL);         
 } 
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
        out.write(" implements JET2Template {");  //$NON-NLS-1$        
        out.write(NL);         
 if( allXMLElements.length > 0) { 
    for(int i = 0; i < tlRefs.length; i++ ) { 
        out.write("    public static final String _jetns_");  //$NON-NLS-1$        
        out.write( tlRefs[i].getPrefix() );
        out.write(" = \"");  //$NON-NLS-1$        
        out.write( tlRefs[i].getTagLibraryId() );
        out.write("\"; //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
    } 
 } 
	for(int i = 0; i < allMemberDeclarations.length; i++) { 
        out.write(NL);         
        out.write( allMemberDeclarations[i].getJavaContent() );
        out.write(NL);         
  } 
        out.write(NL);         
        out.write("\tpublic ");  //$NON-NLS-1$        
        out.write( cu.getOutputJavaClassName() );
        out.write("() {");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("\t\tsuper();");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("\t}");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("\tpublic void generate(final JET2Context context, JET2Writer out) {");  //$NON-NLS-1$        
        out.write(NL);         
	for(int i = 0; i < allXMLElements.length; i++) { 
	    context.setVariable("element", allXMLElements[i]); //$NON-NLS-1$

        out.write("\t\t\t<c:include template=\"templates/tagDataDeclarations.jet\"/>");  //$NON-NLS-1$        
        out.write(NL);         
  } 
        out.write( generatedBody.getBuffer() );
        out.write(NL);         
        out.write("\t}");  //$NON-NLS-1$        
        out.write(NL);         
        out.write(NL);         
        out.write("}");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
