package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.compiler.TextElement;
import org.eclipse.jet.internal.JavaUtil;

public class WriteTextElementTemplate implements JET2Template {

    public WriteTextElementTemplate() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

final String indent = (String)context.getVariable("indent"); //$NON-NLS-1$
final TextElement text = (TextElement)context.getVariable("element"); //$NON-NLS-1$

 if(text.getText().length > 0) { 
        out.write( indent );
        out.write("out.write( ");  //$NON-NLS-1$        
        out.write( JavaUtil.asJavaQuotedString(text.getText()) );
        out.write(" ); //$NON-NLS-1$");  //$NON-NLS-1$        
        out.write(NL);         
 } 
    }
}
