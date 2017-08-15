package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.compiler.GenXMLElement;

public class XMLStartGenerator implements JET2Template {

    public XMLStartGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

	GenXMLElement element = (GenXMLElement) context.getVariable("element"); //$NON-NLS-1$

        out.write("<c:get select=\"$indent\"/>RuntimeTagElement ");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(" = context.getTagFactory().createRuntimeTag(_jetns_");  //$NON-NLS-1$        
        out.write( element.getNSPrefix() );
        out.write(", \"");  //$NON-NLS-1$        
        out.write( element.getTagNCName() );
        out.write("\", \"");  //$NON-NLS-1$        
        out.write( element.getName() );
        out.write("\", ");  //$NON-NLS-1$        
        out.write( element.getTagInfoVariable() );
        out.write("); //$NON-NLS-1$ //$NON-NLS-2$");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("<c:get select=\"$indent\"/>");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(".setRuntimeParent(");  //$NON-NLS-1$        
        out.write( element.getParentTagVariable() );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("<c:get select=\"$indent\"/>");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(".setTagInfo(");  //$NON-NLS-1$        
        out.write( element.getTagInfoVariable() );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("<c:get select=\"$indent\"/>");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(".doStart(context, out);");  //$NON-NLS-1$        
        out.write(NL);         
  if(element.hasBody()) { 
     if(element.requiresNewWriter()) { 
        out.write("<c:get select=\"$indent\"/>JET2Writer ");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write("_saved_out = out;");  //$NON-NLS-1$        
        out.write(NL);         
     } 
        out.write("<c:get select=\"$indent\"/>while (");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(".okToProcessBody()) {");  //$NON-NLS-1$        
        out.write(NL);         
     if(element.requiresNewWriter()) { 
        out.write("<c:get select=\"$indent\"/>    out = out.newNestedContentWriter();");  //$NON-NLS-1$        
        out.write(NL);         
     } 
  } 
    }
}
