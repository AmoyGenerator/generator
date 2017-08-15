package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import java.util.Iterator;
import org.eclipse.jet.taglib.JET2TagException;
import org.eclipse.jet.internal.JavaUtil;
import org.eclipse.jet.compiler.XMLElement;
import org.eclipse.jet.internal.compiler.MethodBodyCreator;

public class TagDataDeclGenerator implements JET2Template {

    public TagDataDeclGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

    XMLElement element;
    try {
	    element  = (XMLElement)context.getVariable("element"); //$NON-NLS-1$
    } catch( JET2TagException e) {
        out.write(e.toString());
        return;
    }
		
	String varName = MethodBodyCreator.tagInfoVariableName(element);

        out.write("        final TagInfo ");  //$NON-NLS-1$        
        out.write( varName );
        out.write(" = new TagInfo(\"");  //$NON-NLS-1$        
        out.write(element.getName());
        out.write("\", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            ");  //$NON-NLS-1$        
        out.write( element.getLine() );
        out.write(", ");  //$NON-NLS-1$        
        out.write( element.getColumn() );
        out.write(",");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            new String[] {");  //$NON-NLS-1$        
        out.write(NL);         
 			for (Iterator i = element.getAttributes().keySet().iterator(); i.hasNext();) { 
                String attrName = (String) i.next(); 
        out.write("                \"");  //$NON-NLS-1$        
        out.write( attrName );
        out.write("\", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
          } 
        out.write("            },");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("            new String[] {");  //$NON-NLS-1$        
        out.write(NL);         
 			for (Iterator i = element.getAttributes().values().iterator(); i.hasNext();) { 
                String attrValue = (String) i.next(); 
        out.write("                \"");  //$NON-NLS-1$        
        out.write( JavaUtil.escapeJavaString(attrValue.toCharArray()) );
        out.write("\", //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
          } 
        out.write("            } );");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
