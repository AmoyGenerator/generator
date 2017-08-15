package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.internal.compiler.GenXMLElement;

public class XMLEndGenerator implements JET2Template {

    public XMLEndGenerator() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

	GenXMLElement element = (GenXMLElement) context.getVariable("element"); //$NON-NLS-1$

  if(element.hasBody()) { 
        out.write("<c:get select=\"$indent\"/>    ");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(".handleBodyContent(out);");  //$NON-NLS-1$        
        out.write(NL);         
        out.write("<c:get select=\"$indent\"/>}");  //$NON-NLS-1$        
        out.write(NL);         
     if(element.requiresNewWriter()) { 
        out.write("<c:get select=\"$indent\"/>out = ");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write("_saved_out;");  //$NON-NLS-1$        
        out.write(NL);         
     } 
  } 
        out.write("<c:get select=\"$indent\"/>");  //$NON-NLS-1$        
        out.write( element.getTagVariable() );
        out.write(".doEnd();");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
