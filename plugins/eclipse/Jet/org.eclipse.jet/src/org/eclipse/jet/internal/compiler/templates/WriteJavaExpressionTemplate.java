package org.eclipse.jet.internal.compiler.templates;

import org.eclipse.jet.JET2Context;
import org.eclipse.jet.JET2Template;
import org.eclipse.jet.JET2Writer;
import org.eclipse.jet.compiler.JET2Expression;

public class WriteJavaExpressionTemplate implements JET2Template {

    public WriteJavaExpressionTemplate() {
        super();
    }

    private static final String NL = System.getProperty("line.separator"); //$NON-NLS-1$

    public void generate(final JET2Context context, final JET2Writer __out) {
        JET2Writer out = __out;

final String indent = (String)context.getVariable("indent"); //$NON-NLS-1$
final JET2Expression expr = (JET2Expression)context.getVariable("element"); //$NON-NLS-1$

        out.write( indent );
        out.write("out.write(");  //$NON-NLS-1$        
        out.write( expr.getJavaContent() );
        out.write(");");  //$NON-NLS-1$        
        out.write(NL);         
    }
}
